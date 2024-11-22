import React, { useMemo } from "react";
import { Day, DAYS, getDayOrder, getTimeOrder, TIMES } from "../../api/time/Time";
import { useAttendingHours } from "../../hooks/doctorHooks";
import "../../css/scheduleViewer.css";
import { t } from "i18next";

interface ScheduleViewerProps {
  doctorId: string;
}

const ScheduleViewer: React.FC<ScheduleViewerProps> = ({ doctorId }) => {
  const { data: attendingHours } = useAttendingHours(doctorId);

  // Rango para mostrar las horas de atención
  const [minAttendingHour, maxAttendingHour] = useMemo(() => {
    let min = 16;
    let max = 35;

    attendingHours?.forEach((attendingHour) =>
      attendingHour.hours.forEach((hour) => {
        const hourOrdinal = getTimeOrder(hour);
        min = Math.min(min, hourOrdinal);
        max = Math.max(max, hourOrdinal);
      })
    );

    return [min, max];
  }, [attendingHours]);

  // Para ver si muestro los findes
  const isWeekendEmpty = useMemo(
    () =>
      !attendingHours?.some(
        (attendingHour) =>
          (attendingHour.hours.length > 0) && (getDayOrder(attendingHour.day) === 5 || getDayOrder(attendingHour.day) === 6) 
      ),
    [attendingHours]
  );

  const doesDoctorAttend = (dayIndex: number, timeIndex: number): boolean =>
    attendingHours?.some(
      (attendingHour) =>
        getDayOrder(attendingHour.day) === dayIndex &&
        attendingHour.hours.some((hour) => getTimeOrder(hour) === timeIndex)
    ) ?? false;

  const renderDaySchedule = (dayIndex: number, day: Day) => (
    <div className="daySchedule" key={dayIndex}>
      <div className="scheduleTitle">{t(`day.${day.toLowerCase()}`)}</div>
      {TIMES.map((_, index) => {
        const doctorAttends = doesDoctorAttend(dayIndex, index);
        const isHidden = index < minAttendingHour || index > maxAttendingHour;

        return (
          <div
            key={index}
            data-index={index}
            className={`timeBlock ${doctorAttends ? "selected" : "unselected"} ${
              isHidden ? "hidden" : ""
            }`}
          >
            &nbsp;
          </div>
        );
      })}
    </div>
  );

  return (
    <div className="scheduleContainer">
      {/* Time Labels */}
      <div className="timeLabelContainer">
        <div className="scheduleTitle">{t("form.time")}</div>
        {TIMES.map((block, index) => {
          const isHidden = index < minAttendingHour || index > maxAttendingHour;
          return (
            <div key={index} className={`timeLabel ${isHidden ? "hidden" : ""}`}>
              {block} - {TIMES[(index + 1) % TIMES.length]}
            </div>
          );
        })}
      </div>

      {/* Daily Schedules */}
      {DAYS.map((day, index) =>
        !isWeekendEmpty || index < 5 ? renderDaySchedule(index, day) : null
      )}
    </div>
  );
};

export default ScheduleViewer;

import React, { useState, useMemo, useRef, useEffect } from "react";
import { Button, ButtonGroup } from "react-bootstrap";
import {
  Day,
  DAYS,
  getDayOrder,
  getTimeOrder,
  TIMES,
} from "../../api/time/Time";
import { useAttendingHours } from "../../hooks/doctorHooks";
import "../../css/scheduleSelector.css";
import { t } from "i18next";
import { AttendingHours } from "../../api/doctor/Doctor";

interface ScheduleSelectorProps {
  doctorId: string;
  onScheduleChange: (updatedSchedule: AttendingHours[]) => void;
}

const ScheduleSelector: React.FC<ScheduleSelectorProps> = ({
  doctorId,
  onScheduleChange,
}) => {
  const { data: attendingHours } = useAttendingHours(doctorId);

  const [selectedHours, setSelectedHours] = useState<{
    [key: string]: Set<number>;
  }>({});

  useEffect(() => {
    if (attendingHours) {
      setSelectedHours(
        attendingHours.reduce(
          (acc, attendingHour) => {
            acc[attendingHour.day] = new Set(
              attendingHour.hours.map((hour) => getTimeOrder(hour)),
            );
            return acc;
          },
          {} as { [key: string]: Set<number> },
        ),
      );
    }
  }, [attendingHours]);

  const [isDragging, setIsDragging] = useState(false);
  const [isSelecting, setIsSelecting] = useState(false);
  const scheduleContainerRef = useRef<HTMLDivElement | null>(null);

  const [showWeekends, setShowWeekends] = useState(false);

  const [minAttendingHour, maxAttendingHour] = useMemo(() => {
    let min = 16;
    let max = 35;

    attendingHours?.forEach((attendingHour) =>
      attendingHour.hours.forEach((hour) => {
        const hourOrdinal = getTimeOrder(hour);
        min = Math.min(min, hourOrdinal);
        max = Math.max(max, hourOrdinal);
      }),
    );

    return [min, max];
  }, [attendingHours]);

  const doesDoctorAttend = (dayIndex: number, timeIndex: number): boolean =>
    selectedHours[DAYS[dayIndex]]?.has(timeIndex) ?? false;

const updateSelectedHours = (
  dayIndex: number,
  timeIndex: number,
  shouldSelect: boolean,
) => {
  const day = DAYS[dayIndex];

  const updatedSelectedHours = { ...selectedHours };
  if (!updatedSelectedHours[day]) {
    updatedSelectedHours[day] = new Set();
  }

  if (shouldSelect) {
    updatedSelectedHours[day].add(timeIndex);
  } else {
    updatedSelectedHours[day].delete(timeIndex);
  }

  setSelectedHours(updatedSelectedHours);

  // Para el callback
  const newAttendingHours: AttendingHours[] = Object.keys(updatedSelectedHours).map(
    (dayKey) => ({
      day: dayKey as Day,
      hours: Array.from(updatedSelectedHours[dayKey]).map(
        (timeIdx) => TIMES[timeIdx],
      ),
    }),
  );

  DAYS.forEach((day) => {
    if (!newAttendingHours.some((attendingHour) => attendingHour.day === day)) {
      newAttendingHours.push({ day, hours: [] });
    }
  });

  onScheduleChange(newAttendingHours);
};

  const handleMouseDown = (
    event: React.MouseEvent,
    dayIndex: number,
    timeIndex: number,
  ) => {
    event.preventDefault();
    const doctorAttends = doesDoctorAttend(dayIndex, timeIndex);
    const shouldSelect = !doctorAttends;

    setIsSelecting(shouldSelect);
    setIsDragging(true);
    updateSelectedHours(dayIndex, timeIndex, shouldSelect);
  };

  const handleMouseEnter = (
    event: React.MouseEvent,
    dayIndex: number,
    timeIndex: number,
  ) => {
    event.preventDefault();
    if (isDragging) {
      const doctorAttends = doesDoctorAttend(dayIndex, timeIndex);
      if (isSelecting !== doctorAttends) {
        updateSelectedHours(dayIndex, timeIndex, isSelecting);
      }
    }
  };

  useEffect(() => {
    const handleMouseUp = () => {
      setIsDragging(false);
    };

    document.addEventListener("mouseup", handleMouseUp);

    return () => {
      document.removeEventListener("mouseup", handleMouseUp);
    };
  }, []);

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
            onMouseDown={(e) => handleMouseDown(e, dayIndex, index)}
            onMouseEnter={(e) => handleMouseEnter(e, dayIndex, index)}
          >
            &nbsp;
          </div>
        );
      })}
    </div>
  );

  return (
    <div className="scheduleSelector">
      <div className="scheduleContainer" ref={scheduleContainerRef}>
        <div className="timeLabelContainer">
          <div className="scheduleTitle">{t("form.time")}</div>
          {TIMES.map((block, index) => {
            const isHidden =
              index < minAttendingHour || index > maxAttendingHour;
            return (
              <div
                key={index}
                className={`timeLabel ${isHidden ? "hidden" : ""}`}
              >
                {block} - {TIMES[(index + 1) % TIMES.length]}
              </div>
            );
          })}
        </div>

        {DAYS.map((day, index) => {
          const isWeekend = index === 5 || index === 6;
          if (isWeekend && !showWeekends) return null;
          return renderDaySchedule(index, day);
        })}
      </div>

      <ButtonGroup className="mb-3">
        <Button
          variant={showWeekends ? "primary" : "outline-primary"}
          onClick={() => setShowWeekends((prev) => !prev)}
        >
          {t("form.showWeekend")}
        </Button>
        <Button
          variant="danger"
          id="clear-button"
          onClick={() => setSelectedHours({})}
        >
          {t("form.clear")}
        </Button>
      </ButtonGroup>
    </div>
  );
};

export default ScheduleSelector;

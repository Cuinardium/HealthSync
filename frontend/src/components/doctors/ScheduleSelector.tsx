import React, { useState, useRef, useEffect } from "react";
import {
  Button,
  Container,
  Form,
} from "react-bootstrap";
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

  const [showWeekends, setShowWeekends] = useState(false);
  const [minAttendingHour, setMinAttendingHour] = useState(16);
  const [maxAttendingHour, setMaxAttendingHour] = useState(35);

  useEffect(() => {
    if (attendingHours) {
      const newHours: { [key: string]: Set<number> } = attendingHours.reduce(
        (acc, attendingHour) => {
          acc[attendingHour.day] = new Set(
            attendingHour.hours.map((hour) => getTimeOrder(hour)),
          );
          return acc;
        },
        {} as { [key: string]: Set<number> },
      );
      setSelectedHours(newHours);

      const newAttendingHours: AttendingHours[] = Object.keys(newHours).map(
        (dayKey) => ({
          day: dayKey as Day,
          hours: Array.from(newHours[dayKey]).map((timeIdx) => TIMES[timeIdx]),
        }),
      );

      const isWeekendEmpty = !newAttendingHours.some(
        (attendingHour) =>
          attendingHour.hours.length > 0 &&
          (getDayOrder(attendingHour.day) === 5 ||
            getDayOrder(attendingHour.day) === 6),
      );

      const [min, max] = [
        Math.min(
          ...newAttendingHours.map((attendingHour) =>
            Math.min(...attendingHour.hours.map(getTimeOrder)),
          ),
        ),
        Math.max(
          ...newAttendingHours.map((attendingHour) =>
            Math.max(...attendingHour.hours.map(getTimeOrder)),
          ),
        ),
      ];

      setShowWeekends(!isWeekendEmpty);

      if (min < 16) {
        setMinAttendingHour(min);
      }

      if (max > 35) {
        setMaxAttendingHour(max);
      }

      onScheduleChange(newAttendingHours);
    }
  }, [attendingHours, onScheduleChange]);

  const [isDragging, setIsDragging] = useState(false);
  const [isSelecting, setIsSelecting] = useState(false);
  const scheduleContainerRef = useRef<HTMLDivElement | null>(null);

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
    const newAttendingHours: AttendingHours[] = Object.keys(
      updatedSelectedHours,
    ).map((dayKey) => ({
      day: dayKey as Day,
      hours: Array.from(updatedSelectedHours[dayKey]).map(
        (timeIdx) => TIMES[timeIdx],
      ),
    }));

    DAYS.forEach((day) => {
      if (
        !newAttendingHours.some((attendingHour) => attendingHour.day === day)
      ) {
        newAttendingHours.push({ day, hours: [] });
      }
    });

    onScheduleChange(newAttendingHours);
  };

  const handleClear = () => {
    setSelectedHours({});
    const emptyAttendingHours: AttendingHours[] = DAYS.map((day) => ({
      day,
      hours: [],
    }));
    onScheduleChange(emptyAttendingHours);
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

      <Container className="mt-3 d-flex flex-row justify-content-center align-items-center">
        <Form.Switch
          id="showWeekend"
          checked={showWeekends}
          onChange={() => setShowWeekends(!showWeekends)}
          label={t("changeSchedule.showWeekend")}
          className={"me-3"}
        />
        <Form.Switch
          id="showAllTimes"
          checked={minAttendingHour === 0 && maxAttendingHour === 47}
          onChange={() => {
            if (minAttendingHour === 0 && maxAttendingHour === 47) {
              setMinAttendingHour(16);
              setMaxAttendingHour(35);
            } else {
              setMinAttendingHour(0);
              setMaxAttendingHour(47);
            }
          }}
          label={t("changeSchedule.showAllTimes")}
          className={"me-3"}
        />
        <Button variant="danger" id="clear-button" onClick={handleClear}>
          {t("changeSchedule.clear")}
        </Button>
      </Container>
    </div>
  );
};

export default ScheduleSelector;

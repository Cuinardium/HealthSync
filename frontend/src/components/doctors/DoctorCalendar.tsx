import { Time } from "../../api/time/Time";
import Calendar, { OnArgs, TileDisabledFunc } from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { useTranslation } from "react-i18next";
import { useUser } from "../../context/UserContext";
import { useAvailableHours } from "../../hooks/doctorHooks";
import {formatDate, formatDatePretty, formatDatePrettyLong} from "../../api/util/dateUtils";
import { useState } from "react";
import { Alert, Card, Col, Row } from "react-bootstrap";
import { useDoctorQueryContext } from "../../context/DoctorQueryContext";
import useLocale from "../../hooks/useLocale";

interface DoctorCalendarProps {
  doctorId: string;
  onSelected: (date: Date, time: Time) => void;
  initialDate?: Date;
}

const DoctorCalendar: React.FC<DoctorCalendarProps> = ({
  doctorId,
  onSelected,
  initialDate,
}) => {
  const { user } = useUser();

  const { t } = useTranslation();

  const { query } = useDoctorQueryContext();
  const {locale} = useLocale();

  const [selectedDate, setSelectedDate] = useState<Date>(() => {
    if (initialDate) {
      return initialDate;
    }

    if (query.date) {
      return query.date;
    }

    const date = new Date();
    date.setHours(0, 0, 0, 0);
    return date;
  });

  const [from, setFrom] = useState(() => {
    const from = new Date();
    from.setHours(0, 0, 0, 0);
    return from;
  });
  const [to, setTo] = useState(() => {
    const to = new Date();
    to.setMonth(to.getMonth() + 1);
    return to;
  });

  const { data: availableHours } = useAvailableHours(doctorId, from, to);

  const isDateDisabled: TileDisabledFunc = ({ date, view }) => {
    if (!!selectedDate && formatDate(date) === formatDate(selectedDate)) {
      return false;
    }

    if (!availableHours) {
      return true;
    }

    const dateKey = formatDate(date);

    if (view === "month") {
      return !availableHours[dateKey] || availableHours[dateKey].length === 0;
    }
    return false;
  };

  const onMonthChange = ({ activeStartDate }: OnArgs) => {
    let newFrom = activeStartDate ?? new Date();
    newFrom.setHours(0, 0, 0, 0);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (newFrom < today) {
      newFrom = today;
    }

    // Last day of the month
    let newTo = new Date(newFrom);
    newTo.setMonth(newTo.getMonth() + 1);
    newTo.setDate(newTo.getDate() - 1);
    newTo.setHours(0, 0, 0, 0);

    setFrom(newFrom);
    setTo(newTo);
    setSelectedDate(newFrom);
  };

  return (
    <div className="d-flex flex-row justify-content-center">
      <Calendar
          className="card shadow-sm"
        minDate={new Date()}
        locale={locale}
        tileDisabled={isDateDisabled}
        view="month"
        maxDetail="month"
        showNeighboringMonth={false}
        value={selectedDate}
        onActiveStartDateChange={onMonthChange}
        onClickDay={(date) =>
          setSelectedDate(() => {
            const newDate = date;
            date.setHours(0, 0, 0, 0);
            return newDate;
          })
        }
      />
      <Card className="ms-3 shadow-sm w-50">
        <Card.Body>
          <Card.Title className="text-center">{formatDatePrettyLong(selectedDate, locale)}</Card.Title>
          <Row className="mt-3 d-flex flex-row justify-content-center">
            {availableHours &&
              availableHours[formatDate(selectedDate)]?.map((time) => (
                <Col key={time} xs={4} sm={4} md={4} lg={3} xl={2}>
                  <div
                    onClick={() => onSelected(selectedDate, time as Time)}
                    className="chip badge rounded-pill"
                  >
                    {time}
                  </div>
                </Col>
              ))}
            {(!availableHours ||
              availableHours[formatDate(selectedDate)]?.length === 0) && (
              <Alert variant="info" className="text-center w-75">
                {t("detailedDoctor.noAppointmentsAvailable")}
              </Alert>
            )}
          </Row>
        </Card.Body>
      </Card>
    </div>
  );
};

export default DoctorCalendar;

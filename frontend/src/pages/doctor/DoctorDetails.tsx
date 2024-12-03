import { Link, useLocation, useNavigate, useParams } from "react-router-dom";
import { Breadcrumb, Col, Container } from "react-bootstrap";
import { useDoctor } from "../../hooks/doctorHooks";

import ReviewList from "../../components/reviews/ReviewList";
import React, {useEffect, useState} from "react";
import { useUser } from "../../context/UserContext";
import AppointmentForm from "../../components/appointments/AppointmentForm";
import { useTranslation } from "react-i18next";
import DetailedDoctorCard from "../../components/doctors/DetailedDoctorCard";
import DoctorCalendar from "../../components/doctors/DoctorCalendar";
import { Patient } from "../../api/patient/Patient";
import DetailedDoctorCardPlaceholder from "../../components/doctors/DetailedDoctorCardPlaceholder";

interface LocationState {
  from: Location;
  selectedDate: Date;
  selectedTime: string;
  doctorId: string;
}

const DoctorDetails: React.FC = () => {
  const { id: doctorId } = useParams<{ id: string }>(); // get the doctor id from the URL
  const navigate = useNavigate();
  const { t } = useTranslation();

  const location = useLocation();

  const {
    data: doctor,
    isLoading,
    isError,
    error,
  } = useDoctor(doctorId as string);

  const { user, isDoctor, loading } = useUser();

  const [selectedDate, setSelectedDate] = useState<Date | null>(() => {
    const state = location.state as LocationState;

    if (state && state.selectedDate && state.doctorId === doctorId) {
      return state.selectedDate;
    }

    return null;
  });
  const [selectedTime, setSelectedTime] = useState<string | null>(() => {
    const state = location.state as LocationState;

    if (state && state.selectedTime && state.doctorId === doctorId) {
      return state.selectedTime;
    }

    return null;
  });

  const [showAppointmentForm, setShowAppointmentForm] = useState<boolean>(
    () => {
      const state = location.state as LocationState;

      return !!(
        state &&
        state.selectedDate &&
        state.doctorId === doctorId &&
        user &&
        !isDoctor
      );
    },
  );

  useEffect(() => {
    if (!doctorId || isNaN(+Number(doctorId)) || Number(doctorId) < 0) {
      navigate("/404", { replace: true });
    }
  }, [doctorId, navigate]);

  useEffect(() => {
    if (isError) {
      const status = error?.response?.status;
      if (status === 404) {
        navigate("/404", { replace: true });
      }
    }
  }, [isError, error, navigate]);

  return (
    <Container className="d-flex justify-content-center align-items-center mt-5 mb-5">
      <Col md={9} lg={9}>
        <Breadcrumb>
          <Breadcrumb.Item
            linkAs={Link}
            linkProps={{ to: "/doctor-dashboard" }}
          >
            {t("doctorDashboard.title")}
          </Breadcrumb.Item>
          <Breadcrumb.Item active>{t("detailedDoctor.title")}</Breadcrumb.Item>
        </Breadcrumb>
        <h1>{t("detailedDoctor.title")}</h1>
        {(isLoading || loading || !doctor) ? <DetailedDoctorCardPlaceholder/>  : <DetailedDoctorCard doctor={doctor} />}

        {doctor && doctorId && (
          <>
            {(!user || !isDoctor) && (
              <>
                {selectedDate && selectedTime && (
                  <AppointmentForm
                    doctor={doctor}
                    user={(user as Patient) ?? undefined}
                    date={selectedDate}
                    time={selectedTime}
                    show={showAppointmentForm}
                    onHide={() => setShowAppointmentForm(false)}
                  />
                )}

                <div className="mt-3 mb-3">
                  <h2 className="mb-3">{t("detailedDoctor.availability")}</h2>
                  <DoctorCalendar
                    doctorId={doctorId}
                    userId={user?.id}
                    initialDate={selectedDate ?? undefined}
                    onSelected={(date, time) => {
                      setSelectedDate(date);
                      setSelectedTime(time);

                      if (user && !isDoctor) {
                        setShowAppointmentForm(true);
                      } else if (!user) {
                        navigate("/login", {
                          state: {
                            from: location,
                            selectedDate: date,
                            selectedTime: time,
                            doctorId: doctorId,
                          },
                        });
                      }
                    }}
                  />
                </div>
              </>
            )}

            <ReviewList doctorId={doctorId} canReview={doctor.canReview} />
          </>
        )}
      </Col>
    </Container>
  );
};

export default DoctorDetails;

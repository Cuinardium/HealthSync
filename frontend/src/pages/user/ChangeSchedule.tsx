import { AxiosError } from "axios";
import { t } from "i18next";
import { useState } from "react";
import { Button, Card, Container } from "react-bootstrap";
import { FaCalendar } from "react-icons/fa6";
import { AttendingHours } from "../../api/doctor/Doctor";
import ScheduleSelector from "../../components/doctors/ScheduleSelector";
import { useAuth } from "../../context/AuthContext";
import { useUpdateAttendingHours } from "../../hooks/doctorHooks";

const ChangeSchedule = () => {
  const { id } = useAuth();

  const [attendingHours, setAttendingHours] = useState<AttendingHours[]>();

  // TODO
  const onSuccess = () => {
    alert("Schedule changed successfully");
  };

  const onError = (error: AxiosError) => {
    const body = JSON.stringify(error.response?.data);
    alert(`Error: ${body}`);
  };

  const updateScheduleMutation = useUpdateAttendingHours(
    String(id),
    onSuccess,
    onError,
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (attendingHours) {
      updateScheduleMutation.mutate(attendingHours);
    }
  };

  return (
    <div>
      <Container className="formContainer generalPadding">
        <h1>Change Schedule</h1>
        <Card>
          <div className="profileTitle">
            <strong>{t("profile.schedule")}</strong>
            <FaCalendar />
          </div>
          <div className="scheduleMargin">
            <ScheduleSelector
              doctorId={String(id)}
              onScheduleChange={(newAttendingHours) => {
                setAttendingHours(newAttendingHours);
              }}
            />
          </div>
          <div className="profileButtonContainer">
            <Button
              variant="primary"
              type="submit"
              onClick={handleSubmit}
              disabled={updateScheduleMutation.isPending}
            >
              {updateScheduleMutation.isPending ? "Loading..." : "Save"}
            </Button>
          </div>
        </Card>
      </Container>
    </div>
  );
};

export default ChangeSchedule;

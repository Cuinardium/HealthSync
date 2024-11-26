import { AxiosError } from "axios";
import { t } from "i18next";
import {
  Alert,
  Breadcrumb,
  Button,
  Card,
  Col,
  Container,
  Row,
  Spinner,
} from "react-bootstrap";
import { FaCalendar } from "react-icons/fa6";
import { AttendingHours } from "../../api/doctor/Doctor";
import ScheduleSelector from "../../components/doctors/ScheduleSelector";
import { useAuth } from "../../context/AuthContext";
import { useUpdateAttendingHours } from "../../hooks/doctorHooks";
import { useForm, Controller } from "react-hook-form";
import { Link } from "react-router-dom";
import React, { useState } from "react";

interface FormValues {
  attendingHours: AttendingHours[] | undefined;
}

const ChangeSchedule = () => {
  const { id } = useAuth();
  const { control, handleSubmit, setValue } = useForm<FormValues>({
    defaultValues: {
      attendingHours: [],
    },
  });

  const [showSuccess, setShowSuccess] = useState<boolean>(false);
  const [showError, setShowError] = useState<boolean>(false);
  const [changes, setChanges] = useState<number>(0);

  const onSuccess = () => {
    setShowSuccess(true);
    setChanges(0);
  };

  const onError = (error: AxiosError) => {
    setShowError(true);
  };

  const updateScheduleMutation = useUpdateAttendingHours(
    String(id),
    onSuccess,
    onError,
  );

  const onSubmit = (data: FormValues) => {
    if (data.attendingHours) {
      updateScheduleMutation.mutate(data.attendingHours);
    }
  };

  return (
    <Container className="justify-content-center mt-5">
      <Col md={8} lg={9}>
        <Breadcrumb>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/doctor-profile" }}>
            {t("profile.profile")}
          </Breadcrumb.Item>
          <Breadcrumb.Item active>{t("changeSchedule.title")}</Breadcrumb.Item>
        </Breadcrumb>
        <h1 className="d-flex flex-row align-items-center">
          {t("changeSchedule.title")} <FaCalendar className="ms-3" />
        </h1>
        <Card className="mt-4 mb-5">
          <Card.Body>
            <h5 className={"text-muted text-center mt-3 mb-4"}>
              {t("changeSchedule.description")}
            </h5>
            <Row>
              <div className="scheduleMargin">
                <Controller
                  name="attendingHours"
                  control={control}
                  render={({ field }) => (
                    <ScheduleSelector
                      doctorId={String(id)}
                      onScheduleChange={(newAttendingHours) => {
                        setValue("attendingHours", newAttendingHours);
                        setChanges((prev) => prev + 1);
                      }}
                    />
                  )}
                />
              </div>
            </Row>

            <Row className={"d-flex flex-row justify-content-center"}>
              {showSuccess && (
                <Alert
                  variant="primary"
                  dismissible
                  onClose={() => setShowSuccess(false)}
                  className={"w-50"}
                >
                  {t("changeSchedule.success")}
                </Alert>
              )}
              {showError && (
                <Alert variant="danger" className={"w-50 mt-3"}>
                  {t("changeSchedule.error")}
                </Alert>
              )}
            </Row>
            <Row>
              <Col>
                <Row>
                  <Col className="d-flex flex-row justify-content-end">
                    <Button
                      variant="primary"
                      type="submit"
                      className="submitButton me-4"
                      onClick={handleSubmit(onSubmit)}
                      disabled={updateScheduleMutation.isPending || changes <= 1}
                    >
                      {updateScheduleMutation.isPending ? (
                        <>
                          <Spinner
                            as="span"
                            animation="border"
                            size="sm"
                            role="status"
                            aria-hidden="true"
                          />{" "}
                          {t("changeSchedule.loading")}
                        </>
                      ) : (
                        t("changeSchedule.submit")
                      )}
                    </Button>
                    <Button
                      variant="secondary"
                      as={Link as any}
                      to="/doctor-profile"
                      className="cancelButton me-2"
                    >
                      {t("changeSchedule.cancel")}
                    </Button>
                  </Col>
                </Row>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      </Col>
    </Container>
  );
};

export default ChangeSchedule;

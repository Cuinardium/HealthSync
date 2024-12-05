import React from "react";
import { Card, Placeholder, Stack, Row, Col } from "react-bootstrap";
import {
  FaAddressBook,
  FaCity,
  FaLocationDot,
  FaMapLocationDot,
  FaEnvelope,
} from "react-icons/fa6";

const DetailedDoctorCardPlaceholder: React.FC = () => {
  return (
    <Card className="mb-4 shadow-sm doctor-card">
      <Card.Body>
        <Stack direction="horizontal" className="mb-3">
          <Placeholder as={Col} xs={1} animation="wave">
            <Placeholder
              className="rounded-circle me-3"
              style={{ width: "60px", height: "60px" }}
            />
          </Placeholder>
          <Col xs={6}>
            <Stack direction="vertical" gap={3}>
              <Placeholder as={Row} animation="wave">
                <Placeholder xs={5} />
              </Placeholder>
              <Placeholder as={Row} animation="wave">
                <Placeholder xs={4} className="text-muted" />
              </Placeholder>
            </Stack>
          </Col>
        </Stack>
        <div className="mt-5">
          <div>
            <div className="d-flex flex-row justify-content-between mt-3">
              <div className="me-5 w-50">
                {/* Specialty Placeholder */}
                <Placeholder animation="wave">
                  <h5 className="mb-3">
                    <Placeholder xs={6} />
                  </h5>
                  <Stack direction="horizontal" gap={2} className="mb-2">
                    <Placeholder xs={4} className="text-primary rounded-pill" />
                  </Stack>
                </Placeholder>

                <Placeholder animation="wave">
                  <h5 className="mb-2 mt-3">
                    <Placeholder xs={5} />
                  </h5>
                  <Stack direction="horizontal" gap={2} className="mb-2">
                    {Array.from({ length: 3 }).map((_, index) => (
                      <Col xs={2} key={index}>
                        <Placeholder
                          xs={12}
                          className="text-primary rounded-pill"
                        />
                      </Col>
                    ))}
                  </Stack>
                </Placeholder>
              </div>
              <div className="w-25"></div>
              <div className="w-25 d-flex flex-column me-5">
                <Placeholder animation="wave">
                  <h5 className="d-flex flex-row align-items-center mb-2">
                    <Placeholder xs={4} />
                    <FaMapLocationDot className="ms-1" />
                  </h5>
                </Placeholder>

                <Placeholder animation="wave">
                  <FaLocationDot /> <Placeholder xs={6} />
                </Placeholder>

                <Placeholder animation="wave">
                  <FaCity /> <Placeholder xs={5} />
                </Placeholder>

                <Placeholder animation="wave">
                  <h5 className="d-flex flex-row align-items-center mt-3 mb-2">
                    <Placeholder xs={4} />
                    <FaAddressBook className="ms-1" />
                  </h5>
                </Placeholder>

                <Placeholder animation="wave">
                  <FaEnvelope /> <Placeholder xs={6} />
                </Placeholder>
              </div>
            </div>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
};

export default DetailedDoctorCardPlaceholder;

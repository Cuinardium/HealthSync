import React from "react";
import {Card, Col, Placeholder, Row, Stack} from "react-bootstrap";

const VacationCardPlaceholder: React.FC = () => {
    return (
        <Card>
            <Card.Body>
                <div className="d-flex flex-row justify-content-between align-items-center">
                    <Placeholder as={Col} xs={10} animation="wave">
                        <Placeholder xs={5} />
                    </Placeholder>
                    <Placeholder as={Col} xs={1} animation="wave">
                        <Placeholder.Button variant="danger" xs={12} />
                    </Placeholder>
                </div>
            </Card.Body>
        </Card>
    );
};

export default VacationCardPlaceholder;

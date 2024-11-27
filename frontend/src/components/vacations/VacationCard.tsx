import { Vacation } from "../../api/vacation/Vacation";
import { Button, Card, Stack, Row, Col } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import {FaArrowRight} from "react-icons/fa";

interface VacationCardProps {
    vacation: Vacation;
    onDelete: (vacationId: string) => void;
    selected: boolean;
}

const VacationCard: React.FC<VacationCardProps> = ({ vacation, onDelete, selected }) => {
    const { t } = useTranslation();

    // Format date to dd/mm/yyyy
    const formatDate = (date: Date) => {
        const d = new Date(date);
        const day = String(d.getDate()).padStart(2, "0");
        const month = String(d.getMonth() + 1).padStart(2, "0");
        const year = d.getFullYear();
        return `${day}/${month}/${year}`;
    };

    const fromDate = formatDate(vacation.fromDate);
    const toDate = formatDate(vacation.toDate);

    return (
        <Card className="shadow-sm" border={vacation.isHappening ? "success": "light"}>
            <Card.Body>
                <Row className="align-items-center">
                    <Col md={2} sm={2}>
                        {vacation.isHappening ? (
                                <div className="text-success text-center">{t("vacation.happening")}</div>
                        ) : vacation.isPast ?
                                <div className="text-secondary text-center">{t("vacation.past")}</div>
                            :(
                                <div className="text-primary text-center">{t("vacation.future")}</div>
                        )}

                    </Col>
                    <Col md={2} sm={3}>
                        <h5 className="text-center">
                            {fromDate}
                        </h5>
                        <div className="text-center text-muted"> {vacation.fromTime}</div>
                    </Col>
                    <Col md={1} sm={1}>
                        <FaArrowRight className="mx-2 text-primary" />
                    </Col>
                    <Col md={2} sm={3}>
                        <h5 className="text-center">
                            {toDate}
                        </h5>
                        <div className="text-center text-muted">{vacation.toTime}</div>
                    </Col>
                    <Col md={5} sm={3} className="text-end">
                        <Button variant="danger" onClick={() => onDelete(vacation.id)} disabled={selected}>
                            {t("vacation.cancel")}
                        </Button>
                    </Col>
                </Row>
            </Card.Body>
        </Card>
    );
};

export default VacationCard;

import { Vacation } from "../../api/vacation/Vacation";
import { Button, Card, Stack, Row, Col } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import {FaArrowRight} from "react-icons/fa";

interface VacationCardProps {
    vacation: Vacation;
    onDelete: (vacationId: string) => void;
}

const VacationCard: React.FC<VacationCardProps> = ({ vacation, onDelete }) => {
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
        <Card className="shadow-sm">
            <Card.Body>
                <Row className="align-items-center">
                    <Col md={6}>
                        <div>
                            <strong>{fromDate}</strong> ({vacation.fromTime})
                            <FaArrowRight className="mx-2 text-primary" />
                            <strong>{toDate}</strong> ({vacation.toTime})
                        </div>
                    </Col>
                    <Col md={6} className="text-end">
                        <Button variant="danger" onClick={() => onDelete(vacation.id)}>
                            {t("vacation.cancel")}
                        </Button>
                    </Col>
                </Row>
            </Card.Body>
        </Card>
    );
};

export default VacationCard;

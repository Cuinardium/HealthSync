import { Review } from "../../api/review/Review";
import { usePatient } from "../../hooks/patientHooks";
import { Card } from "react-bootstrap";
import Rating from "../doctors/Rating";
import {formatDate} from "../../api/util/dateUtils";

interface ReviewCardProps {
  review: Review;
}

const ReviewCard: React.FC<ReviewCardProps> = ({ review }) => {
  return (
    <Card>
      <Card.Body>
        <Card.Text>{review.description}</Card.Text>

        <Rating rating={review.rating} />

        <Card.Subtitle className="mt-2">
          {formatDate(review.date)}
        </Card.Subtitle>
      </Card.Body>
    </Card>
  );
};

export default ReviewCard;

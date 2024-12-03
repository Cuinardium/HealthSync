import { Review } from "../../api/review/Review";
import { Card, Image } from "react-bootstrap";
import Rating from "../doctors/Rating";
import {formatDatePretty} from "../../api/util/dateUtils";

import patientDefault from "../../img/patientDefault.png";

interface ReviewCardProps {
  review: Review;
}

const ReviewCard: React.FC<ReviewCardProps> = ({ review }) => {
  return (
    <Card>
      <Card.Body>
        <Rating rating={review.rating} />

        <div className="mb-3 mt-3">{review.description}</div>

        <div className="d-flex align-items-center mt-3">
          <Image
            src={review.patientImage ? review.patientImage : patientDefault}
            width="50"
            height="50"
            className="rounded-circle me-3"
            alt={review.patientName}
          />
          <div>
            <Card.Subtitle>{review.patientName}</Card.Subtitle>
            <Card.Subtitle className="text-muted mt-2">
              {formatDatePretty(review.date)}
            </Card.Subtitle>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
};

export default ReviewCard;

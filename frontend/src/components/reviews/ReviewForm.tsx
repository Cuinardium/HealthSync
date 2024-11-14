import { AxiosError } from "axios";
import { useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import { ReviewForm as ReviewFormType } from "../../api/review/Review";
import { useCreateReview } from "../../hooks/reviewHooks";

interface ReviewFormProps {
  doctorId: string;
}

const ReviewForm: React.FC<ReviewFormProps> = ({ doctorId }) => {

  const [newReview, setNewReview] = useState<ReviewFormType>({
    description: "",
    rating: 1,
  })

  // TODO
  const onSuccess = () => {
    alert("Review created succesfully")
  }
  
  const onError = (error: AxiosError) => {
    alert(`Failed to create review: ${error.message}`)
  }

  const createReviewMutation = useCreateReview(doctorId, onSuccess, onError)

  const handleCreateReview = () => {
    createReviewMutation.mutate(newReview)
  }


  return (
    <Form
      onSubmit={(e) => {
        e.preventDefault();
        handleCreateReview();
      }}
    >
      <Row className="mb-3">
        <Form.Group as={Col} controlId="description">
          <Form.Label>Description</Form.Label>
          <Form.Control
            type="textarea"
            value={newReview.description}
            onChange={(e) =>
              setNewReview((prev) => ({
                ...prev,
                description: e.target.value,
              }))
            }
          />
        </Form.Group>
        <Form.Group as={Col} controlId="rating">
          <Form.Label>Rating</Form.Label>
          <Form.Control
            type="number"
            value={newReview.rating}
            onChange={(e) =>
              setNewReview((prev) => ({
                ...prev,
                rating: Number(e.target.value)
              }))
            }
          />
        </Form.Group>
      </Row>

      <Button
        variant="primary"
        type="submit"
        disabled={createReviewMutation.isPending}
      >
        {createReviewMutation.isPending ? "Creating..." : "Create Review"}
      </Button>
    </Form>
  );
};

export default ReviewForm;

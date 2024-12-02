import { AxiosError } from "axios";
import React, { useState } from "react";
import {Alert, Button, Col, Form, Modal, Row, Spinner, Stack} from "react-bootstrap";
import { ReviewForm as ReviewFormType } from "../../api/review/Review";
import { useCreateReview } from "../../hooks/reviewHooks";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import {
  validateReviewDescription,
  validateReviewRating,
} from "../../api/validation/validations";
import { useTranslation } from "react-i18next";
import ClickableRating from "../doctors/ClickableRating";

interface ReviewFormProps {
  doctorId: string;
  showReviewForm: boolean;
  onHide: () => void;
}

const ReviewForm: React.FC<ReviewFormProps> = ({
  doctorId,
  showReviewForm,
  onHide,
}) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    control,
    setError,
    reset,
  } = useForm<ReviewFormType>();

  const { t } = useTranslation();

  const [showSuccess, setShowSuccess] = useState<boolean>(false);

  const onSuccess = () => {
    setShowSuccess(true);
    // Call onHide after 2 seconds
    setTimeout(() => {
      handleHide();
    }, 1000);
  };

  const onError = (error: AxiosError) => {
    setError("root", {
        message: "review.error",
    })
    // Call onHide after 2 seconds
    setTimeout(() => {
      handleHide();
    }, 1000);
  };

  const createReviewMutation = useCreateReview(doctorId, onSuccess, onError);

  const handleCreateReview: SubmitHandler<ReviewFormType> = (
    data: ReviewFormType,
  ) => {
    createReviewMutation.mutate(data);
  };

  const handleHide = () => {
    reset();
    setValue("rating", 0);
    setShowSuccess(false);
    onHide();
  };

  return (
    <div>
      <Modal show={showReviewForm} onHide={handleHide}>
        <Modal.Header closeButton>
          <Modal.Title>{t("review.title")}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleSubmit(handleCreateReview)}>
            <Row className="mb-3">
              <Form.Group as={Col} controlId="description">
                <Form.Label>{t("review.description.title")}</Form.Label>
                <Form.Control
                  as="textarea"
                  cols={5}
                  {...register("description", {
                    validate: validateReviewDescription,
                  })}
                  placeholder={t("review.description.placeholder")}
                  isInvalid={!!errors.description}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.description && t(errors.description?.message ?? "")}
                </Form.Control.Feedback>
              </Form.Group>
            </Row>

            <Row className="mb-3">
              <Form.Group as={Col} controlId="rating">
                <Form.Label>{t("review.rating")}</Form.Label>
                <Controller
                  render={({ field, fieldState: { error } }) => (
                    <>
                      <ClickableRating
                        initialRating={field.value}
                        onClick={(rating) => field.onChange(rating)}
                        showClear={false}
                      />

                      {error && (
                        <Form.Text className="text-danger">
                          {t(error.message || "Invalid rating")}
                        </Form.Text>
                      )}
                    </>
                  )}
                  control={control}
                  name={"rating"}
                  rules={{ validate: validateReviewRating }}
                />
              </Form.Group>
            </Row>
            {errors.root && (
                <Alert variant="danger" className="mb-3">
                  {t(errors.root?.message ?? "")}
                </Alert>
            )}
            {showSuccess && (
                <Alert variant="primary" dismissible className="mb-3">
                  {t("review.modal.text")}
                </Alert>
            )}

            <Stack direction="horizontal">
              <Button
                className="ms-auto"
                variant="primary"
                type="submit"
                disabled={createReviewMutation.isPending}
              >
                {createReviewMutation.isPending ? (
                  <>
                    <Spinner
                      as="span"
                      animation="border"
                      size="sm"
                      role="status"
                      aria-hidden="true"
                    />{" "}
                    {t("review.submitting")}
                  </>
                ) : (
                  t("review.submit")
                )}
              </Button>
            </Stack>
          </Form>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default ReviewForm;

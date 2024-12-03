import React, { useState } from "react";
import { useReviews } from "../../hooks/reviewHooks";
import { useTranslation } from "react-i18next";
import { Alert, Button, Spinner, Stack } from "react-bootstrap";
import ReviewCard from "./ReviewCard";
import ReviewForm from "./ReviewForm";
import ReviewCardPlaceholder from "./ReviewCardPlaceholder";

interface ReviewListProps {
  doctorId: string;
  canReview: boolean;
}

const ReviewList: React.FC<ReviewListProps> = ({ doctorId, canReview }) => {
  const { t } = useTranslation();

  const pageSize = 10;

  const [showReviewForm, setShowReviewForm] = useState<boolean>(false);

  const {
    data: reviewPage,
    isLoading,
    isError,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useReviews(doctorId, pageSize);

  if (isError) {
    return <Alert variant="danger">{t("review.error")}</Alert>;
  }

  if (
    reviewPage &&
    (reviewPage.pages.length === 0 || reviewPage.pages[0].content.length === 0)
  ) {
    return (
      <div>
        <Stack direction="horizontal" className="mt-3">
          <h5 className="text-muted">{t("review.noReviews")}</h5>
          {canReview && (
            <Button
              variant="outline-primary"
              className="ms-auto"
              onClick={() => setShowReviewForm(true)}
            >
              {t("detailedDoctor.firstToReview")}
            </Button>
          )}
        </Stack>
        <ReviewForm
          doctorId={doctorId}
          showReviewForm={showReviewForm}
          onHide={() => setShowReviewForm(false)}
        />
      </div>
    );
  }

  const reviews = reviewPage?.pages.flatMap((page) => page.content) ?? [];

  return (
    <div>
      <Stack direction="horizontal" className="mb-3">
        <h2>{t("detailedDoctor.reviews")}</h2>
        {canReview && (
          <Button
            variant="outline-primary"
            className="ms-auto"
            onClick={() => setShowReviewForm(true)}
          >
            {t("detailedDoctor.review")}
          </Button>
        )}
      </Stack>

      <ReviewForm
        doctorId={doctorId}
        showReviewForm={showReviewForm}
        onHide={() => setShowReviewForm(false)}
      />

      {isLoading && (
        <Stack direction="vertical" gap={3}>
          {[...Array(pageSize)].map((_, index) => (
            <ReviewCardPlaceholder key={index} />
          ))}
        </Stack>
      )}

      <Stack direction="vertical" gap={3}>
        {reviews.map((review) => (
          <ReviewCard review={review} key={review.id} />
        ))}
      </Stack>

      {/* Load more */}
      <div style={{ marginTop: "20px", textAlign: "center" }}>
        {hasNextPage && (
          <Button
            variant="outline-primary"
            onClick={() => fetchNextPage()}
            disabled={isFetchingNextPage}
          >
            {isFetchingNextPage ? (
              <>
                <Spinner
                  as="span"
                  animation="border"
                  size="sm"
                  role="status"
                  aria-hidden="true"
                />{" "}
                {t("review.loadingMore")}
              </>
            ) : (
              t("review.loadMore")
            )}
          </Button>
        )}
      </div>
    </div>
  );
};

export default ReviewList;

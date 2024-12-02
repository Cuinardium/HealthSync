import React from "react";
import Loader from "../Loader";
import { ReviewQuery } from "../../api/review/Review";
import { useReviews } from "../../hooks/reviewHooks";
import { useTranslation } from "react-i18next";
import { Alert, Button, Spinner, Stack } from "react-bootstrap";
import ReviewCard from "./ReviewCard";

interface ReviewListProps {
  doctorId: string;
  canReview: boolean;
}

const ReviewList: React.FC<ReviewListProps> = ({ doctorId, canReview }) => {
  const { t } = useTranslation();

  const pageSize = 2;

  const {
    data: reviewPage,
    isLoading,
    isError,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useReviews(doctorId, pageSize);

  if (isLoading) {
    // TODO
    return <Loader />;
  }

  if (isError) {
    return <Alert variant="danger">{t("review.error")}</Alert>;
  }

  if (
    !reviewPage ||
    reviewPage.pages.length === 0 ||
    reviewPage.pages[0].content.length === 0
  ) {
    return (
        <Stack direction="horizontal">
            <h5 className="text-muted">{t("review.noReviews")}</h5>
            {canReview && (
                <Button variant="outline-primary" className="ms-auto">
                    {t("detailedDoctor.firstToReview")}
                </Button>
            )}
        </Stack>
    );
  }

  const reviews = reviewPage.pages.flatMap((page) => page.content) ?? [];

  // TODO: Hacerlo bien
  return (
    <div>
      <Stack direction="horizontal">
        <h2>{t("detailedDoctor.reviews")}</h2>
        {canReview && (
          <Button variant="outline-primary" className="ms-auto">
            {t("detailedDoctor.review")}
          </Button>
        )}
      </Stack>

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

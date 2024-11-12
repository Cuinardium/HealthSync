import React from "react";
import Loader from "./Loader";
import { ReviewQuery } from "../api/review/Review";
import { useReviews } from "../hooks/reviewHooks";

interface ReviewListProps {
  doctorId: string;
  page?: number;
  pageSize?: number;
  onPageChange: (page: number) => void;
}

const ReviewList: React.FC<ReviewListProps> = ({
  doctorId,
  page = 1,
  pageSize = 10,
  onPageChange,
}) => {
  const query: ReviewQuery = {
    page,
    pageSize,
  };

  const {
    data: reviewPage,
    isLoading,
    isError,
    error,
  } = useReviews(doctorId, query);

  if (isLoading) {
    // TODO
    return <Loader />;
  }

  if (isError) {
    // TODO
    return <div>Error fetching Reviews: {error?.message}</div>;
  }

  if (!reviewPage || reviewPage.content.length === 0) {
    // TODO
    return <div>No reviews found</div>;
  }

  // TODO: Hacerlo bien
  return (
    <div>
      <h2>Reviews</h2>
      <ul>
        {reviewPage.content.map((review) => (
          <li key={review.id}>
            <div>
              <strong>Date:</strong> {review.date.toLocaleDateString()}
            </div>
            <div>
              <strong>Rating:</strong> {review.rating} / 5
            </div>
            <div>
              <strong>Description:</strong> {review.description}
            </div>
            <div>
              <strong>Patient:</strong> {review.patient}
            </div>
          </li>
        ))}
      </ul>

      {/* Pagination Controls */}
      <div style={{ marginTop: "20px" }}>
        <button disabled={page === 1} onClick={() => onPageChange(page - 1)}>
          Previous
        </button>
        <span style={{ margin: "0 10px" }}>Page {page}</span>
        <button
          onClick={() => onPageChange(page + 1)}
          disabled={reviewPage.currentPage === reviewPage.totalPages}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default ReviewList;

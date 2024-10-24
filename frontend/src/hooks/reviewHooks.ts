import { useMutation, useQuery } from "@tanstack/react-query";
import { getReview, getReviews, createReview } from "../api/review/reviewApi";
import { Review, ReviewForm, ReviewQuery } from "../api/review/Review";
import { queryClient } from "../api/queryClient";
import { Page } from "../api/page/Page";

const STALE_TIME = 5 * 60 * 1000;

// ========== useReviews ==========

export function useReviews(doctorId: string, reviewQuery: ReviewQuery) {
  return useQuery<Page<Review>, Error>(
    {
      queryKey: ["reviews", doctorId, reviewQuery],
      queryFn: () => getReviews(doctorId, reviewQuery),
      enabled: !!doctorId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useReview ==========

export function useReview(doctorId: string, reviewId: string) {
  return useQuery<Review, Error>(
    {
      queryKey: ["review", doctorId, reviewId],
      queryFn: () => getReview(doctorId, reviewId),
      enabled: !!doctorId && !!reviewId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useCreateReview ==========

export function useCreateReview(doctorId: string) {
  return useMutation<Review, Error, ReviewForm>(
    {
      mutationFn: (review: ReviewForm) => createReview(doctorId, review),
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["reviews", doctorId] });
      },
    },
    queryClient,
  );
}

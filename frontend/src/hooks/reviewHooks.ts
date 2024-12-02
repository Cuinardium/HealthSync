import { useInfiniteQuery, useMutation, useQuery } from "@tanstack/react-query";
import { getReview, getReviews, createReview } from "../api/review/reviewApi";
import { Review, ReviewForm } from "../api/review/Review";
import { queryClient } from "../api/queryClient";
import { AxiosError } from "axios";

const STALE_TIME = 5 * 60 * 1000;

// ========== useReviews ==========

export function useReviews(doctorId: string, pageSize: number) {
  return useInfiniteQuery(
    {
      queryKey: ["reviews", doctorId],
      queryFn: ({ pageParam = 1 }) =>
        getReviews(doctorId, { pageSize, page: pageParam }),
      enabled: !!doctorId,
      staleTime: STALE_TIME,
      getNextPageParam: (lastPage) =>
        lastPage.currentPage < lastPage.totalPages
          ? lastPage.currentPage + 1
          : undefined,
      initialPageParam: 1,
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

export function useCreateReview(
  doctorId: string,
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<Review, AxiosError, ReviewForm>(
    {
      mutationFn: (review: ReviewForm) => createReview(doctorId, review),
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["reviews", doctorId] });
        queryClient.invalidateQueries({ queryKey: ["doctor", doctorId] });
        onSuccess();
      },
      onError: (error) => {
        onError(error);
      },
    },
    queryClient,
  );
}

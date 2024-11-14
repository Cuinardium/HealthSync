import { axios } from "../axios";
import { getPage, Page } from "../page/Page";
import { Review, ReviewForm, ReviewQuery, ReviewResponse } from "./Review";

const REVIEW_ENDPOINT = (doctor_id: string) => `/doctors/${doctor_id}/reviews`;

const REVIEW_CONTENT_TYPE = "application/vnd.review.v1+json";
const REVIEW_LIST_CONTENT_TYPE = "application/vnd.reviews-list.v1+json";

// =========== reviews ==============

export async function getReviews(
  doctorId: string,
  reviewQuery: ReviewQuery,
): Promise<Page<Review>> {
  const response = await axios.get(REVIEW_ENDPOINT(doctorId), {
    params: reviewQuery,
    headers: {
      Accept: REVIEW_LIST_CONTENT_TYPE,
    },
  });

  if (response.status == 200) {
    // Set date to Date object
    response.data = response.data?.map((review: ReviewResponse) => mapDetails(review));
  }

  return getPage(response);
}

export async function createReview(
  doctorId: string,
  review: ReviewForm,
): Promise<Review> {
  const response = await axios.post<Review>(REVIEW_ENDPOINT(doctorId), review, {
    headers: {
      "Content-Type": REVIEW_CONTENT_TYPE,
    },
  });

  const location = response.headers.location;
  const reviewId = location?.split("/").pop();

  return await getReview(doctorId, reviewId as string);
}

// =========== reviews/{id} =======

export async function getReview(doctorId: string, id: string): Promise<Review> {
  const response = await axios.get<ReviewResponse>(
    `${REVIEW_ENDPOINT(doctorId)}/${id}`,
    {
      headers: {
        Accept: REVIEW_CONTENT_TYPE,
      },
    },
  );

  return mapDetails(response.data);
}

// ========== auxiliary functions ==============

function mapDetails(review: ReviewResponse): Review {
  const date = new Date(review.date);
  const patientId = review.links.find((link) => link.rel === "patient")?.href.split("/").pop() as string;

  return {
    ...review,
    date,
    patientId,
  };
}

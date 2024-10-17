import { axios } from "../axios";
import { Review, ReviewForm, ReviewQuery } from "./Review";

const REVIEW_ENDPOINT = (doctor_id: string) =>
  `/doctors/${doctor_id}/reviews`;

const REVIEW_CONTENT_TYPE = "application/vnd.review.v1+json";
const REVIEW_LIST_CONTENT_TYPE = "application/vnd.reviews-list.v1+json";

// =========== reviews ==============

export async function getReviews(doctorId: string, reviewQuery: ReviewQuery): Promise<Review[]> {
  const response = await axios.get<Review[]>(
    REVIEW_ENDPOINT(doctorId),
    {
      params: reviewQuery,
      headers: {
        Accept: REVIEW_LIST_CONTENT_TYPE,
      },
    },
  );

  // Set date to Date object
  return response.data.map((review) => mapDates(review));
}

export async function createReview(
  doctorId: string,
  review: ReviewForm,
): Promise<Review> {
  const response = await axios.post<Review>(
    REVIEW_ENDPOINT(doctorId),
    review,
    {
      headers: {
        "Content-Type": REVIEW_CONTENT_TYPE,
      },
    },
  );

  const location = response.headers.location;
  const reviewId = location?.split("/").pop();

  return await getReview(doctorId, reviewId as string);
}

// =========== reviews/{id} =======

export async function getReview(doctorId: string, id: string): Promise<Review> {
  const response = await axios.get<Review>(
    `${REVIEW_ENDPOINT(doctorId)}/${id}`,
    {
      headers: {
        Accept: REVIEW_CONTENT_TYPE,
      },
    },
  );

  return mapDates(response.data);
}

// ========== auxiliary functions ==============

function mapDates(review: Review): Review {
  review.date = new Date(review.date);
  return review;
}

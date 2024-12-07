import { axios } from "../../api/axios";
import { ReviewForm, ReviewQuery } from "../../api/review/Review";
import {
  createReview,
  getReview,
  getReviews,
} from "../../api/review/reviewApi";
import { parseLocalDate } from "../../api/util/dateUtils";

jest.mock("../../api/axios");

const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("Review API Tests", () => {
  const doctorId = "123";
  const reviewId = "456";

  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("should retrieve reviews for a doctor", async () => {
    const reviewQuery: ReviewQuery = { page: 1, pageSize: 10 };
    const mockResponse = {
      status: 200,
      data: [
        {
          id: 1,
          date: "2024-12-07",
          description: "Great doctor!",
          rating: 5,
          patientName: "Alice",
          links: [{ rel: "patient-image", href: "/images/1", method: "GET" }],
        },
      ],
      headers: { link: 'rel="last" page=1' },
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const reviewsPage = await getReviews(doctorId, reviewQuery);

    expect(mockedAxios.get).toHaveBeenCalledWith(
      `/doctors/${doctorId}/reviews`,
      {
        params: reviewQuery,
        headers: { Accept: "application/vnd.reviews-list.v1+json" },
      },
    );

    expect(reviewsPage.content).toHaveLength(1);
    expect(reviewsPage.content[0].description).toBe("Great doctor!");
    expect(reviewsPage.content[0].rating).toBe(5);

    const expectedDate = parseLocalDate("2024-12-07");
    expect(reviewsPage.content[0].date).toStrictEqual(expectedDate);

    expect(reviewsPage.content[0].patientImage).toBe("/images/1");
  });

  it("should create a new review for a doctor", async () => {
    const reviewForm: ReviewForm = {
      description: "Amazing service!",
      rating: 5,
    };
    const mockPostResponse = {
      headers: { location: `/doctors/${doctorId}/reviews/${reviewId}` },
    };
    const mockGetResponse = {
      data: {
        id: 456,
        date: "2024-12-07",
        description: "Amazing service!",
        rating: 5,
        patientName: "Bob",
        links: [],
      },
    };

    mockedAxios.post.mockResolvedValue(mockPostResponse);
    mockedAxios.get.mockResolvedValue(mockGetResponse);

    const review = await createReview(doctorId, reviewForm);

    expect(mockedAxios.post).toHaveBeenCalledWith(
      `/doctors/${doctorId}/reviews`,
      reviewForm,
      { headers: { "Content-Type": "application/vnd.review.v1+json" } },
    );

    expect(mockedAxios.get).toHaveBeenCalledWith(
      `/doctors/${doctorId}/reviews/${reviewId}`,
      { headers: { Accept: "application/vnd.review.v1+json" } },
    );

    expect(review.description).toBe("Amazing service!");
    expect(review.rating).toBe(5);
  });

  it("should retrieve a single review by ID", async () => {
    const mockResponse = {
      data: {
        id: 456,
        date: "2024-12-07",
        description: "Excellent care!",
        rating: 5,
        patientName: "Charlie",
        links: [{ rel: "patient-image", href: "/images/1", method: "GET" }],
      },
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const review = await getReview(doctorId, reviewId);

    expect(mockedAxios.get).toHaveBeenCalledWith(
      `/doctors/${doctorId}/reviews/${reviewId}`,
      { headers: { Accept: "application/vnd.review.v1+json" } },
    );

    expect(review.description).toBe("Excellent care!");
    expect(review.rating).toBe(5);
    expect(review.date).toStrictEqual(parseLocalDate("2024-12-07"));
    expect(review.patientName).toBe("Charlie");
    expect(review.patientImage).toBe("/images/1");
  });
});

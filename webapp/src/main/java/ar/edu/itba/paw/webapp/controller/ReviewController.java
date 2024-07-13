package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ReviewService;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.ReviewForbiddenException;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.webapp.auth.PawAuthUserDetails;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import ar.edu.itba.paw.webapp.mediaType.VndType;
import ar.edu.itba.paw.webapp.query.PageQuery;
import ar.edu.itba.paw.webapp.utils.ResponseUtil;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/doctors/{doctorId:\\d+}/reviews")
@Component
public class ReviewController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

  private final ReviewService reviewService;
  @Context private UriInfo uriInfo;

  @Autowired
  public ReviewController(final ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  // ================= reviews =================

  @GET
  @Produces(VndType.APPLICATION_REVIEWS_LIST)
  public Response listReviews(
      @PathParam("doctorId") final Long doctorId, @Valid @BeanParam final PageQuery pageQuery)
      throws DoctorNotFoundException {

    LOGGER.debug(
        "Listing reviews for doctor: \nDoctorId: {}\nPage: {}", doctorId, pageQuery.getPage());

    Page<Review> reviews =
        reviewService.getReviewsForDoctor(doctorId, pageQuery.getPage(), pageQuery.getPageSize());

    if (reviews.getContent().isEmpty()) {
      LOGGER.debug("No reviews found for doctor: {}", doctorId);
      return Response.noContent().build();
    }

    final List<ReviewDto> reviewDtoList =
        reviews.getContent().stream()
            .map(r -> ReviewDto.fromReview(uriInfo, r))
            .collect(java.util.stream.Collectors.toList());

    return ResponseUtil.setPaginationLinks(
            Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtoList) {}), uriInfo, reviews)
        .build();
  }

  @POST
  @Consumes(VndType.APPLICATION_REVIEW)
  public Response createReview(
      @PathParam("doctorId") final Long doctorId, @Valid final ReviewForm reviewForm)
      throws DoctorNotFoundException, PatientNotFoundException, ReviewForbiddenException {

    final int rating = reviewForm.getRating();
    final String description = reviewForm.getDescription();

    final long patientId = PawAuthUserDetails.getCurrentUserId();

    Review review = reviewService.createReview(doctorId, patientId, rating, description);

    URI createdReviewUri =
        uriInfo
            .getBaseUriBuilder()
            .path("doctors")
            .path(doctorId.toString())
            .path("reviews")
            .path(review.getId().toString())
            .build();

    return Response.created(createdReviewUri).build();
  }

  // ================= reviews/{reviewId} =================

  @GET
  @Path("/{reviewId:\\d+}")
  @Produces(VndType.APPLICATION_REVIEW)
  public Response getReview(
      @PathParam("doctorId") final Long doctorId, @PathParam("reviewId") final Long reviewId)
      throws ReviewNotFoundException {

    LOGGER.debug("Getting review: {}", reviewId);

    final Review review =
        reviewService.getReview(reviewId).orElseThrow(ReviewNotFoundException::new);

    if (!review.getDoctor().getId().equals(doctorId)) {
      throw new ReviewNotFoundException();
    }

    final ReviewDto reviewDto = ReviewDto.fromReview(uriInfo, review);
    return Response.ok(reviewDto).build();
  }
}

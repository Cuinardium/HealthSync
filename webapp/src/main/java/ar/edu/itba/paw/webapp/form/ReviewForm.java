package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReviewForm {

  @Size(min = 1, max = 1000, message = "Size.reviewForm.description")
  @NotNull(message = "NotNull.reviewForm.description")
  private String description;

  @Min(value = 1, message = "Min.reviewForm.rating")
  @Max(value = 5, message = "Max.reviewForm.rating")
  @NotNull(message = "NotNull.reviewForm.rating")
  private Short rating;

  public short getRating() {
    return rating;
  }

  public void setRating(short rating) {
    this.rating = rating;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}

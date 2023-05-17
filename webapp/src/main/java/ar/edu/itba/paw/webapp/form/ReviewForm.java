package ar.edu.itba.paw.webapp.form;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


public class ReviewForm {

  @Size(min = 1, max = 1000)
  private String description;

  @Min(1)
  @Max(5)
  private short rating;

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

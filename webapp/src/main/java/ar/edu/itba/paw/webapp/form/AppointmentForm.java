package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.DateAnnotation;
import javax.validation.constraints.Size;

@DateAnnotation
public class AppointmentForm {
  private String date;

  @Size(min = 1, max = 100)
  private String description;

  private String docEmail;
  private String address;
  private String city;
  private int docId;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDocEmail() {
    return docEmail;
  }

  public void setDocEmail(String docEmail) {
    this.docEmail = docEmail;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public int getDocId() {
    return docId;
  }

  public void setDocId(int docId) {
    this.docId = docId;
  }
}

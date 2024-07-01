package ar.edu.itba.paw.webapp.responses;

import org.springframework.http.HttpStatus;

public class AuthErrorResponse {
  private HttpStatus httpStatus;
  private String message;

  public AuthErrorResponse(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }

  public AuthErrorResponse() {}

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

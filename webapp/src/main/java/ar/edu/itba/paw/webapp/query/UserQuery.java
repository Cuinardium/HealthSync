package ar.edu.itba.paw.webapp.query;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;

public class UserQuery {

  @QueryParam("userId")
  @Min(value = 0, message = "Min.userQuery.userId")
  @NotNull(message = "NotNull.userQuery.userId")
  private Long userId;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "UserQuery [userId=" + userId + "]";
  }
}

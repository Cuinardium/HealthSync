package ar.edu.itba.paw.webapp.query;

import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;
import ar.edu.itba.paw.webapp.utils.DateUtil;
import java.time.LocalDate;
import javax.validation.constraints.Pattern;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class AppointmentQuery {

  @QueryParam("status")
  @ExistsInEnumString(
      enumClass = AppointmentStatus.class,
      message = "ExistsInEnumString.AppointmentQuery.status")
  private String status;

  @Pattern(regexp = DateUtil.DATE_REGEX, message = "Pattern.query.date")
  @QueryParam("date")
  private String date;

  @Pattern(regexp = "^(asc|desc)$", message = "Pattern.query.order")
  @DefaultValue("asc")
  @QueryParam("order")
  private String order;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public AppointmentStatus getAppointmentStatus() {
    if (status == null) {
      return null;
    }
    return AppointmentStatus.valueOf(status);
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public LocalDate getLocalDate() {
    return DateUtil.parseDate(date);
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public boolean sortAsc() {
    return order.equals("asc");
  }

  @Override
  public String toString() {
    return "AppointmentQuery [status=" + status + "]";
  }
}

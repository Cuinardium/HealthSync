package ar.edu.itba.paw.webapp.query;

import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;
import ar.edu.itba.paw.webapp.annotations.ValidRange;
import ar.edu.itba.paw.webapp.utils.DateUtil;
import java.time.LocalDate;
import javax.validation.constraints.Pattern;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@ValidRange(message = "ValidRange.appointmentQuery")
public class AppointmentQuery {

  @QueryParam("status")
  @ExistsInEnumString(
      enumClass = AppointmentStatus.class,
      message = "ExistsInEnumString.AppointmentQuery.status")
  private String status;

  @Pattern(regexp = DateUtil.DATE_REGEX, message = "Pattern.query.date")
  @QueryParam("from")
  private String from;

  @Pattern(regexp = DateUtil.DATE_REGEX, message = "Pattern.query.date")
  @QueryParam("to")
  private String to;

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

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public LocalDate getFromDate() {
    return DateUtil.parseDate(from);
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public LocalDate getToDate() {
    return DateUtil.parseDate(to);
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
    return "{" + "from='" + getFromDate() + '\'' + ", to='" + getToDate() + '\'' + '}';
  }
}

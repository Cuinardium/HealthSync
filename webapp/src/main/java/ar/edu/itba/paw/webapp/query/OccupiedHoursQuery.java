package ar.edu.itba.paw.webapp.query;

import ar.edu.itba.paw.webapp.annotations.FutureOrPresent;
import ar.edu.itba.paw.webapp.annotations.ValidRange;
import ar.edu.itba.paw.webapp.utils.DateUtil;
import java.time.LocalDate;
import javax.validation.constraints.Pattern;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@ValidRange(message = "ValidRange.occupiedHoursQuery")
public class OccupiedHoursQuery extends PageQuery {

  @FutureOrPresent(message = "FutureOrPresent.occupiedHoursQuery.from")
  @Pattern(
      regexp = DateUtil.DATE_REGEX + '|' + DateUtil.TODAY,
      message = "Pattern.occupiedHoursQuery.from")
  @DefaultValue(DateUtil.TODAY)
  @QueryParam("from")
  private String from;

  @Pattern(
      regexp = DateUtil.DATE_REGEX + '|' + DateUtil.MAX_DATE,
      message = "Pattern.occupiedHoursQuery.to")
  @DefaultValue(DateUtil.MAX_DATE)
  @QueryParam("to")
  private String to;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public LocalDate getFromDate() {
    return DateUtil.parseDate(from);
  }

  public void setFromDate(LocalDate fromDate) {
    this.from = fromDate.toString();
  }

  public LocalDate getToDate() {
    return DateUtil.parseDate(to);
  }

  public void setToDate(LocalDate toDate) {
    this.to = toDate.toString();
  }

  @Override
  public String toString() {
    return "{" + "from='" + getFromDate() + '\'' + ", to='" + getToDate() + '\'' + '}';
  }
}

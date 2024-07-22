package ar.edu.itba.paw.webapp.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateUtil {

  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
  public static final String TODAY = "today";
  public static final String MAX_DATE = "max";

  private DateUtil() {
    throw new RuntimeException();
  }

  public static LocalDate parseDate(String date) {

    if (date == null) {
      return null;
    }

    if (date.equals(TODAY)) {
      return LocalDate.now();
    }

    if (date.equals(MAX_DATE)) {
      return LocalDate.now().plusMonths(6);
    }

    LocalDate parsedDate;

    try {
      parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    } catch (DateTimeParseException e) {
      parsedDate = null;
    }

    return parsedDate;
  }

  public static int daysBetween(LocalDate from, LocalDate to) {
    return (int) ChronoUnit.DAYS.between(from, to);
  }
}

package ar.edu.itba.paw.webapp.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class LocaleUtil {

  private static final List<Locale> AVAILABLE_LOCALES =
      Arrays.asList(Locale.forLanguageTag("en"), Locale.forLanguageTag("es"));

  private LocaleUtil() {
    throw new RuntimeException();
  }

  public static Locale getCurrentRequestLocale() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    if (request == null) {
      return Locale.getDefault();
    }

    return request.getLocale();
  }

  public static List<Locale> getAvailableLocales() {
    return AVAILABLE_LOCALES;
  }
}

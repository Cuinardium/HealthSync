package ar.edu.itba.paw.webapp.utils;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class LocaleUtil {

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
}

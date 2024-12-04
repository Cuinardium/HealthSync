package ar.edu.itba.paw.webapp.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class LocaleUtil {

  private static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("en");

  private static final List<Locale> AVAILABLE_LOCALES =
      Arrays.asList(Locale.forLanguageTag("en"), Locale.forLanguageTag("es"));

  private LocaleUtil() {
    throw new RuntimeException();
  }

  public static Locale getCurrentRequestLocale() {

    Locale locale = LocaleContextHolder.getLocale();

    Locale localeLanguage = Locale.forLanguageTag(locale.getLanguage());

    if (!AVAILABLE_LOCALES.contains(localeLanguage)) {
      return DEFAULT_LOCALE;
    }

    return localeLanguage;
  }

  public static List<Locale> getAvailableLocales() {
    return AVAILABLE_LOCALES;
  }

  public static Locale getDefaultLocale() {
    return DEFAULT_LOCALE;
  }
}

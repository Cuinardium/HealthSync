package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.annotations.ValidLocale;
import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import java.util.Locale;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocaleValidator implements ConstraintValidator<ValidLocale, String> {

  @Override
  public boolean isValid(String localeStr, ConstraintValidatorContext context) {
    if (localeStr == null || localeStr.isEmpty()) {
      // Other validators will check for this
      return true;
    }

    Locale locale = Locale.forLanguageTag(localeStr);

    return LocaleUtil.getAvailableLocales().stream()
        .anyMatch(l -> l.getLanguage().equals(locale.getLanguage()));
  }
}

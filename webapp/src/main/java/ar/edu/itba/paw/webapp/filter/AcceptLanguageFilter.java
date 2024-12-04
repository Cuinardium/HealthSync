package ar.edu.itba.paw.webapp.filter;

import ar.edu.itba.paw.webapp.utils.LocaleUtil;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Locale;

@Provider
public class AcceptLanguageFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        List<Locale> acceptableLanguages = requestContext.getAcceptableLanguages();

        List<Locale> availableLocales = LocaleUtil.getAvailableLocales();

        Locale selectedLocale = acceptableLanguages.stream()
                .map(Locale::getLanguage)
                .filter(language -> availableLocales.contains(Locale.forLanguageTag(language)))
                .map(Locale::forLanguageTag)
                .findFirst()
                .orElse(LocaleUtil.getDefaultLocale());


        LocaleContextHolder.setLocale(selectedLocale);
    }

}

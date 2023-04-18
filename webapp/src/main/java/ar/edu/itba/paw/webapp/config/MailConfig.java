package ar.edu.itba.paw.webapp.config;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@ComponentScan({"ar.edu.itba.paw.interfaces.services"})
public class MailConfig {

  // get property from application.properties
  @Autowired private Environment env;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);

    mailSender.setUsername(env.getProperty("mail.username"));
    mailSender.setPassword(env.getProperty("mail.password"));

    Properties properties = mailSender.getJavaMailProperties();
    properties.put("mail.transport.protocol", "smtp");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.debug", "true");

    return mailSender;
  }

  // Resolver for locating mail templates
  // Located un resources/mailTemplates
  @Bean
  public ITemplateResolver thymeleafTemplateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

    templateResolver.setPrefix("mailTemplates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML");
    templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
    templateResolver.setOrder(0);

    return templateResolver;
  }

  // Message source for translations
  // Located in resources/i18n
  // With file format mailMessages_xx_YY.properties
  @Bean
  public ResourceBundleMessageSource emailMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

    messageSource.setCacheSeconds(5);
    messageSource.setBasename("i18n/mailMessages");
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

    return messageSource;
  }

  @Bean
  public SpringTemplateEngine thymeleafTemplateEngine(ITemplateResolver templateResolver) {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();

    templateEngine.setTemplateResolver(templateResolver);
    templateEngine.setTemplateEngineMessageSource(emailMessageSource());

    return templateEngine;
  }
}

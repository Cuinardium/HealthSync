package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.MailService;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

@Service
public class MailServiceImpl implements MailService {

  private static final String FROM = "noreply@healthsync.com";

  private final JavaMailSender mailSender;

  private final SpringTemplateEngine templateEngine;

  private final ResourceBundleMessageSource messageSource;

  @Autowired
  public MailServiceImpl(
      JavaMailSender mailSender,
      SpringTemplateEngine templateEngine,
      ResourceBundleMessageSource messageSource) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
    this.messageSource = messageSource;
  }

  private void sendHtmlMessage(String to, String subject, String htmlBody) {
    MimeMessage message = mailSender.createMimeMessage();

    try {
      MimeMessageHelper helper =
          new MimeMessageHelper(message, true, StandardCharsets.UTF_8.displayName());

      helper.setTo(to);
      helper.setFrom(FROM);
      helper.setSubject(subject);
      helper.setText(htmlBody, true);
      mailSender.send(message);
    } catch (MessagingException e) {
      // TODO: error handling
    }
  }

  private String getHtmlBody(String template, Map<String, Object> templateModel, Locale locale) {
    Context context = new Context(locale);

    context.setVariables(templateModel);
    return templateEngine.process(template, context);
  }

  @Override
  public void sendAppointmentRequestMail(
      String clientEmail,
      String doctorEmail,
      String clientName,
      String healthCare,
      String date,
      String description,
      Locale locale) {

    Map<String, Object> templateModel = new HashMap<>();

    // Load model
    templateModel.put("userName", clientName);
    templateModel.put("date", date);
    templateModel.put("description", description);
    templateModel.put("userHealthcare", healthCare);
    templateModel.put("userMail", clientEmail);

    String htmlBody = getHtmlBody("appointmentRequest", templateModel, locale);

    String subject = messageSource.getMessage("appointmentRequest.subject", null, locale);

    sendHtmlMessage(doctorEmail, subject, htmlBody);
  }

  @Override
  public void sendAppointmentReminderMail(
          String clientEmail,
          String doctorEmail,
          String address,
          String city,
          String clientName,
          String healthCare,
          String date,
          String description,
          Locale locale
  ){
    Map<String, Object> templateModel = new HashMap<>();

    // Load model
    templateModel.put("userName", clientName);
    templateModel.put("userMail", clientEmail);
    templateModel.put("docEmail", doctorEmail);
    templateModel.put("address", address);
    templateModel.put("city", city);
    templateModel.put("date", date);
    templateModel.put("description", description);
    templateModel.put("userHealthcare", healthCare);

    String htmlBody = getHtmlBody("appointmentReminder", templateModel, locale);

    String subject = messageSource.getMessage("appointmentReminder.subject", null, locale);

    sendHtmlMessage(clientEmail, subject, htmlBody);
  }
}

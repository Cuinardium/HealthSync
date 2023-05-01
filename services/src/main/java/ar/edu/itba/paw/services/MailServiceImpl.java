package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

@Service
public class MailServiceImpl implements MailService {

  private static final String FROM = "noreply@healthsync.com";

  private final JavaMailSender mailSender;

  private final SpringTemplateEngine templateEngine;

  // For mail messages
  private final ResourceBundleMessageSource mailMessageSource;
  // For i18n enums
  private final MessageSource messageSource;

  @Autowired
  public MailServiceImpl(
      JavaMailSender mailSender,
      SpringTemplateEngine templateEngine,
      ResourceBundleMessageSource mailMessageSource,
      MessageSource messageSource) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
    this.mailMessageSource = mailMessageSource;
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
  @Async
  public void sendAppointmentRequestMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale) {

    Map<String, Object> templateModel = new HashMap<>();

    String patientName = patient.getFirstName() + " " + patient.getLastName();
    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeggining();
    String description = appointment.getDescription();
    String patientHealthInsurance =
        messageSource.getMessage(patient.getHealthInsurance().getMessageID(), null, locale);
    String patientEmail = patient.getEmail();

    // Load model
    templateModel.put("userName", patientName);
    templateModel.put("date", dateTime);
    templateModel.put("description", description);
    templateModel.put("userHealthcare", patientHealthInsurance);
    templateModel.put("userMail", patientEmail);

    String htmlBody = getHtmlBody("appointmentRequest", templateModel, locale);

    String subject = mailMessageSource.getMessage("appointmentRequest.subject", null, locale);

    String doctorEmail = doctor.getEmail();

    sendHtmlMessage(doctorEmail, subject, htmlBody);
  }

  @Override
  @Async
  public void sendAppointmentReminderMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale) {

    Map<String, Object> templateModel = new HashMap<>();

    String patientName = patient.getFirstName() + " " + patient.getLastName();
    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeggining();
    String description = appointment.getDescription();
    String patientHealthInsurance =
        messageSource.getMessage(patient.getHealthInsurance().getMessageID(), null, locale);
    String patientEmail = patient.getEmail();
    String doctorEmail = doctor.getEmail();
    String doctorAddress = doctor.getLocation().getAddress();
    String doctorCity =
        messageSource.getMessage(doctor.getLocation().getCity().getMessageID(), null, locale);

    // Load model
    templateModel.put("userName", patientName);
    templateModel.put("userMail", patientEmail);
    templateModel.put("docEmail", doctorEmail);
    templateModel.put("address", doctorAddress);
    templateModel.put("city", doctorCity);
    templateModel.put("date", dateTime);
    templateModel.put("description", description);
    templateModel.put("userHealthcare", patientHealthInsurance);

    String htmlBody = getHtmlBody("appointmentReminder", templateModel, locale);

    String subject = mailMessageSource.getMessage("appointmentReminder.subject", null, locale);

    sendHtmlMessage(patientEmail, subject, htmlBody);
  }
}

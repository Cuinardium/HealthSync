package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

@Service
public class MailServiceImpl implements MailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

  private static final String FROM = "noreply@healthsync.com";

  private final JavaMailSender mailSender;

  private final SpringTemplateEngine templateEngine;

  // For mail messages
  private final ResourceBundleMessageSource mailMessageSource;
  // For i18n enums
  private final MessageSource messageSource;

  private final String baseUrl;

  @Autowired
  public MailServiceImpl(
      JavaMailSender mailSender,
      SpringTemplateEngine templateEngine,
      ResourceBundleMessageSource mailMessageSource,
      MessageSource messageSource,
      Environment env) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
    this.mailMessageSource = mailMessageSource;
    this.messageSource = messageSource;

    String baseUrl = env.getProperty("WEBAPP_BASEURL");

    // Add trailing slash if not present
    if (!baseUrl.endsWith("/")) {
      baseUrl += "/";
    }

    this.baseUrl = baseUrl;
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
      LOGGER.error("Error sending email", e);
    }
  }

  private String getHtmlBody(String template, Map<String, Object> templateModel, Locale locale) {
    Context context = new Context(locale);

    context.setVariables(templateModel);
    return templateEngine.process(template, context);
  }

  // ========================== Appointments ==========================

  // ========================= For Doctor =========================

  @Override
  @Async
  public void sendAppointmentRequestMail(Appointment appointment) {

    Locale locale = appointment.getDoctor().getLocale();

    Map<String, Object> templateModel = new HashMap<>();

    String patientName =
        appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName();
    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeginning();
    String description = appointment.getDescription();
    String patientHealthInsurance =
        messageSource.getMessage(
            appointment.getPatient().getHealthInsurance().getMessageID(), null, locale);
    String patientEmail = appointment.getPatient().getEmail();

    String appointmentUrl = baseUrl + "detailed-appointment/" + appointment.getId();

    // Load model
    templateModel.put("baseUrl", baseUrl);
    templateModel.put("userName", patientName);
    templateModel.put("date", dateTime);
    templateModel.put("description", description);
    templateModel.put("userHealthcare", patientHealthInsurance);
    templateModel.put("userMail", patientEmail);
    templateModel.put("appointmentUrl", appointmentUrl);

    String htmlBody = getHtmlBody("appointmentRequest", templateModel, locale);

    String subject = mailMessageSource.getMessage("appointmentRequest.subject", null, locale);

    String doctorEmail = appointment.getDoctor().getEmail();

    sendHtmlMessage(doctorEmail, subject, htmlBody);
  }

  @Override
  @Async
  public void sendAppointmentCancelledByPatientMail(Appointment appointment) {

    Locale locale = appointment.getDoctor().getLocale();

    Map<String, Object> templateModel = new HashMap<>();

    String patientName =
        appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName();
    String patientEmail = appointment.getPatient().getEmail();
    String patientHealthInsurance =
        messageSource.getMessage(
            appointment.getPatient().getHealthInsurance().getMessageID(), null, locale);

    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeginning();
    String description = appointment.getDescription();

    String doctorEmail = appointment.getDoctor().getEmail();

    String cancelDescription = appointment.getCancelDesc();

    // Load model
    templateModel.put("baseUrl", baseUrl);
    templateModel.put("userName", patientName);
    templateModel.put("userMail", patientEmail);
    templateModel.put("userHealthcare", patientHealthInsurance);

    templateModel.put("date", dateTime);
    templateModel.put("description", description);
    templateModel.put("cancelDesc", cancelDescription);

    String htmlBody = getHtmlBody("appointmentCancelledByPatient", templateModel, locale);

    String subject = mailMessageSource.getMessage("appointmentCancelled.subject", null, locale);

    sendHtmlMessage(doctorEmail, subject, htmlBody);
  }

  // ========================== For Patient ==========================

  @Override
  @Async
  public void sendAppointmentReminderMail(Appointment appointment) {

    Locale locale = appointment.getPatient().getLocale();

    Map<String, Object> templateModel = new HashMap<>();

    String patientName =
        appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName();
    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeginning();
    String description = appointment.getDescription();
    String patientHealthInsurance =
        messageSource.getMessage(
            appointment.getPatient().getHealthInsurance().getMessageID(), null, locale);
    String patientEmail = appointment.getPatient().getEmail();
    String doctorName =
        appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
    String doctorAddress = appointment.getDoctor().getAddress();
    String doctorCity = appointment.getDoctor().getCity();

    // Load model
    templateModel.put("baseUrl", baseUrl);
    templateModel.put("userName", patientName);
    templateModel.put("userMail", patientEmail);
    templateModel.put("docName", doctorName);
    templateModel.put("address", doctorAddress);
    templateModel.put("city", doctorCity);
    templateModel.put("date", dateTime);
    templateModel.put("description", description);
    templateModel.put("userHealthcare", patientHealthInsurance);

    String htmlBody = getHtmlBody("appointmentReminder", templateModel, locale);

    String subject = mailMessageSource.getMessage("appointmentReminder.subject", null, locale);

    sendHtmlMessage(patientEmail, subject, htmlBody);
  }

  @Override
  @Async
  public void sendAppointmentCancelledByDoctorMail(Appointment appointment) {

    Locale locale = appointment.getPatient().getLocale();

    Map<String, Object> templateModel = new HashMap<>();

    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeginning();
    String patientEmail = appointment.getPatient().getEmail();
    String doctorName =
        appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();

    String doctorUrl = baseUrl + "detailed-doctor/" + appointment.getDoctor().getId();

    String cancelDescription = appointment.getCancelDesc();

    // Load model
    templateModel.put("baseUrl", baseUrl);
    templateModel.put("userMail", patientEmail);
    templateModel.put("docName", doctorName);
    templateModel.put("doctorUrl", doctorUrl);
    templateModel.put("date", dateTime);
    templateModel.put("cancelDesc", cancelDescription);

    String htmlBody = getHtmlBody("appointmentCancelledByDoctor", templateModel, locale);

    String subject = mailMessageSource.getMessage("appointmentCancelled.subject", null, locale);

    sendHtmlMessage(patientEmail, subject, htmlBody);
  }

  @Override
  @Async
  public void sendAppointmentCompletedMail(Appointment appointment) {

    Locale locale = appointment.getPatient().getLocale();

    Map<String, Object> templateModel = new HashMap<>();

    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeginning();

    String doctorName =
        appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();

    String reviewUrl = baseUrl + "detailed-doctor/" + appointment.getDoctor().getId();

    // Load model
    templateModel.put("baseUrl", baseUrl);
    templateModel.put("reviewUrl", reviewUrl);
    templateModel.put("docName", doctorName);
    templateModel.put("date", dateTime);

    String htmlBody = getHtmlBody("appointmentCompleted", templateModel, locale);

    String subject = mailMessageSource.getMessage("appointmentCompleted.subject", null, locale);

    sendHtmlMessage(appointment.getPatient().getEmail(), subject, htmlBody);
  }

  @Override
  @Async
  public void sendConfirmationMail(VerificationToken token) {

    User user = token.getUser();
    Locale locale = user.getLocale();

    String name = user.getFirstName() + " " + user.getLastName();

    Map<String, Object> templateModel = new HashMap<>();

    String confirmationUrl = baseUrl + "verify";

    // Load model
    templateModel.put("baseUrl", baseUrl);
    templateModel.put("confirmationUrl", confirmationUrl);
    templateModel.put("email", user.getEmail());
    templateModel.put("token", token.getToken());

    templateModel.put("userName", name);

    String htmlBody = getHtmlBody("confirmation", templateModel, locale);

    String subject = mailMessageSource.getMessage("confirmation.subject", null, locale);

    sendHtmlMessage(user.getEmail(), subject, htmlBody);
  }
}

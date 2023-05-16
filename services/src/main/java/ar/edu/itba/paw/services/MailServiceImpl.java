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
import org.springframework.core.env.Environment;
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

  // To get urls from application.properties
  private Environment env;

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
    this.env = env;
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

  // ========================== Appointments ==========================

  // ========================= For Doctor =========================

  @Override
  @Async
  public void sendAppointmentRequestMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale) {

    Map<String, Object> templateModel = new HashMap<>();

    String patientName = patient.getFirstName() + " " + patient.getLastName();
    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeginning();
    String description = appointment.getDescription();
    String patientHealthInsurance =
        messageSource.getMessage(patient.getHealthInsurance().getMessageID(), null, locale);
    String patientEmail = patient.getEmail();

    String baseUrl = env.getProperty("webapp.baseUrl");

    String appointmentsUrl = baseUrl + "my-appointments";

    // Load model
    templateModel.put("baseUrl", baseUrl);
    templateModel.put("userName", patientName);
    templateModel.put("date", dateTime);
    templateModel.put("description", description);
    templateModel.put("userHealthcare", patientHealthInsurance);
    templateModel.put("userMail", patientEmail);
    templateModel.put("appointmentsUrl", appointmentsUrl);

    String htmlBody = getHtmlBody("appointmentRequest", templateModel, locale);

    String subject = mailMessageSource.getMessage("appointmentRequest.subject", null, locale);

    String doctorEmail = doctor.getEmail();

    sendHtmlMessage(doctorEmail, subject, htmlBody);
  }

  @Override
  @Async
  public void sendAppointmentCancelledByPatientMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale) {

    Map<String, Object> templateModel = new HashMap<>();

    String patientName = patient.getFirstName() + " " + patient.getLastName();
    String patientEmail = patient.getEmail();
    String patientHealthInsurance =
        messageSource.getMessage(patient.getHealthInsurance().getMessageID(), null, locale);

    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeginning();
    String description = appointment.getDescription();

    String doctorEmail = doctor.getEmail();

    // Load model
    templateModel.put("baseUrl", env.getProperty("webapp.baseUrl"));
    templateModel.put("userName", patientName);
    templateModel.put("userMail", patientEmail);
    templateModel.put("userHealthcare", patientHealthInsurance);

    templateModel.put("date", dateTime);
    templateModel.put("description", description);
    templateModel.put("cancelDesc", appointment.getCancelDesc());

    String htmlBody = getHtmlBody("appointmentCancelledByPatient", templateModel, locale);

    String subject = mailMessageSource.getMessage("appointmentCancelled.subject", null, locale);

    sendHtmlMessage(doctorEmail, subject, htmlBody);
  }

  // ========================== For Patient ==========================

  @Override
  @Async
  public void sendAppointmentReminderMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale) {

    Map<String, Object> templateModel = new HashMap<>();

    String patientName = patient.getFirstName() + " " + patient.getLastName();
    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeginning();
    String description = appointment.getDescription();
    String patientHealthInsurance =
        messageSource.getMessage(patient.getHealthInsurance().getMessageID(), null, locale);
    String patientEmail = patient.getEmail();
    String doctorName = doctor.getFirstName() + " " + doctor.getLastName();
    String doctorAddress = doctor.getLocation().getAddress();
    String doctorCity =
        messageSource.getMessage(doctor.getLocation().getCity().getMessageID(), null, locale);

    // Load model
    templateModel.put("baseUrl", env.getProperty("webapp.baseUrl"));
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
  public void sendAppointmentCancelledByDoctorMail(
      Appointment appointment, Doctor doctor, Patient patient, Locale locale) {
    Map<String, Object> templateModel = new HashMap<>();

    String dateTime =
        appointment.getDate().toString() + " " + appointment.getTimeBlock().getBlockBeginning();
    String patientEmail = patient.getEmail();
    String doctorName = doctor.getFirstName() + " " + doctor.getLastName();

    String baseUrl = env.getProperty("webapp.baseUrl");

    String doctorAppointmentUrl = baseUrl + doctor.getId() + "/appointment";

    // Load model
    templateModel.put("baseUrl", baseUrl);
    templateModel.put("userMail", patientEmail);
    templateModel.put("docName", doctorName);
    templateModel.put("appointmentUrl", doctorAppointmentUrl);
    templateModel.put("date", dateTime);
    templateModel.put("cancelDesc", appointment.getCancelDesc());

    String htmlBody = getHtmlBody("appointmentCancelledByDoctor", templateModel, locale);

    String subject = mailMessageSource.getMessage("appointmentCancelled.subject", null, locale);

    sendHtmlMessage(patientEmail, subject, htmlBody);
  }
}

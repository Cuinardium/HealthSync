package ar.edu.itba.paw.webapp.utils;

import java.net.URI;
import javax.ws.rs.core.UriInfo;

public class URIUtil {

  private static final String IMAGE_BASE_PATH = "/images";
  private static final String HEALTHINSURANCE_BASE_PATH = "/healthinsurances";
  private static final String APPOINTMENT_BASE_PATH = "/appointments";
  private static final String NOTIFICATION_BASE_PATH = "/notifications";
  private static final String SPECIALTY_BASE_PATH = "/specialties";
  private static final String REVIEW_BASE_PATH = "/reviews";
  private static final String ATTENDINGHOUR_BASE_PATH = "/attendinghours";
  private static final String OCCUPIEDHOUR_BASE_PATH = "/occupiedhours";
  private static final String VACATION_BASE_PATH = "/vacations";
  private static final String DOCTOR_BASE_PATH = "/doctors";
  private static final String PATIENT_BASE_PATH = "/patients";

  private URIUtil() {
    throw new RuntimeException();
  }

  public static URI getImageURI(UriInfo uriInfo, long imageId) {
    return uriInfo.getBaseUriBuilder().path(IMAGE_BASE_PATH).path(String.valueOf(imageId)).build();
  }

  public static URI getHealthInsuranceURI(UriInfo uriInfo, long healthInsuranceId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(HEALTHINSURANCE_BASE_PATH)
        .path(String.valueOf(healthInsuranceId))
        .build();
  }

  public static URI getAppointmentsURI(UriInfo uriInfo) {
    return uriInfo.getBaseUriBuilder().path(APPOINTMENT_BASE_PATH).build();
  }

  public static URI getAppointmentURI(UriInfo uriInfo, long appointmentId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(APPOINTMENT_BASE_PATH)
        .path(String.valueOf(appointmentId))
        .build();
  }

  public static URI getUserAppointmentURI(UriInfo uriInfo, long userId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(APPOINTMENT_BASE_PATH)
        .queryParam("userId", userId)
        .build();
  }

  public static URI getNotificationURI(UriInfo uriInfo, long notificationId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(NOTIFICATION_BASE_PATH)
        .path(String.valueOf(notificationId))
        .build();
  }

  public static URI getUserNotificationURI(UriInfo uriInfo, long userId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(NOTIFICATION_BASE_PATH)
        .queryParam("userId", userId)
        .build();
  }

  public static URI getSpecialtyURI(UriInfo uriInfo, long specialtyId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(SPECIALTY_BASE_PATH)
        .path(String.valueOf(specialtyId))
        .build();
  }

  public static URI getReviewURI(UriInfo uriInfo, long doctorId, long reviewId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(DOCTOR_BASE_PATH)
        .path(String.valueOf(doctorId))
        .path(REVIEW_BASE_PATH)
        .path(String.valueOf(reviewId))
        .build();
  }

  public static URI getDoctorReviewURI(UriInfo uriInfo, long doctorId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(DOCTOR_BASE_PATH)
        .path(String.valueOf(doctorId))
        .path(REVIEW_BASE_PATH)
        .build();
  }

  public static URI getAttendingHoursURI(UriInfo uriInfo, long doctorId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(DOCTOR_BASE_PATH)
        .path(String.valueOf(doctorId))
        .path(ATTENDINGHOUR_BASE_PATH)
        .build();
  }

  public static URI getOccupiedHoursURI(UriInfo uriInfo, long doctorId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(DOCTOR_BASE_PATH)
        .path(String.valueOf(doctorId))
        .path(OCCUPIEDHOUR_BASE_PATH)
        .build();
  }

  public static URI getVacationURI(UriInfo uriInfo, long doctorId, long vacationId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(DOCTOR_BASE_PATH)
        .path(String.valueOf(doctorId))
        .path(VACATION_BASE_PATH)
        .path(String.valueOf(vacationId))
        .build();
  }

  public static URI getDoctorVacationURI(UriInfo uriInfo, long doctorId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(DOCTOR_BASE_PATH)
        .path(String.valueOf(doctorId))
        .path(VACATION_BASE_PATH)
        .build();
  }

  public static URI getDoctorURI(UriInfo uriInfo, long doctorId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(DOCTOR_BASE_PATH)
        .path(String.valueOf(doctorId))
        .build();
  }

  public static URI getPatientURI(UriInfo uriInfo, long patientId) {
    return uriInfo
        .getBaseUriBuilder()
        .path(PATIENT_BASE_PATH)
        .path(String.valueOf(patientId))
        .build();
  }
}

package ar.edu.itba.paw.models;

public enum AppointmentStatus {

  // NO CAMBIAR EL ORDEN, SIEMPRE INSERTAR AL FONDO
  // appointmentStatus.<appointmentStatus> tiene que estar internacionalizado en los i18n
  PENDING("appointmentStatus.pending"),
  ACCEPTED("appointmentStatus.accepted"),
  REJECTED("appointmentStatus.rejected"),
  CANCELLED("appointmentStatus.cancelled");

  private final String messageID;

  private AppointmentStatus(final String messageID) {
    this.messageID = messageID;
  }

  public String getMessageID() {
    return messageID;
  }
}

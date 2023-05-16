package ar.edu.itba.paw.models;

public enum AppointmentStatus {

  // NO CAMBIAR EL ORDEN, SIEMPRE INSERTAR AL FONDO
  // appointmentStatus.<appointmentStatus> tiene que estar internacionalizado en los i18n
  CONFIRMED("appointmentStatus.confirmed"),
  CANCELLED("appointmentStatus.cancelled"),
  COMPLETED("appointmentStatus.completed");

  private final String messageID;

  private AppointmentStatus(final String messageID) {
    this.messageID = messageID;
  }

  public String getMessageID() {
    return messageID;
  }
}

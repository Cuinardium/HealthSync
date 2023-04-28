package ar.edu.itba.paw.models;

public enum HealthInsurance {

  // NO CAMBIAR EL ORDEN, SIEMPRE INSERTAR AL FONDO
  // specialty.<specialty> tiene que estar internacionalizado en los i18n
  OMINT("healthInsurance.omint"),
  OSDE("healthInsurance.osde"),
  SWISS_MEDICAL("healthInsurance.swiss.medical");

  private final String messageID;

  private HealthInsurance(final String messageID) {
    this.messageID = messageID;
  }

  public String getMessageID() {
    return messageID;
  }
}

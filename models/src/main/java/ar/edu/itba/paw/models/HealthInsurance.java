package ar.edu.itba.paw.models;

public enum HealthInsurance {
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

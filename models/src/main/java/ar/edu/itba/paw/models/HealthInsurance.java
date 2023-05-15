package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum HealthInsurance {

  // NO CAMBIAR EL ORDEN, SIEMPRE INSERTAR AL FONDO
  // healthInsurance.<healthInsurance> tiene que estar internacionalizado en los i18n
  NONE("healthInsurance.none"),
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

  public static HealthInsurance getHealthInsurance(int healthInsuranceCode) {
    return HealthInsurance.values()[healthInsuranceCode];
  }

  public static List<HealthInsurance> getHealthInsurances(List<Integer> healthInsurancesCodes) {

    if (healthInsurancesCodes == null) {
      return Collections.emptyList();
    }

    List<HealthInsurance> healthInsurances = new ArrayList<>();
    HealthInsurance[] healthInsurancesArray = HealthInsurance.values();

    for (Integer healthInsuranceCode : healthInsurancesCodes) {
      if (healthInsuranceCode != null
          && healthInsuranceCode >= 0
          && healthInsuranceCode < healthInsurancesArray.length) {
        healthInsurances.add(healthInsurancesArray[healthInsuranceCode]);
      }
    }

    return healthInsurances;
  }
}

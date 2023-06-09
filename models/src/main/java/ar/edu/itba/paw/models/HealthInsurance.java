package ar.edu.itba.paw.models;

import java.util.*;

public enum HealthInsurance {

  // las obtuve consultando www.miobrasocial.com.ar
  // NO CAMBIAR EL ORDEN, SIEMPRE INSERTAR AL FONDO
  // healthInsurance.<healthInsurance> tiene que estar internacionalizado en los i18n
  NONE("healthInsurance.none"),
  OMINT("healthInsurance.omint"),
  OSDE("healthInsurance.osde"),
  SWISS_MEDICAL("healthInsurance.swiss.medical"),
  GALENO("healthInsurance.galeno"),
  MEDIFE("healthInsurance.medife"),
  MEDICUS("healthInsurance.medicus"),
  ACCORD("healthInsurance.accord"),
  HOSPITAL_ITALIANO("healthInsurance.hospital.italiano"),
  HOSPITAL_ALEMAN("healthInsurance.hospital.aleman"),
  HOSPITAL_BRITANICO("healthInsurance.hospital.britanico"),
  PREVENCION_SALUD("healthInsurance.prevencion.salud"),
  AVALIAN("healthInsurance.avalian"),
  WILLIAM_HOPE("healthInsurance.william.hope"),
  PRE_MEDIC("healthInsurance.pre.medic"),
  OSFE("healthInsurance.osfe"),
  OSPACA("healthInsurance.ospaca"),
  ANDAR("healthInsurance.andar"),
  LUIS_PASTEUR("healthInsurance.luis.pasteur"),
  UNION_PERSONAL("healthInsurance.union.personal"),
  JERARQUICOS("healthInsurance.jerarquicos"),
  OSDEPYM("healthInsurance.osdepym"),
  OSPE("healthInsurance.ospe"),
  OSPLAD("healthInsurance.osplad"),
  OSSEG("healthInsurance.osseg"),
  SANCOR_SALUD("healthInsurance.sancor.salud");

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

  public static Set<HealthInsurance> getHealthInsurances(List<Integer> healthInsurancesCodes) {

    if (healthInsurancesCodes == null) {
      return Collections.emptySet();
    }

    Set<HealthInsurance> healthInsurances = new HashSet<>();
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

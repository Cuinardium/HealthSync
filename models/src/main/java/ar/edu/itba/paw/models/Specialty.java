package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Specialty {

  // NO CAMBIAR EL ORDEN, SIEMPRE INSERTAR AL FONDO
  // specialty.<specialty> tiene que estar internacionalizado en los i18n
  // Las especialidades las obtuve de:
  // https://www.argentina.gob.ar/sites/default/files/especialidades_medicas_2019.pdf
  // Algunas traducciones fueron retocadas :D
  // Arme un excel con las especialidades:
  // https://docs.google.com/spreadsheets/d/16GyBZ2_rLOlsEKNITkQ_AonuA4aH-8KHnARFikagJmA/edit?usp=sharing
  ALLERGY_AND_IMMUNOLOGY("specialty.allergy.and.immunology"),
  PEDIATRIC_ALLERGY_AND_IMMUNOLOGY("specialty.pediatric.allergy.and.immunology"),
  PATHOLOGICAL_ANATOMY("specialty.pathological.anatomy"),
  ANESTHESIOLOGY("specialty.anesthesiology"),
  GENERAL_ANGIOLOGY_AND_HEMODYNAMICS("specialty.general.angiology.and.hemodynamics"),
  CARDIOLOGY("specialty.cardiology"),
  CHILDRENS_CARDIOLOGIST("specialty.childrens.cardiologist"),
  CARDIOVASCULAR_SURGERY("specialty.cardiovascular.surgery"),
  PEDIATRIC_CARDIOVASCULAR_SURGERY("specialty.pediatric.cardiovascular.surgery"),
  HEAD_AND_NECK_SURGERY("specialty.head.and.neck.surgery"),
  THORAX_SURGERY("specialty.thorax.surgery"),
  GENERAL_SURGERY("specialty.general.surgery"),
  CHILD_SURGERY("specialty.child.surgery"),
  PLASTIC_AND_RECONSTRUCTIVE_SURGERY("specialty.plastic.and.reconstructive.surgery"),
  PERIPHERAL_VASCULAR_SURGERY("specialty.peripheral.vascular.surgery"),
  MEDICAL_CLINIC("specialty.medical.clinic"),
  COLOPROCTOLOGY("specialty.coloproctology"),
  DERMATOLOGY("specialty.dermatology"),
  PEDIATRIC_DERMATOLOGY("specialty.pediatric.dermatology"),
  IMAGE_DIAGNOSIS("specialty.image.diagnosis"),
  CARDIAC_ELECTROPHYSIOLOGY("specialty.cardiac.electrophysiology"),
  EMERGENTOLOGY("specialty.emergentology"),
  ENDOCRINOLOGY("specialty.endocrinology"),
  INFANT_ENDOCRINOLOGIST("specialty.infant.endocrinologist"),
  CLINICAL_PHARMACOLOGY("specialty.clinical.pharmacology"),
  PHYSIATRY("specialty.physiatria"),
  GASTROENTEROLOGY("specialty.gastroenterology"),
  CHILDRENS_GASTROENTEROLOGIST("specialty.childrens.gastroenterologist"),
  MEDICAL_GENETICS("specialty.medical.genetics"),
  GERIATRICS("specialty.geriatrics"),
  GYNECOLOGY("specialty.gynecology"),
  PEDIATRIC_HEMATO_AWARENESS("specialty.pediatric.hemato.awareness"),
  HEMATOLOGY("specialty.hematology"),
  HEMOTHERAPY_AND_IMMUNOHEMATOLOGY("specialty.hemotherapy.and.immunohematology"),
  HEPATOLOGY("specialty.hepatology"),
  PEDIATRIC_HEPATOLOGY("specialty.pediatric.hepatology"),
  INFECTOLOGY("specialty.infectology"),
  CHILD_INFECTOLOGIST("specialty.child.infectologist"),
  SPORTS_MEDICINE("specialty.sports.medicine"),
  WORK_MEDICINE("specialty.work.medicine"),
  GENERAL_MEDICINE_AND_OR_FAMILY_MEDICINE("specialty.general.medicine.and.or.family.medicine"),
  LEGAL_MEDICINE("specialty.legal.medicine"),
  NUCLEAR_MEDICINE("specialty.nuclear.medicine"),
  PALITATIVE_MEDICINE("specialty.palitative.medicine"),
  NEPHROLOGY("specialty.nephrology"),
  CHILD_NEPHROLOGIST("specialty.child.nephrologist"),
  NEONATOLOGY("specialty.neonatology"),
  PNEUMONOLOGY("specialty.pneumonology"),
  CHILD_PNEUMONOLOGIST("specialty.child.pneumonologist"),
  NEUROSURGERY("specialty.neurosurgery"),
  NEUROLOGY("specialty.neurology"),
  CHILD_NEUROLOGIST("specialty.child.neurologist"),
  NUTRITION("specialty.nutrition"),
  OBSTETRICS("specialty.obstetrics"),
  OPHTHALMOLOGY("specialty.ophthalmology"),
  ONCOLOGY("specialty.oncology"),
  ORTHOPEDICS_AND_TRAUMATOLOGY("specialty.orthopedics.and.traumatology"),
  ORTHOPEDICS_AND_CHILD_TRAUMATOLOGY("specialty.orthopedics.and.child.traumatology"),
  OTORHINOLARYNGOLOGY("specialty.otorhinolaryngology"),
  PEDIATRICS("specialty.pediatrics"),
  PSYCHIATRY("specialty.psychiatry"),
  CHILD_AND_YOUTH_PSYCHIATRY("specialty.child.and.youth.psychiatry"),
  RADIOTHERAPY_OR_RADIATION_THERAPY("specialty.radiotherapy.or.radiation.therapy"),
  RHEUMATOLOGY("specialty.rheumatology"),
  CHILD_RHEUMATOLOGIST("specialty.child.rheumatologist"),
  INTENSIVE_THERAPY("specialty.intensive.therapy"),
  INTENSIVE_CHILDRENS_THERAPIST("specialty.intensive.childrens.therapist"),
  TOCOGINECOLOGY("specialty.tocoginecology"),
  TOXICOLOGY("specialty.toxicology"),
  UROLOGY("specialty.urology");

  private final String messageID;

  private Specialty(final String messageID) {
    this.messageID = messageID;
  }

  public String getMessageID() {
    return messageID;
  }

  // TODO: check for range?
  public static Specialty getSpecialty(int specialtyCode) {
    return Specialty.values()[specialtyCode];
  }

  public static List<Specialty> getSpecialties(List<Integer> specialtiesCodes) {

    if (specialtiesCodes == null) {
      return Collections.emptyList();
    }

    List<Specialty> specialties = new ArrayList<>();
    Specialty[] specialtiesArray = Specialty.values();

    for (Integer specialtyCode : specialtiesCodes) {
      if (specialtyCode != null && specialtyCode >= 0 && specialtyCode < specialtiesArray.length) {
        specialties.add(specialtiesArray[specialtyCode]);
      }
    }

    return specialties;
  }
}

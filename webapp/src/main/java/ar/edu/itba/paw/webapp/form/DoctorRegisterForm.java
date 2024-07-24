package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotEmpty;

public class DoctorRegisterForm extends UserRegisterForm {

  private static final Collection<ThirtyMinuteBlock> DEFAULT_HOURS =
      ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_00);

  private static final Collection<DayOfWeek> DEFAULT_DAYS =
      Arrays.asList(
          DayOfWeek.MONDAY,
          DayOfWeek.TUESDAY,
          DayOfWeek.WEDNESDAY,
          DayOfWeek.THURSDAY,
          DayOfWeek.FRIDAY);

  @Valid
  @NotEmpty(message = "NotEmpty.doctorForm.healthInsurance")
  @FormDataParam("healthInsurance")
  private List<
          @ExistsInEnumString(
              enumClass = HealthInsurance.class,
              message = "ExistsInEnumString.doctorForm.healthInsurance")
          String>
      healthInsurances;

  @NotNull(message = "NotNull.doctorForm.city")
  @Size(min = 1, max = 20, message = "Size.doctorForm.city")
  @Pattern(regexp = "[a-zA-Z0-9. ñÑáÁéÉíÍóÓúÚ]+", message = "Pattern.doctorForm.city")
  @FormDataParam("city")
  private String city;

  @NotNull(message = "NotNull.doctorForm.address")
  @Size(min = 1, max = 100, message = "Size.doctorForm.address")
  @Pattern(regexp = "[a-zA-Z0-9. ñÑáÁéÉíÍóÓúÚ]+", message = "Pattern.doctorForm.address")
  @FormDataParam("address")
  private String address;

  @NotNull(message = "NotNull.doctorForm.specialty")
  @ExistsInEnumString(
      enumClass = Specialty.class,
      message = "ExistsInEnumString.doctorForm.specialty")
  @FormDataParam("specialty")
  private String specialty;

  public static Set<AttendingHours> getDefaultAttendingHours() {
    return DEFAULT_DAYS.stream()
        .flatMap(day -> DEFAULT_HOURS.stream().map(hour -> new AttendingHours(null, day, hour)))
        .collect(Collectors.toSet());
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<String> getHealthInsurances() {
    return healthInsurances;
  }

  public void setHealthInsurances(List<String> healthInsurances) {
    this.healthInsurances = healthInsurances;
  }

  public Set<HealthInsurance> getHealthInsurancesEnum() {
    return healthInsurances.stream().map(HealthInsurance::valueOf).collect(Collectors.toSet());
  }

  public String getSpecialty() {
    return specialty;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  public Specialty getSpecialtyEnum() {
    return Specialty.valueOf(specialty);
  }
}

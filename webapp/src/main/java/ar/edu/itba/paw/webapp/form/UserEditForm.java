package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.ValidLocale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.util.Locale;

public class UserEditForm extends ImageForm {
  @NotNull(message = "NotNull.userForm.name")
  @Size(min = 1, max = 50, message = "Size.userForm.name")
  @Pattern(regexp = "[a-zA-Z ñÑáÁéÉíÍóÓúÚ]+", message = "Pattern.userForm.name")
  @FormDataParam("name")
  private String name;

  @NotNull(message = "NotNull.userForm.lastname")
  @Size(min = 1, max = 50, message = "Size.userForm.lastname")
  @Pattern(regexp = "[a-zA-Z ñÑáÁéÉíÍóÓúÚ]+", message = "Pattern.userForm.lastname")
  @FormDataParam("lastname")
  private String lastname;

  @NotNull(message = "NotNull.userForm.email")
  @Size(min = 1, max = 50, message = "Size.userForm.email")
  @Pattern(
      regexp = "[a-zA-Z0-9.+-ñÑ]+@[a-zA-Z0-9.-]+(.com|.com.ar|.edu.ar)",
      message = "Pattern.userForm.email")
  @FormDataParam("email")
  private String email;

  @NotNull(message = "NotNull.userForm.locale")
  @ValidLocale(message = "ValidLocale.userForm.locale")
  @FormDataParam("locale")
  private String localeName;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLocaleName() {
    return localeName;
  }

  public Locale getLocale() {
    return Locale.forLanguageTag(localeName);
  }

  public void setLocaleName(String localeName) {
    this.localeName = localeName;
  }
}

package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.ValidLocale;
import java.util.Locale;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.glassfish.jersey.media.multipart.FormDataParam;

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

  public String getLocaleName() {
    return localeName;
  }

  public void setLocaleName(String localeName) {
    this.localeName = localeName;
  }

  public Locale getLocale() {
    return Locale.forLanguageTag(localeName);
  }
}

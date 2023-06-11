package ar.edu.itba.paw.webapp.form;

import java.util.Locale;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserEditForm extends ImageForm {
  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z nÑ]+")
  private String name;

  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z nÑ]+")
  private String lastname;

  @Size(min = 1)
  @Pattern(regexp = "[a-zA-Z0-9.+-nÑ]+@[a-zA-Z0-9.-]+(.com|.com.ar|.edu.ar)")
  private String email;

  private Locale locale;

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

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public Locale getLocale() {
    return locale;
  }
}

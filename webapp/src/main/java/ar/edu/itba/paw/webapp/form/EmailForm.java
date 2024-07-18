package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EmailForm {

    @NotNull(message = "NotNull.userForm.email")
    @Size(min = 1, max = 50, message = "Size.userForm.email")
    @Pattern(
            regexp = "[a-zA-Z0-9.+-ñÑ]+@[a-zA-Z0-9.-]+(.com|.com.ar|.edu.ar)",
            message = "Pattern.userForm.email")
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

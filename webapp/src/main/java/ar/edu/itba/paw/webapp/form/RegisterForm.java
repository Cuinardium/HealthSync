package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class RegisterForm{

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+")
    private String email;

    @Size(min = 4)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String password;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
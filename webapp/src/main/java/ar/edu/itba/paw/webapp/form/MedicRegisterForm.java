package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;


public class MedicRegisterForm{

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z]+")
    private String name;

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z]+")
    private String lastname;

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+")
    private String email;

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z]+")
    private String healthcare;

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z]+")
    private String city;

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String address;

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z]+")
    private String specialization;

    @Size(min = 4)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String password;

    @Size(min = 4)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String confirmPassword;

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

    public String getHealthcare() {
        return healthcare;
    }
    public void setHealthcare(String healthcare) {
        this.healthcare = healthcare;
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

    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


}
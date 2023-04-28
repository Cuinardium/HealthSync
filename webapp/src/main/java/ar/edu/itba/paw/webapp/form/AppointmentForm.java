package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AppointmentForm{

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z ]+")
    private String name;

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z ]+")
    private String lastname;

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z0-9.+-]+@[a-zA-Z0-9.-]+(.com|.com.ar|.edu.ar)")
    private String email;

    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z ]+")
    private String healthcare;

    private String date;

    @Size(min = 1, max = 100)
    private String description;

    private String docEmail;
    private String address;
    private String city;
    private int docId;

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

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocEmail() {
        return docEmail;
    }
    public void setDocEmail(String docEmail) {
        this.docEmail = docEmail;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public int getDocId() {
        return docId;
    }
    public void setDocId(int docId) {
        this.docId = docId;
    }
}

package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class IndicationForm {

    @Size(min = 1, max = 1000)
    private String indications;

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }
}

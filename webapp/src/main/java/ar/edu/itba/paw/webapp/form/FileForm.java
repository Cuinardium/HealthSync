package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.ValidateFile;
import org.springframework.web.multipart.MultipartFile;

public class FileForm {

    @ValidateFile
    private MultipartFile file = null;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

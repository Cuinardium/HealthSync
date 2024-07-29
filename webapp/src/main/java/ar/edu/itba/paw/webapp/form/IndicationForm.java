package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.annotations.ValidateFile;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

public class IndicationForm {

  private static final int MAX_FILE_SIZE = 1024 * 1024 * 10; // 10MB

  @NotNull(message = "NotNull.indicationForm.indications")
  @Size(min = 1, max = 1000, message = "Size.indicationForm.indications")
  @FormDataParam("indications")
  private String indications;

  @ValidateFile(message = "ValidateFile.indicationForm.file")
  @FormDataParam("file")
  private FormDataContentDisposition fileDetails;

  @Size(min = 1, max = MAX_FILE_SIZE, message = "Size.indicationForm.file")
  @FormDataParam("file")
  private byte[] fileContent;

  public byte[] getFileContent() {
    return fileContent;
  }

  public void setFileContent(byte[] fileContent) {
    this.fileContent = fileContent;
  }

  public String getFileName() {
    return fileDetails.getFileName();
  }

  public boolean hasFile() {
    return fileDetails != null && fileContent != null && fileContent.length > 0;
  }

  public String getIndications() {
    return indications;
  }

  public void setIndications(String indications) {
    this.indications = indications;
  }
}

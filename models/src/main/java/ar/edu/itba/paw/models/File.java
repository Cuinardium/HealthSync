package ar.edu.itba.paw.models;

import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "file")
public class File {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_file_id_seq")
  @SequenceGenerator(
      sequenceName = "file_file_id_seq",
      name = "file_file_id_seq",
      allocationSize = 1)
  @Column(name = "file_id")
  private Long fileId;

  @Column(name = "file", nullable = false)
  private byte[] bytes;

  @Column(name = "name", nullable = false)
  private String name;

  @OneToOne(mappedBy = "file")
  private Indication indication;

  protected File() {
    // Solo para hibernate
  }

  private File(Builder builder) {
    this.fileId = builder.id;
    this.bytes = builder.bytes;
    this.name = builder.name;
    this.indication = builder.indication;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long imageId) {
    this.fileId = imageId;
  }

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Indication getIndication() {
    return indication;
  }

  public void setIndication(Indication indication) {
    this.indication = indication;
  }

  @Override
  public String toString() {
    return "File [fileId=" + fileId + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof File)) return false;
    File other = (File) obj;
    return Objects.equals(fileId, other.fileId);
  }

  public static class Builder {

    private final byte[] bytes;
    private final String name;

    private Long id = null;

    private Indication indication = null;

    public Builder(byte[] bytes, String name) {
      this.bytes = bytes;
      this.name = name;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder indication(Indication indication) {
      this.indication = indication;
      return this;
    }

    public File build() {
      return new File(this);
    }
  }
}

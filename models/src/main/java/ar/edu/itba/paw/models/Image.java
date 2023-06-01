package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "profile_picture")
public class Image {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "profile_picture_profile_picture_id_seq"
  )
  @SequenceGenerator(
    sequenceName = "profile_picture_profile_picture_id_seq",
    name = "profile_picture_profile_picture_id_seq",
    allocationSize = 1
  )
  @Column(name = "profile_picture_id")
  private Long imageId;

  @Column(name = "profile_picture", nullable = false)
  private byte[] bytes;

  protected Image() {
    // Solo para hibernate
  }

  public Image(byte[] bytes) {
    this.bytes = bytes;
  }

  public Image(Long imageId, byte[] bytes) {
    this.imageId = imageId;
    this.bytes = bytes;
  }

  // Q: static fromMultiPartFile method
  public Long getImageId() {
    return imageId;
  }

  public void setImageId(Long imageId) {
    this.imageId = imageId;
  }

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }

  @Override
  public String toString() {
    return "Image [imageId=" + imageId + ", image=" + Arrays.toString(bytes) + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Image)) return false;
    Image other = (Image) obj;
    return Objects.equals(imageId, other.imageId) && Arrays.equals(bytes, other.bytes);
  }
}

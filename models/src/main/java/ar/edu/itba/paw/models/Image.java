package ar.edu.itba.paw.models;

import java.util.Objects;
import javax.persistence.*;

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

  @Column(name = "media_type")
  private String mediaType;

  @Column(name = "profile_picture", nullable = false)
  private byte[] bytes;

  @OneToOne(mappedBy = "image")
  private User user;

  protected Image() {
    // Solo para hibernate
  }

  private Image(Builder builder) {
    this.imageId = builder.id;
    this.mediaType = builder.mediaType;
    this.bytes = builder.bytes;
  }

  // Q: static fromMultiPartFile method
  public Long getImageId() {
    return imageId;
  }

  public void setImageId(Long imageId) {
    this.imageId = imageId;
  }

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "Image [imageId=" + imageId + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Image)) return false;
    Image other = (Image) obj;
    return Objects.equals(imageId, other.imageId);
  }

  public static class Builder {

    private byte[] bytes;

    private Long id = null;

    private String mediaType;

    public Builder(byte[] bytes, String mediaType) {
      this.bytes = bytes;
      this.mediaType = mediaType;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Image build() {
      return new Image(this);
    }
  }
}

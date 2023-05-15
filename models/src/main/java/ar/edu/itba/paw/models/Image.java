package ar.edu.itba.paw.models;

import java.util.Arrays;

public class Image {
  private Long imageId = null;
  private byte[] image;

  public Image(byte[] image) {
    this.image = image;
  }

  public Image(Long imageId, byte[] image) {
    this.imageId = imageId;
    this.image = image;
  }

  // Q: static fromMultiPartFile method

  public byte[] getBytes() {
    return image;
  }

  public Long getImageId() {
    return imageId;
  }

  @Override
  public String toString() {
    return "Image [imageId=" + imageId + ", image=" + Arrays.toString(image) + "]";
  }
}

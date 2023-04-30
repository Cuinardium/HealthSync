package ar.edu.itba.paw.models;

public class Image {
  private byte[] image;

  public Image(byte[] image) {
    this.image = image;
  }

  public byte[] getImage() {
    return image;
  }
}

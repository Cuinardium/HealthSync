package ar.edu.itba.paw.webapp.dto;

public class CityDto {

  private String name;
  private int popularity;


  public static CityDto fromCity(final String name, final int popularity) {
    final CityDto dto = new CityDto();
    dto.name = name;
    dto.popularity = popularity;
    return dto;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPopularity() {
    return popularity;
  }

  public void setPopularity(int popularity) {
    this.popularity = popularity;
  }
}

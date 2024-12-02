package ar.edu.itba.paw.webapp.query;

import javax.validation.constraints.Pattern;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class CityQuery extends DoctorQuery{

  @Pattern(regexp = "^(popularity|alphabetical)$", message = "Pattern.cityQuery.sort")
  @DefaultValue("popularity")
  @QueryParam("sort")
  private String sort;

  @Pattern(regexp = "^(asc|desc)$", message = "Pattern.query.order")
  @DefaultValue("desc")
  @QueryParam("order")
  private String order;

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public boolean sortByPopularity() {
    return sort.equals("popularity");
  }

  public boolean reversed() {
    return order.equals("desc");
  }

  @Override
  public String toString() {
    return "CityQuery [sort=" + sort + ", order=" + order + "]";
  }
}

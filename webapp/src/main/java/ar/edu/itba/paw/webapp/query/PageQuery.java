package ar.edu.itba.paw.webapp.query;

import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class PageQuery {

  @QueryParam("page")
  @Min(value = 1, message = "Size.pageQuery.page")
  @DefaultValue("1")
  private int page;

  @QueryParam("pageSize")
  @Min(value = 1, message = "Size.pageQuery.pageSize")
  @DefaultValue("10")
  private int pageSize;

  public int getPage() {
    return page - 1;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  public String toString() {
    return "PageQuery [page=" + page + ", pageSize=" + pageSize + "]";
  }
}

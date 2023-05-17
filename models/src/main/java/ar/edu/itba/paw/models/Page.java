package ar.edu.itba.paw.models;

import java.util.List;

public class Page<T> {

  private final List<T> content;
  private final Integer currentPage;
  private final Integer totalContentCount;
  private final Integer pageSize;

  public Page(List<T> content, Integer currentPage, Integer totalContentCount, Integer pageSize) {
    this.content = content;
    this.currentPage = currentPage;
    this.totalContentCount = totalContentCount;
    this.pageSize = pageSize;
  }

  public List<T> getContent() {
    return content;
  }

  public Integer getCurrentPage() {
    return currentPage;
  }

  public Integer getTotalPages() {

    if (pageSize == null || totalContentCount == null) {
      return null;
    }

    return (int) Math.ceil((double) totalContentCount / pageSize);
  }
}

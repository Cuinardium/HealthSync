package ar.edu.itba.paw.models;

import java.util.List;

public class Page<T> {

  private final List<T> content;
  private final int currentPage;
  private final int totalContentCount;
  private final int pageSize;

  public Page(List<T> content, int currentPage, int totalContentCount, int pageSize) {
    this.content = content;
    this.currentPage = currentPage;
    this.totalContentCount = totalContentCount;
    this.pageSize = pageSize;
  }

  public List<T> getContent() {
    return content;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public int getTotalPages() {
    return (int) Math.ceil((double) totalContentCount / pageSize);
  }
}

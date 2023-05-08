package ar.edu.itba.paw.models;

import java.util.List;

public class Page<T> {
  private final List<T> content;
  private final int currentPage;
  private final int totalContentCount;

  public Page(List<T> content, int currentPage, int totalContentCount) {
    this.content = content;
    this.currentPage = currentPage;
    this.totalContentCount = totalContentCount;
  }

  public List<T> getContent() {
    return content;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public int getTotalContentCount() {
    return totalContentCount;
  }

  public int getPageCount() {
    return content.size();
  }
}

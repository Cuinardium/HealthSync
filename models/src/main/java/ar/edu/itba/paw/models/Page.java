package ar.edu.itba.paw.models;

import java.util.List;
import java.util.Objects;

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

    return content.size() == 0 ? 1 : (int) Math.ceil((double) totalContentCount / pageSize);
  }

  // TODO: revisar, esto esta asi pq contamos las paginas desde 0 aca, pero desde 1 en el controller
  // ...
  public boolean hasNext() {
    return (this.getCurrentPage() < (this.getTotalPages() - 1));
  }

  public boolean hasPrev() {
    return (this.getCurrentPage() > 0);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Page<?>)) return false;
    Page<?> page = (Page<?>) obj;
    return Objects.equals(content, page.content)
        && Objects.equals(currentPage, page.currentPage)
        && Objects.equals(totalContentCount, page.totalContentCount)
        && Objects.equals(pageSize, page.pageSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, currentPage, totalContentCount, pageSize);
  }
}

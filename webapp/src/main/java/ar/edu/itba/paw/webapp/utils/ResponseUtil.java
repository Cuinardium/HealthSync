package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.Page;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

// TODO: how to properly do utils? should it be Singleton?
public class ResponseUtil {
  private ResponseUtil() {
    throw new RuntimeException();
  }

  // seria el metodo mencionado en clase
  // para armar los links de contenido paginado
  public static <T> Response.ResponseBuilder setPaginationLinks(
      Response.ResponseBuilder responseBuilder, UriInfo uri, Page<T> page) {
    String first = uri.getRequestUriBuilder().replaceQueryParam("page", 1).toString();
    String last =
        uri.getRequestUriBuilder().replaceQueryParam("page", page.getTotalPages()).toString();
    if (page.hasNext()) {
      String next =
          uri.getRequestUriBuilder()
              .replaceQueryParam("page", page.getCurrentPage() + 1)
              .toString();
      responseBuilder.link(next, "next");
    }
    if (page.hasPrev()) {
      String prev =
          uri.getRequestUriBuilder()
              .replaceQueryParam("page", page.getCurrentPage() - 1)
              .toString();
      responseBuilder.link(prev, "prev");
    }
    return responseBuilder.link(first, "first").link(last, "last");
  }
}

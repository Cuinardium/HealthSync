package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.Page;

import javax.ws.rs.core.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public final class ResponseUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseUtil.class);
  private static final int STATIC_MAX_AGE = 31536000;

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
              .replaceQueryParam(
                  "page", page.getCurrentPage() + 2)
              .toString();
      responseBuilder.link(next, "next");
    }
    if (page.hasPrev()) {
      String prev =
          uri.getRequestUriBuilder()
              .replaceQueryParam(
                  "page", page.getCurrentPage())
              .toString();
      responseBuilder.link(prev, "prev");
    }
    return responseBuilder.link(first, "first").link(last, "last");
  }
  
  // basado en el siguiente articulo
  // https://psamsotha.github.io/jersey/2015/10/18/http-caching-with-jersey.html
  public static Response.ResponseBuilder setEtagCache(Response.ResponseBuilder responseBuilder, Request request, EntityTag etag) {
    Response.ResponseBuilder notModified = request.evaluatePreconditions(etag);
    CacheControl cacheControl = new CacheControl();
    cacheControl.setNoStore(false);
    cacheControl.setMustRevalidate(true);

    // 304 Not Modified
    if(notModified != null) {
      LOGGER.debug("Cache HIT");
      return notModified.tag(etag).cacheControl(cacheControl);
    }
    LOGGER.debug("Cache MISSED");
    return responseBuilder.tag(etag).cacheControl(cacheControl);
  }

  public static Response.ResponseBuilder setImmutable(Response.ResponseBuilder responseBuilder) {
    CacheControl cacheControl = new CacheControl();
    cacheControl.setMaxAge(STATIC_MAX_AGE);
    cacheControl.setMustRevalidate(false);
    responseBuilder.cacheControl(cacheControl);
    return responseBuilder;
  }
}

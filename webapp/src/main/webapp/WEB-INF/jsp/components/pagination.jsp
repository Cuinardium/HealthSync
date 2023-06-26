<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/css/pagination.css" var="paginationCss"/>

<html>
<head>
    <link href="${paginationCss}" rel="stylesheet"/>
</head>
<body>
<c:if test="${param.totalPages > 1}">
    <nav class="paginationContainer">
        <ul class="pagination">
                <%-- Previous --%>
            <%-- TODO: FIX THIS --%>
            <li class="page-item ${param.currentPage == 1 ? "disabled" : ""}">
                <c:url value="${param.url}" var="url">
                    <c:param name="page" value="${param.currentPage - 1}"/>
                </c:url>
                <a class="page-link" href="${url}">
                    <span>&laquo;</span>
                </a>
            </li>

                <%-- First --%>
            <c:if test="${param.currentPage > 1}">
                <li class="page-item">
                    <c:url value="${param.url}" var="url">
                        <c:param name="page" value="1"/>
                    </c:url>
                    <a class="page-link" href="${url}">1</a>
                </li>
            </c:if>

                <%-- Filler --%>
            <c:if test="${param.currentPage > 2}">
                <li class="page-item disabled">
                    <a class="page-link" href="#">...</a>
                </li>
            </c:if>


                <%-- Current --%>
            <li class="page-item">
                <c:url value="${param.url}" var="url">
                    <c:param name="page" value="${param.currentPage}"/>
                </c:url>
                <a class="page-link active" href="${url}">${param.currentPage}</a>
            </li>

                <%-- Filler --%>
            <c:if test="${param.totalPages > 2 && param.currentPage < param.totalPages - 1}">
                <li class="page-item disabled">
                    <a class="page-link" href="#">...</a>
                </li>
            </c:if>

                <%-- Last --%>
            <c:if test="${param.totalPages > 1 && param.currentPage < param.totalPages}">
                <li class="page-item">
                    <c:url value="${param.url}" var="url">
                        <c:param name="page" value="${param.totalPages}"/>
                    </c:url>
                    <a class="page-link" href="${url}">${param.totalPages}</a>
                </li>
            </c:if>

                <%-- Next --%>
            <li class="page-item ${param.currentPage == param.totalPages ? "disabled" : ""}">
                <c:url value="${param.url}" var="url">
                    <c:param name="page" value="${param.currentPage + 1}"/>
                </c:url>
                <a class="page-link" href="${url}">
                    <span>&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>
</c:if>
</body>
</html>

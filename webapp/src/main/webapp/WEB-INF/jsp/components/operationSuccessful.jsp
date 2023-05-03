<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/appointmentSent.css" var="appointmentSentCss"/>

<c:url value="/" var="homeUrl"/>

<spring:message code="appointmentSent.home" var="homeMsg"/>

<html>
<head>
    <title><spring:message code="${operationTitle}"/></title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${appointmentSentCss}" rel="stylesheet"/>
</head>
<body>
<c:if test="${showHeader}">
    <jsp:include page="../components/header.jsp"/>
</c:if>

<!-- Content -->
<div class="page-content p-5" id="content">
    <div class="card media">
        <h5 class="card-title media-body">
            <spring:message code="${operationMsg}"/>
        </h5>
        <h6 class="card-text media-body">
            <a href="${homeUrl}" class="link-info">${homeMsg}</a>
        </h6>
    </div>
</div>
</body>
</html>

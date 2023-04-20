<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/appointmentSent.css" var="appointmentSentCss"/>

<c:url value="/" var="homeUrl"/>

<spring:message code="registerMedic.registerSuccessfulTitle" var="title"/>
<spring:message code="registerMedic.registerSuccessful" var="registerSuccessful"/>
<spring:message code="appointmentSent.home" var="homeMsg"/>


<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${appointmentSentCss}" rel="stylesheet"/>
</head>
<body>
<!-- Navbar -->
<jsp:include page="../components/navBar.jsp"/>
<!-- Content -->
<div class="page-content p-5" id="content">
    <div class="card media">
        <h5 class="card-title media-body">
            ${registerSuccessful}
        </h5>
        <h6 class="card-text media-body">
            <a href="${homeUrl}" class="link-info">${homeMsg}</a>
        </h6>
    </div>
</div>
</body>
</html>

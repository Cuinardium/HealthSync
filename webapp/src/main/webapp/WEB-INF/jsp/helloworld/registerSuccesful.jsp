<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="/resources/externalResources.jsp"/>

<html>
<head>
    <title>Register Succesfull</title>
    <c:url value="/css/main.css" var="mainCss" />
    <c:url value="/css/appointmentSent.css" var="appointmentSentCss" />
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${appointmentSentCss}" rel="stylesheet"/>
</head>
<body>
<jsp:include page="../components/navBar.jsp"/>
<div class="page-content p-5" id="content">
    <c:url value="/" var="home" />
    <div class="card media">
        <h5 class="card-title media-body">
            <spring:message code="registerMedic.registerSuccesful"/>
        </h5>
        <h5 class="card-subtitle media-body">
            <spring:message code="registerMedic.registerSuccesful2"/>
        </h5>
        <h6 class="card-text media-body">
          <a href="${home}" class="link-info"><spring:message code="appointmentSent.home"/></a>
        </h6>
    </div>
</div>
</body>
</html>

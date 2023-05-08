<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/error.css" var="errorCss"/>

<spring:message code="${message}" var="error"/>


<html>
<head>
    <title>${error}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${errorCss}" rel="stylesheet"/>
</head>
<body>
<jsp:include page="../components/header.jsp"/>
<div class="errorContainer">
    <div class="alert alert-danger">
        <h1>Error 404</h1>
        <h4>${error}</h4>
    </div>
</div>
</body>
</html>

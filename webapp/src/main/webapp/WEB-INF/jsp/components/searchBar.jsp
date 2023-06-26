<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/css/main.css" var="mainCss"/>

<c:url value="/doctor-dashboard" var="doctorDashboardUrl"/>

<spring:message code="doctorDashboard.placeholder.search" var="search"/>

<html>
<head>
    <link href="${mainCss}" rel="stylesheet"/>
</head>
<body>
<form method="get" action="${doctorDashboardUrl}">
    <div class="d-flex">
        <input type="text" id="input" class="form-control" name="name" placeholder="${search}"
               value="${param.searchValue}">
        <button type="submit" class="btn btn-primary search">
            <i class="fas fa-search"></i>
        </button>
    </div>
</form>
</body>
</html>

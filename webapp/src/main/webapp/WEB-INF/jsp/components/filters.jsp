<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="cityMap" scope="request" type="java.util.Map"/>
<jsp:useBean id="specialtyMap" scope="request" type="java.util.Map"/>
<jsp:useBean id="healthInsuranceMap" scope="request" type="java.util.Map"/>

<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/filters.css" var="filtersCss"/>
<c:url value="/js/filters.js" var="filtersJs"/>
<c:url value="/doctorDashboard" var="doctorDashboardUrl"/>

<spring:message code="doctorDashboard.placeholder.city" var="city"/>
<spring:message code="doctorDashboard.placeholder.specialty" var="specialty"/>
<spring:message code="doctorDashboard.placeholder.insurance" var="insurance"/>
<spring:message code="doctorDashboard.button.filter" var="filter"/>
<spring:message code="doctorDashboard.placeholder.search" var="search"/>
<spring:message code="filters.clear" var="clear"/>


<html>
<head>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${filtersCss}" rel="stylesheet"/>
    <script src="${filtersJs}"></script>
</head>
<body>


</body>
</html>


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
<form:form id="filter-form" modelAttribute="doctorFilterForm" method="get" action="${doctorDashboardUrl}">
    <div class="searchBarContainer">
        <form:input id="name-input" type="text" cssClass="form-control" placeholder="${search}" path="name"/>
        <button type="submit" class="btn btn-primary search">
            <i class="fas fa-search"></i>
        </button>
    </div>

    <div class="filtersContainer">
        <form:select id="city-select" cssClass="form-select" path="cityCode">
            <form:option value="-1" disabled="true" hidden="true"> ${city} </form:option>
            <c:forEach items="${cityMap}" var="city">
                <form:option value="${city.key.ordinal()}">
                    <spring:message code="${city.key.messageID}"/> (${city.value})
                </form:option>
            </c:forEach>
        </form:select>

        <form:select id="specialty-select" cssClass="form-select" path="specialtyCode">
            <form:option value="-1" disabled="true" hidden="true"> ${specialty} </form:option>
            <c:forEach items="${specialtyMap}" var="specialty">
                <form:option value="${specialty.key.ordinal()}">
                    <spring:message code="${specialty.key.messageID}"/> (${specialty.value})
                </form:option>
            </c:forEach>
        </form:select>

        <form:select id="health-insurance-select" cssClass="form-select" path="healthInsuranceCode">
            <form:option value="-1" disabled="true" hidden="true"> ${insurance} </form:option>
            <c:forEach items="${healthInsuranceMap}" var="healthInsurance">
                <form:option value="${healthInsurance.key.ordinal()}">
                    <spring:message code="${healthInsurance.key.messageID}"/> (${healthInsurance.value})
                </form:option>
            </c:forEach>
        </form:select>

        <input type="submit" class="btn btn-danger" value="${clear}" onclick="clearFilters()">
        <input type="submit" class="btn btn-primary" value="${filter}">
    </div>
</form:form>
</body>
</html>


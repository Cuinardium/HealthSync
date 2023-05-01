<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/doctorDashboard.css" var="doctorDashboardCss"/>

<c:url value="/doctorDashboard" var="doctorDashboardUrl"/>

<c:url value="/img/doctorCardDefault.jpg" var="doctorCardDefaultImg"/>

<spring:message code="doctorDashboard.title" var="title"/>
<spring:message code="doctorDashboard.placeholder.city" var="city"/>
<spring:message code="doctorDashboard.placeholder.specialty" var="specialty"/>
<spring:message code="doctorDashboard.placeholder.insurance" var="insurance"/>
<spring:message code="doctorDashboard.button.filter" var="filter"/>
<spring:message code="doctorDashboard.button.book" var="book"/>
<spring:message code="doctorDashboard.no.doctors" var="noDoctors"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${doctorDashboardCss}" rel="stylesheet"/>
</head>

<body>
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<div class="page-content generalPadding">

    <!-- Search Bar -->
    <jsp:include page="../components/searchBar.jsp"/>

    <form method="get" action="${doctorDashboardUrl}">
        <div class="filtersContainer">
            <input type="text" class="form-control" id="city" name="city" placeholder="${city}"/>
            <input type="text" class="form-control" id="specialty" name="specialty" placeholder="${specialty}">
            <input type="text" class="form-control" id="healthcare" name="healthcare" placeholder="${insurance}">
            <input type="submit" class="btn btn-primary" value="${filter}">
        </div>
    </form>

    <div class="cardsContainer">
        <c:forEach items="${doctors}" var="doctor">
            <c:url value="/${doctor.id}/appointment" var="appointmentUrl"/>
            <c:url value="/${doctor.id}/detailed_doctor" var="detailedUrl"/>
            <div class="card">
                <div class="imageContainer">
                    <img src="${doctorCardDefaultImg}"
                         class="card-img-top" alt="A blonde dermatologist">
                </div>
                <div class="infoContainer">
                    <div class="card-body">
                        <h5 class="card-title"><a href="${detailedUrl}">${doctor.firstName} ${doctor.lastName}</a></h5>
                        <p class="card-text">${doctor.specialty}. ${doctor.address}, ${doctor.city}</p>
                        <p class="card-text">${doctor.healthInsurance}</p>
                    </div>
                </div>
                <div class="buttonsContainer">
                    <div class="card-body">
                        <a href="${appointmentUrl}" class="btn btn-primary">${book}</a>
                    </div>
                </div>
            </div>
            
        </c:forEach>
        <c:if test="${doctors.isEmpty()}">
            <div class="d-flex justify-content-center">
                <!-- TODO: style this vvvv -->
                <h4>${noDoctors}</h4>
            </div>
        </c:if>
    </div>
</div>
<script src="${searchBarJs}"></script>
</body>
</html>



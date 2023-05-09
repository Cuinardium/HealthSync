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
    <jsp:include page="../components/searchBar.jsp">
        <jsp:param name="searchValue" value="${name}"/>
    </jsp:include>

    <form method="get" action="${doctorDashboardUrl}">
        <div class="filtersContainer">
            <select class="form-select" name="cityCode">
                <option value="" selected disabled hidden> -- </option>
                <c:forEach items="${cityMap}" var="city">
                    <option value="${city.key.ordinal()}" ${city.key.ordinal() == cityCode? 'selected':''}>
                      <spring:message code="${city.key.messageID}"/> (${city.value})
                    </option>
                </c:forEach>
            </select>

            <select class="form-select" name="specialtyCode">
                <option value="" selected disabled hidden> -- </option>
                <c:forEach items="${specialtyMap}" var="specialty">
                    <option value="${specialty.key.ordinal()}" ${specialty.key.ordinal() == specialtyCode? 'selected':''}>
                        <spring:message code="${specialty.key.messageID}"/> (${specialty.value})
                    </option>
                </c:forEach>
            </select>

            <select class="form-select" name="healthInsuranceCode">
                <option value="" selected disabled hidden> -- </option>
                <c:forEach items="${healthInsuranceMap}" var="healthInsurance">
                    <option value="${healthInsurance.key.ordinal()}" ${healthInsurance.key.ordinal() == healthInsuranceCode? 'selected':''}>
                        <spring:message code="${healthInsurance.key.messageID}"/> (${healthInsurance.value})
                    </option>
                </c:forEach>
            </select>

            <input type="submit" class="btn btn-primary" value="${filter}">
        </div>
    </form>

    <div class="cardsContainer">
        <c:forEach items="${doctors}" var="doctor">
            <c:url value="/${doctor.id}/appointment" var="appointmentUrl"/>
            <spring:message code="${doctor.specialty.messageID}" var="doctorSpecialty"/>
            <spring:message code="${doctor.location.city.messageID}" var="doctorCity"/>
            <spring:message code="${doctor.healthInsurance.messageID}" var="healthInsurance"/>
            <c:url value="/${doctor.id}/detailed_doctor" var="detailedUrl"/>
            <div class="card">
                <div class="imageContainer">
                    <img src="${doctorCardDefaultImg}"
                         class="card-img-top" alt="A blonde dermatologist">
                </div>
                <div class="infoContainer">
                    <div class="card-body">
                        <h5 class="card-title"><a href="${detailedUrl}">${doctor.firstName} ${doctor.lastName}</a></h5>
                        <p class="card-text">${doctorSpecialty}. ${doctor.location.address}, ${doctorCity}</p>
                        <p class="card-text">${healthInsurance}</p>
                    </div>
                </div>
                <c:if test="${canBook}">
                    <div class="buttonsContainer">
                        <div class="card-body">
                            <a href="${appointmentUrl}" class="btn btn-primary">${book}</a>
                        </div>
                    </div>
                </c:if>
            </div>

        </c:forEach>
        <c:if test="${empty doctors}">
            <div class="noDoctorsMsg">
                <div class="alert alert-info">${noDoctors}</div>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>



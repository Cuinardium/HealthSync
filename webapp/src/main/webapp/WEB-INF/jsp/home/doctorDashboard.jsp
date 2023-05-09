<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/doctorDashboard.css" var="doctorDashboardCss"/>

<c:url value="/img/doctorCardDefault.jpg" var="doctorCardDefaultImg"/>

<spring:message code="doctorDashboard.title" var="title"/>

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

    <c:set var="cityMap" value="${cityMap}" scope="request"/>
    <c:set var="specialtyMap" value="${specialtyMap}" scope="request"/>
    <c:set var="healthInsuranceMap" value="${healthInsuranceMap}" scope="request"/>
    <jsp:include page="../components/filters.jsp"/>

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


        <c:url value="/doctorDashboard" var="doctorDashboardUrl">
            <c:param name="name" value="${name}"/>
            <c:param name="cityCode" value="${cityCode}"/>
            <c:param name="specialtyCode" value="${specialtyCode}"/>
            <c:param name="healthInsuranceCode" value="${healthInsuranceCode}"/>
        </c:url>
        <jsp:include page="../components/pagination.jsp">
            <jsp:param name="currentPage" value="${currentPage}"/>
            <jsp:param name="totalPages" value="${totalPages}"/>
            <jsp:param name="url" value="${doctorDashboardUrl}"/>
        </jsp:include>
    </div>
</div>
</body>
</html>



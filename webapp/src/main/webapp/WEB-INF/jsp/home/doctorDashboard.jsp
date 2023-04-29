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
<spring:message code="doctorDashboard.placeholder.search" var="search"/>
<spring:message code="doctorDashboard.placeholder.city" var="city"/>
<spring:message code="doctorDashboard.placeholder.specialty" var="specialty"/>
<spring:message code="doctorDashboard.placeholder.insurance" var="insurance"/>
<spring:message code="doctorDashboard.button.filter" var="filter"/>
<spring:message code="doctorDashboard.button.book" var="book"/>
<spring:message code="doctorDashboard.no.doctors" var="noDoctors"/>

<jsp:include page="/resources/externalResources.jsp"/>


<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${doctorDashboardCss}" rel="stylesheet"/>
</head>

<body>
<!-- NavBar-->
<jsp:include page="../components/navBar.jsp"/>
<!-- Content -->
<div class="page-content p-5" id="content">
    <div class="row">
        <!-- Search Bar -->
        <div class="input-group">
            <input type="text" id="input" class="form-control" placeholder="${search}" aria-label="Search"
                   aria-describedby="basic-addon2">
            <button type="button" class="btn btn-primary" onclick="search();">
                <i class="fas fa-search"></i>
            </button>
        </div>
    </div>
    <form method="get" id="filters" action="${doctorDashboardUrl}">
        <div class="row pt-3">
            <div class="col">
                <input type="number" class="form-control" id="cityCode" name="cityCode" placeholder="${city}"/>
            </div>

            <div class="col">
                <input type="number" class="form-control" id="specialtyCode" name="specialtyCode" placeholder="${specialty}">

            </div>
            <div class="col">
                <input type="number" class="form-control" id="healthInsuranceCode" name="healthInsuranceCode" placeholder="${insurance}">

            </div>
            <div class="col-auto">
                <input type="submit" class="btn btn-primary" value="${filter}">
            </div>
        </div>
    </form>
    <div class="flex-container bcontent">
        <c:forEach items="${doctors}" var="doctor">
            <c:url value="/${doctor.id}/appointment" var="appointmentUrl"/>
            <spring:message code="${doctor.specialty.messageID}" var="doctorSpecialty"/>
            <spring:message code="${doctor.location.city.messageID}" var="doctorCity"/>
            <div class="card">
                <div class="row g-0">
                    <div class="col-sm-5">
                        <img src="${doctorCardDefaultImg}"
                             class="card-img-top" alt="A blonde dermatologist">
                    </div>
                    <div class="col-sm-7">
                        <div class="card-body">
                            <h5 class="card-title">${doctor.firstName} ${doctor.lastName}</h5>
                            <p class="card-text">${doctorSpecialty}. ${doctor.location.address}, ${doctorCity}</p>
                            <p class="card-text">${doctor.healthInsurance}</p>
                            <a href="${appointmentUrl}" class="btn btn-primary">${book}</a>
                        </div>
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
</body>
<script>
    let search = () => {
        let element = document.getElementById("input").value;
        window.location = '${doctorDashboardUrl}?name=' + element;
    }
</script>
</html>

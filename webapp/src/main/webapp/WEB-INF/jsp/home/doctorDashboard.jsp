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
<spring:message code="doctorDashboard.modal.title" var="modalTitle"/>
<spring:message code="doctorDashboard.modal.desc" var="modalDesc"/>
<spring:message code="doctorDashboard.modal.confirm" var="modalConfirm"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${doctorDashboardCss}" rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
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
                            <a class="btn btn-primary" onclick="checkInsurance('${appointmentUrl}', '${doctor.healthInsurance}')">${book}</a>
                        </div>
                    </div>
                    <div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="modalLabel">${modalTitle}</h5>
                                </div>
                                <div class="modal-body">
                                    ${modalDesc}
                                </div>
                                <div class="modal-footer">
                                    <a type="button" id="modal-href" class="btn btn-primary">${modalConfirm}</a>
                                </div>
                            </div>
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
<script>


    function checkInsurance(appointmentUrl, object){
        if(object=='${patientHealthInsurance}' || ${notLogged}){
            redirect(appointmentUrl);
        }
        else{
            $('#modal').modal('show');
            $('#modal-href').attr('href', appointmentUrl);

        }
    }

    function redirect(appointmentUrl){
        window.location.replace(appointmentUrl);
    }
</script>






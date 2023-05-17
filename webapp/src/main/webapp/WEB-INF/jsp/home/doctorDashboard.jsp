<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/doctorDashboard.css" var="doctorDashboardCss"/>
<c:url value="/css/filters.css" var="filtersCss"/>
<c:url value="/js/filters.js" var="filtersJs"/>

<c:url value="/doctorDashboard" var="doctorDashboardUrl"/>

<spring:message code="doctorDashboard.title" var="title"/>

<spring:message code="doctorDashboard.button.book" var="book"/>
<spring:message code="doctorDashboard.no.doctors" var="noDoctors"/>
<spring:message code="doctorDashboard.modal.title" var="modalTitle"/>
<spring:message code="doctorDashboard.modal.desc" var="modalDesc"/>
<spring:message code="doctorDashboard.modal.confirm" var="modalConfirm"/>
<spring:message code="doctorDashboard.modal.deny" var="modalDeny"/>
<spring:message code="doctorDashboard.placeholder.city" var="city"/>
<spring:message code="doctorDashboard.placeholder.specialty" var="specialty"/>
<spring:message code="doctorDashboard.placeholder.insurance" var="insurance"/>
<spring:message code="doctorDashboard.placeholder.search" var="search"/>
<spring:message code="doctorDashboard.placeholder.from" var="fromTitle"/>
<spring:message code="doctorDashboard.placeholder.to" var="toTitle"/>
<spring:message code="filters.title" var="filtersTitle"/>
<spring:message code="filters.clear" var="clear"/>
<spring:message code="form.date" var="date"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${doctorDashboardCss}" rel="stylesheet"/>
    <link href="${filtersCss}" rel="stylesheet"/>

    <script src="${filtersJs}"></script>

    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
            integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js"
            integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js"
            integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
            crossorigin="anonymous"></script>
</head>

<body>
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<form:form id="filter-form" modelAttribute="doctorFilterForm" method="get" action="${doctorDashboardUrl}">
    <div class="dashboardContainer">
        <div class="sidebarContainer">
            <div class="filtersContainer">
                <div class="titleFilterContainer">
                    <h2>${filtersTitle}</h2>
                    <input type="submit" class="btn btn-danger" value="${clear}" onclick="clearFilters()">
                </div>
                <hr>
                <form:select id="city-select" cssClass="form-select" path="cityCode" onchange="this.form.submit()">
                    <form:option value="-1" disabled="true" hidden="true"> ${city} </form:option>
                    <c:forEach items="${cityMap}" var="city">
                        <form:option value="${city.key.ordinal()}">
                            <spring:message code="${city.key.messageID}"/> (${city.value})
                        </form:option>
                    </c:forEach>
                </form:select>

                <form:select id="specialty-select" cssClass="form-select" path="specialtyCode" onchange="this.form.submit()">
                    <form:option value="-1" disabled="true" hidden="true"> ${specialty} </form:option>
                    <c:forEach items="${specialtyMap}" var="specialty">
                        <form:option value="${specialty.key.ordinal()}">
                            <spring:message code="${specialty.key.messageID}"/> (${specialty.value})
                        </form:option>
                    </c:forEach>
                </form:select>

                <form:select id="health-insurance-select" cssClass="form-select" path="healthInsuranceCode" onchange="this.form.submit()">
                    <form:option value="-1" disabled="true" hidden="true"> ${insurance} </form:option>
                    <c:forEach items="${healthInsuranceMap}" var="healthInsurance">
                        <form:option value="${healthInsurance.key.ordinal()}">
                            <spring:message code="${healthInsurance.key.messageID}"/> (${healthInsurance.value})
                        </form:option>
                    </c:forEach>
                </form:select>

                <form:input id="date" type="date" cssClass="form-control" placeholder="${date}" path="date" onchange="this.form.submit()"/>

                <c:if test="${dateFilter != null}">
                    <h4>${fromTitle}</h4>
                    <form:select id="from-select" cssClass="form-select" path="from" onchange="this.form.submit()">
                        <form:option value="-1" disabled="true" hidden="true"/>
                        <c:forEach items="${possibleAttendingHours}" var="attHour">
                            <c:if test="${toBlock.ordinal() > attHour.ordinal()}">
                                <form:option value="${attHour.ordinal()}">
                                    ${attHour.getBlockBeginning()}
                                </form:option>
                            </c:if>
                        </c:forEach>
                    </form:select>
                </c:if>


                <c:if test="${dateFilter != null}">
                    <h4>${toTitle}</h4>
                    <form:select id="to-select" cssClass="form-select" path="to" onchange="this.form.submit()">
                        <form:option value="-1" disabled="true" hidden="true"/>
                        <c:forEach items="${possibleAttendingHours}" var="attHour">
                            <c:if test="${fromBlock.ordinal() < attHour.ordinal()}">
                                <form:option value="${attHour.ordinal()}">
                                    ${attHour.getBlockBeginning()}
                                </form:option>
                            </c:if>
                        </c:forEach>
                    </form:select>
                </c:if>
            </div>
        </div>

        <div class="discoveryContainer generalPadding">
            <c:set var="cityMap" value="${cityMap}" scope="request"/>
            <c:set var="specialtyMap" value="${specialtyMap}" scope="request"/>
            <c:set var="healthInsuranceMap" value="${healthInsuranceMap}" scope="request"/>

            <div class="searchBarContainer">
                <form:input id="name-input" type="text" cssClass="form-control" placeholder="${search}" path="name"/>
                <button type="submit" class="btn btn-primary search">
                    <i class="fas fa-search"></i>
                </button>
            </div>

            <div class="cardsContainer">
                <c:forEach items="${doctors}" var="doctor">
                    <spring:message code="${doctor.specialty.messageID}" var="doctorSpecialty"/>
                    <spring:message code="${doctor.location.city.messageID}" var="doctorCity"/>
                    <c:url value="/${doctor.id}/detailed_doctor" var="detailedUrl"/>
                    <div class="card">
                        <div class="imageContainer">
                            <c:url value="/img/${doctor.getProfilePictureId() == null ? \"doctorDefault.png\" : doctor.getProfilePictureId()}"
                                   var="doctorImg"/>
                            <img src="${doctorImg}" class="card-img-top">
                        </div>
                        <div class="infoContainer">
                            <div class="card-body">
                                <h5 class="card-title"><a
                                        href="${detailedUrl}">${doctor.firstName} ${doctor.lastName}</a>
                                </h5>
                                <p class="card-text">${doctorSpecialty}. ${doctor.location.address}, ${doctorCity}</p>

                                <p class="card-text">
                                    <c:forEach items="${doctor.healthInsurances}" var="healthInsurance"
                                               varStatus="status">
                                        <spring:message code="${healthInsurance.messageID}" var="healthInsuranceMsg"/>
                                        ${healthInsuranceMsg}${status.last ? "" : ", "}
                                    </c:forEach>
                                </p>
                            </div>
                        </div>
                        <c:if test="${canBook}">
                            <div class="buttonsContainer">
                                <div class="card-body">
                                    <a class="btn btn-primary"
                                       onclick="checkInsurance(
                                               '${detailedUrl}',
                                               [<c:forEach items="${doctor.healthInsurances}" var="healthInsurance"
                                                           varStatus="status">
                                           ${healthInsurance.ordinal()}${status.last ? "" : ", "}
                                       </c:forEach>])">
                                            ${book}
                                    </a>
                                </div>
                            </div>
                            <div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="modalLabel"
                                 aria-hidden="true">
                                <div class="modal-dialog" role="dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="modalLabel">${modalTitle}</h5>
                                        </div>
                                        <div class="modal-body">
                                                ${modalDesc}
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-danger"
                                                    onclick="closeModal()">${modalDeny}</button>
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


                <c:url value="/doctorDashboard" var="doctorDashboardFilteredUrl">
                    <c:param name="name" value="${name}"/>
                    <c:param name="cityCode" value="${cityCode}"/>
                    <c:param name="specialtyCode" value="${specialtyCode}"/>
                    <c:param name="healthInsuranceCode" value="${healthInsuranceCode}"/>
                    <c:param name="date" value="${dateFilter}"/>
                    <c:param name="from" value="${fromBlock.ordinal()}"/>
                    <c:param name="to" value="${toBlock.ordinal()}"/>
                </c:url>
                <jsp:include page="../components/pagination.jsp">
                    <jsp:param name="currentPage" value="${currentPage}"/>
                    <jsp:param name="totalPages" value="${totalPages}"/>
                    <jsp:param name="url" value="${doctorDashboardFilteredUrl}"/>
                </jsp:include>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>
<script>


    function checkInsurance(appointmentUrl, object) {
        if (object.includes(${patientHealthInsurance.ordinal()}) || ${notLogged}) {
            redirect(appointmentUrl);
        } else {
            $('#modal').modal('show');
            $('#modal-href').attr('href', appointmentUrl);

        }
    }

    function closeModal() {
        $('#modal').modal('hide')
    }

    function redirect(appointmentUrl) {
        window.location.href = appointmentUrl;
    }
</script>






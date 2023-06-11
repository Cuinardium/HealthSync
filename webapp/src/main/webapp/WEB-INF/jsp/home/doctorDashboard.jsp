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

<c:url value="/doctor-dashboard" var="doctorDashboardUrl"/>
<c:url value="/doctor-dashboard?specialtyCode=" var="specialtyFilter"/>
<c:url value="/doctor-dashboard?healthInsuranceCode=" var="healthInsuranceFilter"/>

<spring:message code="doctorDashboard.title" var="title"/>

<spring:message code="detailedDoctor.title" var="title"/>
<spring:message code="detailedDoctor.specialties" var="specialties"/>
<spring:message code="detailedDoctor.address" var="address"/>
<spring:message code="detailedDoctor.insurance" var="insurances"/>
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
<spring:message code="doctor.noReviews" var="noReviews"/>
<spring:message code="filters.byAvailability" var="byAvailability"/>
<spring:message code="review.rating" var="ratingTitle"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${doctorDashboardCss}" rel="stylesheet"/>
    <link href="${filtersCss}" rel="stylesheet"/>

    <script src="${filtersJs}"></script>

</head>

<body>
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<form:form id="filter-form" modelAttribute="doctorFilterForm" method="get" action="${doctorDashboardUrl}">
    <div class="dashboardContainer">
        <div class="sidebarContainer card">
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

                <form:select id="specialty-select" cssClass="form-select" path="specialtyCode"
                             onchange="this.form.submit()">
                    <form:option value="-1" disabled="true" hidden="true"> ${specialty} </form:option>
                    <c:forEach items="${specialtyMap}" var="specialty">
                        <form:option value="${specialty.key.ordinal()}">
                            <spring:message code="${specialty.key.messageID}"/> (${specialty.value})
                        </form:option>
                    </c:forEach>
                </form:select>

                <form:select id="health-insurance-select" cssClass="form-select" path="healthInsuranceCode"
                             onchange="this.form.submit()">
                    <form:option value="-1" disabled="true" hidden="true"> ${insurance} </form:option>
                    <c:forEach items="${healthInsuranceMap}" var="healthInsurance">
                        <form:option value="${healthInsurance.key.ordinal()}">
                            <spring:message code="${healthInsurance.key.messageID}"/> (${healthInsurance.value})
                        </form:option>
                    </c:forEach>
                </form:select>

                <h5>${ratingTitle}</h5>

                <form:input id="min-rating" type="text" path="minRating" onchange="this.form.submit()" hidden="true"/>
                <div id="rating-selector" class="starContainer">
                    <c:forEach begin="1" end="5" step="1" var="i">
                        <div class="star ${minRating >= i ? "selected" : "unselected"}" data-index="${i}">
                            <i class="fa fa-lg fa-star"></i>
                        </div>
                    </c:forEach>
                </div>


                <hr>
                <h4>${byAvailability}</h4>

                <form:input id="date" type="date" cssClass="form-control" placeholder="${date}" path="date"
                            onchange="this.form.submit()"/>

                <c:if test="${!empty dateFilter}">
                    <div class="fromToContainer">
                        <div class="hourPicker">
                            <h5>${fromTitle}</h5>
                            <form:select id="from-select" cssClass="form-select" path="from"
                                         onchange="this.form.submit()">
                                <form:option value="-1" disabled="true" hidden="true"/>
                                <c:forEach items="${possibleAttendingHours}" var="attHour">
                                    <c:if test="${toBlock.ordinal() >= attHour.ordinal()}">
                                        <form:option value="${attHour.ordinal()}">
                                            ${attHour.getBlockBeginning()}
                                        </form:option>
                                    </c:if>
                                </c:forEach>
                            </form:select>
                        </div>

                        <div class="hourPicker">
                            <h5>${toTitle}</h5>
                            <form:select id="to-select" cssClass="form-select" path="to" onchange="this.form.submit()">
                                <form:option value="-1" disabled="true" hidden="true"/>
                                <c:forEach items="${possibleAttendingHours}" var="attHour">
                                    <c:if test="${fromBlock.ordinal() <= attHour.ordinal()}">
                                        <form:option value="${attHour.ordinal()}">
                                            ${attHour.blockEnd}
                                        </form:option>
                                    </c:if>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                </c:if>


            </div>
        </div>

        <div class="discoveryContainer">
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
                    <spring:message code="${doctor.city.messageID}" var="doctorCity"/>
                    <spring:message code="doctor.alt.doctorImg" arguments="${doctor.firstName}, ${doctor.lastName}"
                                    var="altDoctorImg"/>
                    <c:url value="/${doctor.id}/detailed-doctor" var="detailedUrl"/>
                    <div class="card">
                        <div class="imageContainer">
                            <c:url value="/img/${doctor.getImage() == null ? \"doctorDefault.png\" : doctor.getImage().getImageId()}"
                                   var="doctorImg"/>
                            <img src="${doctorImg}" class="card-img-top" alt="${altDoctorImg}">
                        </div>
                        <div class="infoContainer">
                            <h5 class="card-title">${doctor.firstName} ${doctor.lastName}</h5>
                            <div class="chipsContainer">
                                <div class="card-text">${specialties}:</div>
                                <div class="chip" data-mdb-close="true">
                                    <a class="stretched-link"
                                       href="${specialtyFilter}${doctor.specialty.ordinal()}">${doctorSpecialty}</a>
                                </div>
                            </div>
                            <div class="card-text">${address} ${doctor.address}, ${doctorCity}</div>
                            <div class="chipsContainer">
                                <div class="card-text">${insurances}:</div>
                                <c:forEach items="${doctor.healthInsurances}" var="healthInsurance">
                                    <spring:message code="${healthInsurance.messageID}"
                                                    var="doctorHealthInsurance"/>
                                    <div class="chip" data-mdb-close="true">
                                        <a class="stretched-link"
                                           href="${healthInsuranceFilter}${healthInsurance.ordinal()}">${doctorHealthInsurance}</a>
                                    </div>
                                </c:forEach>
                            </div>
                            <c:choose>
                                <c:when test="${doctor.rating != null}">
                                    <div class="starContainer card-text">
                                        <c:forEach begin="1" end="5" step="1" var="i">
                                            <div class="star ${doctor.rating >= i ? "selected" : "unselected"}">
                                                <i class="fa fa-lg fa-star"></i>
                                            </div>
                                        </c:forEach>
                                        <div class="ratingCount">
                                            (${doctor.ratingCount})
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="card-text">
                                            ${noReviews}
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <c:if test="${canBook}">
                                <div class="buttonsContainer">
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
                                <div class="modal fade" id="modal" tabindex="-1" role="dialog"
                                     aria-labelledby="modalLabel"
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
                                                <a type="button" id="modal-href"
                                                   class="btn btn-primary">${modalConfirm}</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>

                </c:forEach>
                <c:if test="${empty doctors}">
                    <div class="noDoctorsMsg">
                        <div class="alert alert-info">${noDoctors}</div>
                    </div>
                </c:if>


                <jsp:include page="../components/pagination.jsp">
                    <jsp:param name="currentPage" value="${currentPage}"/>
                    <jsp:param name="totalPages" value="${totalPages}"/>
                    <jsp:param name="url"
                               value="/doctor-dashboard?name=${name}&cityCode=${cityCode}&specialtyCode=${specialtyCode}&date=${dateFilter}&from=${fromBlock.ordinal()}&to=${toBlock.ordinal()}"/>
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






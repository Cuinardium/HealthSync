<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/detailedAppointment.css" var="detailedAppointmentCss"/>


<c:url value="/my-appointments" var="myAppointmentsUrl">
    <c:param name="selected_tab" value="${selectedTab}"/>
</c:url>

<c:url value="/${appointment.doctorId}/detailed-doctor" var="detailedDoctorUrl"/>

<spring:message code="${appointment.doctor.city.messageID}" var="city"/>
<spring:message code="${appointment.patient.healthInsurance.messageID}" var="healthInsurance"/>
<spring:message code="${appointment.status.messageID}" var="status"/>

<spring:message code="detailedAppointment.title" var="title"/>
<spring:message code="detailedAppointment.doctor" var="doctor"/>
<spring:message code="detailedAppointment.patient" var="patient"/>
<spring:message code="detailedAppointment.address" var="address"/>
<spring:message code="detailedAppointment.healthInsurance" var="healthInsurance_title"/>
<spring:message code="detailedAppointment.status" var="statusTitle"/>
<spring:message code="detailedAppointment.description" var="description"/>
<spring:message code="detailedAppointment.cancelDesc" var="cancelDescriptionTitle"/>
<spring:message code="detailedAppointment.indication" var="indication"/>
<spring:message code="appointments.button.appointment" var="bookAppointment"/>
<spring:message code="appointments.modal.title" var="modalTitle"/>
<spring:message code="appointments.modal.desc" var="modalDesc"/>
<spring:message code="appointments.modal.confirm" var="modalConfirm"/>
<spring:message code="appointments.modal.deny" var="modalDeny"/>
<spring:message code="appointments.modal.cancelDesc" var="cancelDesc"/>
<spring:message code="appointments.indicationModal.title" var="indicationModalTitle"/>
<spring:message code="appointments.indicationModal.desc" var="indicationModalDesc"/>
<spring:message code="appointments.indicationModal.confirm" var="indicationModalConfirm"/>
<spring:message code="appointments.indicationModal.deny" var="indicationModalDeny"/>
<spring:message code="appointments.indicationModal.indication" var="indicationDesc"/>
<spring:message code="appointments.noIndications" var="noIndications"/>
<spring:message code="appointments.indication.title" var="indicationsTitle"/>
<spring:message code="appointments.indication.button" var="indicationButton"/>
<spring:message code="detailedAppointment.me" var="me"/>


<html>
<head>
    <title>${title}</title>

    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${detailedAppointmentCss}" rel="stylesheet"/>

</head>
<body>
<jsp:include page="../components/header.jsp"/>

<div class="generalPadding">
    <div class="backButtonContainer">
        <a href="${myAppointmentsUrl}" class="btn btn-primary backButton">
            <i class="fa-solid fa-arrow-left"></i>
        </a>
    </div>
    <div class="card">

        <div class="card-header">
            <strong>${appointment.date} ${appointment.timeBlock.blockBeginning}</strong>
            <strong>${statusTitle}: ${status}</strong>
        </div>
        <div class="card-body">


            <div class="card-title">
                <div class="card-title">
                    <strong>${patient}: </strong>${appointment.patient.firstName} ${appointment.patient.lastName}
                </div>
            </div>

            <div class="card-title">
                <strong>${doctor}: </strong>${appointment.doctor.firstName} ${appointment.doctor.lastName}</div>

            <div class="card-title"><strong>${address}: </strong>${address}, ${city}</div>


            <div class="card-title"><strong>${healthInsurance_title}: </strong>${healthInsurance}</div>
            <div class="card-title"><strong>${description}: </strong>${appointment.description}</div>
            <c:if test="${not empty appointment.cancelDesc}">
                <div class="card-title"><strong>${cancelDescriptionTitle}: </strong>${appointment.cancelDesc}</div>
            </c:if>
            <div class="cardButtonContainer">
                <c:if test="${(appointment.status == 'COMPLETED' || appointment.status == 'CANCELLED') && !isDoctor}">
                    <a href="${detailedDoctorUrl}">
                        <button type="button" class="btn btn-primary">${bookAppointment}</button>
                    </a>
                </c:if>
                <c:if test="${appointment.status == 'CONFIRMED'}">

                    <c:url value="/my-appointments/${appointment.id}/cancel" var="cancelUrl">
                        <c:param name="selected_tab" value="${selectedTab}"/>
                    </c:url>

                    <button onclick="openModal('${cancelUrl}')"
                            class="post-button btn btn-danger">
                        <spring:message code="appointments.cancel"/>
                    </button>
                    <div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="modalLabel"
                         aria-hidden="true">
                        <div class="modal-dialog" role="dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="modalLabel">${modalTitle}</h5>
                                </div>
                                <form:form modelAttribute="modalForm" id="post-modal">
                                    <div class="modal-body">

                                            ${modalDesc}
                                        <div class="form-group">
                                            <form:label path="description" for="cancelDescription"
                                                        class="col-form-label">${cancelDesc}</form:label>
                                            <form:input path="description" class="form-control" id="cancelDescription"/>
                                        </div>

                                    </div>
                                    <div class="modal-footer">

                                        <div class="cardButtonContainer">
                                            <button type="button" class="btn btn-danger"
                                                    onclick="closeModal()">${modalDeny}</button>
                                            <button type="submit" class="btn btn-primary">${modalConfirm}</button>
                                        </div>

                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    <c:if test="${appointment.status=='COMPLETED'}">
    <div class="indicationsHeader">
        <h3>${empty indications ? noIndications : indicationsTitle}</h3>


        <c:url value="/${appointment.id}/indication" var="indicationUrl"/>
        <a href="${indicationUrl}" class="btn btn-outline-primary detailed-link">
            ${indicationButton}
        </a>

    </div>
    <c:if test="${not empty indications}">
        <div>
            <c:forEach items="${indications}" var="indication">
                <div class="card indicationCard">
                    <div class="card-body">
                        <div class="card-text cardDescription">
                                ${indication.description}
                        </div>
                    </div>

                    <div class="cardFooter">
                        <c:url value="/img/${indication.user.image == null ? \"patientDefault.png\" : indication.user.image.imageId}"
                               var="userImg"/>
                        <spring:message code="patient.alt.patientImg"
                                        arguments="${indication.user.firstName}, ${indication.user.lastName}"
                                        var="altUserImg"/>
                        <img src="${userImg}" alt="${altUserImg}" width="50" height="50"
                             class="rounded-circle">
                        <div class="cardNameDate">
                            <c:choose>
                                <c:when test="${indication.user.id==user.id}">
                                    <strong>${me}</strong>
                                </c:when>
                                <c:otherwise>
                                    <strong>${indication.user.firstName} ${indication.user.lastName}</strong>
                                </c:otherwise>
                            </c:choose>
                                ${indication.date}
                        </div>

                    </div>
                </div>
            </c:forEach>

            <jsp:include page="../components/pagination.jsp">
                <jsp:param name="currentPage" value="${currentPage}"/>
                <jsp:param name="totalPages" value="${totalPages}"/>
                <jsp:param name="url" value="/${appointment.id}/detailed-appointment"/>
            </jsp:include>
        </div>
    </c:if>
    </c:if>
</div>
</body>
</html>
<script>
    function openModal(action) {
        $('#modal').modal('show');
        $('#post-modal').attr('action', action);
    }


    function closeModal() {
        $('#modal').modal('hide')
    }
</script>

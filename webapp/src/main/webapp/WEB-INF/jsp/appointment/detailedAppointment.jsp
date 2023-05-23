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

<spring:message code="${appointment.doctor.location.city.messageID}" var="city"/>
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
<spring:message code="appointments.modal.title" var="modalTitle"/>
<spring:message code="appointments.modal.desc" var="modalDesc"/>
<spring:message code="appointments.modal.confirm" var="modalConfirm"/>
<spring:message code="appointments.modal.deny" var="modalDeny"/>
<spring:message code="appointments.modal.cancelDesc" var="cancelDesc"/>

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

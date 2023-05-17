<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/appointments.css" var="appointmentsCss"/>
<c:url value="/my-appointments" var="myAppointmentUrl"/>

<c:url value="/js/myAppointments.js" var="myAppointmentsJs"/>

<spring:message code="appointments.title" var="title"/>
<spring:message code="appointments.noAppointments" var="noAppointments"/>
<spring:message code="appointments.from" var="fromTitle"/>
<spring:message code="appointments.to" var="toTitle"/>
<spring:message code="appointments.filter" var="filter"/>
<spring:message code="detailedAppointment.status" var="statusTitle"/>
<spring:message code="appointments.modal.title" var="modalTitle"/>
<spring:message code="appointments.modal.desc" var="modalDesc"/>
<spring:message code="appointments.modal.confirm" var="modalConfirm"/>
<spring:message code="appointments.modal.deny" var="modalDeny"/>
<spring:message code="appointments.modal.cancelDesc" var="cancelDesc"/>

<spring:message code="detailedAppointment.title" var="title"/>
<spring:message code="detailedAppointment.doctor" var="doctor"/>
<spring:message code="detailedAppointment.patient" var="patient"/>
<spring:message code="detailedAppointment.address" var="address"/>
<spring:message code="detailedAppointment.healthInsurance" var="healthInsurance_title"/>
<spring:message code="detailedAppointment.status" var="statusTitle"/>
<spring:message code="detailedAppointment.description" var="description"/>
<spring:message code="detailedAppointment.cancelDesc" var="cancelDescriptionTitle"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${appointmentsCss}" rel="stylesheet"/>
    <script src="${myAppointmentsJs}"></script>
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


<div class="contentContainer">
    <ul id="nav" class="nav nav-tabs">
        <c:forEach items="${tabs}" var="tab">
            <li class="nav-item">
                <spring:message code="${tab.messageID}" var="tabTitle"/>
                <a class="nav-link tab ${tab.isActive(selectedTab) ? 'active bg-primary text-white' : ''}"
                   href="#${tab.tabName}">${tabTitle}</a>
            </li>
        </c:forEach>
    </ul>

    <div class="cardsContainer">
        <c:forEach items="${tabs}" var="tab" varStatus="status">
            <div id="${tab.tabName}" class="tabContent ${tab.isActive(selectedTab) ? 'active' : ''}">
                <c:forEach items="${tab.appointments}" var="appointment">

                    <div class="card">
                        <div class="card-header">
                            <strong>${appointment.date} ${appointment.timeBlock.blockBeginning}</strong>
                        </div>
                        <div class="card-body">

                            <div class="card-title">
                                <strong>${doctor}: </strong>${appointment.doctor.firstName} ${appointment.doctor.lastName}
                            </div>
                            <div class="card-title">
                                <strong>${patient}: </strong>${appointment.patient.firstName} ${appointment.patient.lastName}
                            </div>

                            <spring:message code="${appointment.doctor.location.city.messageID}" var="city"/>
                            <div class="card-title">
                                <strong>${address}: </strong>${appointment.doctor.location.address}, ${city}</div>

                            <spring:message code="${appointment.patient.healthInsurance.messageID}"
                                            var="healthInsurance"/>
                            <div class="card-title"><strong>${healthInsurance_title}: </strong>${healthInsurance}</div>

                            <div class="card-title"><strong>${description}: </strong>${appointment.description}</div>
                            <c:if test="${not empty appointment.cancelDesc}">
                                <div class="card-title">
                                    <strong>${cancelDescriptionTitle}: </strong>${appointment.cancelDesc}</div>
                            </c:if>

                            <div class="cardButtonContainer">
                                <c:if test="${status.index == 1}">
                                    <c:url value="/my-appointments/${appointment.id}/update" var="updateUrl">
                                        <c:param name="selected_tab" value="${selectedTab}"/>
                                        <c:param name="status" value="${tab.allowedAction.statusCode}"/>
                                    </c:url>
                                    <button onclick="openModal('${updateUrl}')"
                                            class="post-button btn ${tab.allowedAction.buttonClass}">
                                        <spring:message code="${tab.allowedAction.messageID}"/>
                                    </button>
                                    <div class="modal fade" id="modal" tabindex="-1" role="dialog"
                                         aria-labelledby="modalLabel"
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
                                                            <form:input path="description" class="form-control"
                                                                        id="cancelDescription"/>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">

                                                        <div class="cardButtonContainer">
                                                            <button type="button" class="btn btn-danger"
                                                                    onclick="closeModal()">${modalDeny}</button>
                                                            <button type="submit"
                                                                    class="btn btn-primary">${modalConfirm}</button>
                                                        </div>

                                                    </div>
                                                </form:form>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${status.index == 2 && tab.allowedAction != null}">
                                    <c:url value="/${appointment.doctor.id}/review" var="reviewUrl"/>
                                    <a href="${reviewUrl}" class="btn ${tab.allowedAction.buttonClass}">
                                        <spring:message code="${tab.allowedAction.messageID}"/>
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty tab.appointments}">
                    <div class="noAppointmentsMsg">
                        <div class="alert alert-info">${noAppointments}</div>
                    </div>
                </c:if>
            </div>
        </c:forEach>
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

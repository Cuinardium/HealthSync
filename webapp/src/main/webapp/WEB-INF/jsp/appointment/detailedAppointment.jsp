<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/detailedAppointment.css" var="detailedAppointmentCss"/>


<c:url value="/my-appointments" var="myAppointmentsUrl">
  <c:param name="from" value="${from}" />
  <c:param name="to" value="${to}" />
  <c:param name="selected_tab" value="${selectedTab}" />
</c:url>
<c:url value="/my-appointments/${appointmentId}/update" var="updateUrl"/>

<spring:message code="${cityMessageId}" var="city"/>
<spring:message code="${patientHealthInsuranceMessageId}" var="healthInsurance"/>
<spring:message code="${appointmentStatusMessageId}" var="status"/>

<spring:message code="detailedAppointment.title" var="title"/>
<spring:message code="detailedAppointment.doctor" var="doctor"/>
<spring:message code="detailedAppointment.patient" var="patient"/>
<spring:message code="detailedAppointment.address" var="address"/>
<spring:message code="detailedAppointment.healthInsurance" var="healthInsurance_title"/>
<spring:message code="detailedAppointment.status" var="statusTitle"/>
<spring:message code="detailedAppointment.description" var="description"/>
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
  <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
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
        <strong>${appointmentDateTime}</strong>
        <strong>${statusTitle}: ${status}</strong>
    </div>
    <div class="card-body">
      <div class="card-title"><strong>${doctor}: </strong>${doctorName}</div>
      <div class="card-title"><strong>${patient}: </strong>${patientName}</div>
      <div class="card-title"><strong>${address}: </strong>${address}, ${city}</div>
      <div class="card-title"><strong>${healthInsurance_title}: </strong>${healthInsurance}</div>
      <div class="card-title"><strong>${description}: </strong>${appointmentDesc}</div>
      <div class="cardButtonContainer">
        <c:forEach items="${actions}" var="action">
          <button onclick="openModal('${updateUrl}/?status=${action.statusCode}')"
                  class="post-button btn ${action.buttonClass}">
            <spring:message code="${action.messageID}"/>
          </button>
          <div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
            <div class="modal-dialog" role="dialog">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="modalLabel">${modalTitle}</h5>
                </div>
                <form:form modelAttribute="modalForm" id="post-modal">
                <div class="modal-body">

                      ${modalDesc}
                    <div class="form-group">
                      <form:label path="description" for="cancelDescription" class="col-form-label">${cancelDesc}</form:label>
                      <form:input path="description" class="form-control" id="cancelDescription"/>
                    </div>

                </div>
                <div class="modal-footer">

                    <div class="cardButtonContainer">
                      <button type="button" class="btn btn-danger" onclick="closeModal()">${modalDeny}</button>
                      <button type="submit" class="btn btn-primary">${modalConfirm}</button>
                    </div>

                </div>
                </form:form>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</div>
</body>
</html>
<script>
  function openModal(action){
    $('#modal').modal('show');
    $('#post-modal').attr('action', action);
  }
  function closeModal(){
    $('#modal').modal('hide')
  }
</script>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>


<c:url value="/my-appointments" var="myAppointmentsUrl">
  <c:param name="from" value="${from}" />
  <c:param name="to" value="${to}" />
  <c:param name="selected_tab" value="${selectedTab}" />
</c:url>

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

<html>
<head>
  <title>${title}</title>

  <jsp:include page="../components/favicon.jsp"/>
  <link href="${mainCss}" rel="stylesheet"/>
</head>
<body>
<jsp:include page="../components/header.jsp"/>

<div class="generalPadding">
  <div class="backButtonContainer">
    <a href="${myAppointmentsUrl}" class="btn btn-primary backButton">
      <i class="fa-solid fa-arrow-left"></i>
    </a>
  </div>
  <div class="card mt-3">
    <div class="card-header">
      <div class="d-flex flex-row justify-content-between">
        <strong>${appointmentDateTime}</strong>
        <strong>${statusTitle}: ${status}</strong>

      </div>

    </div>
    <div class="card-body">
      <div class="card-title"><strong>${doctor}: </strong>${doctorName}</div>
      <div class="card-title"><strong>${patient}: </strong>${patientName}</div>
      <div class="card-title"><strong>${address}: </strong>${address}, ${city}</div>
      <div class="card-title"><strong>${healthInsurance_title}: </strong>${healthInsurance}</div>
      <div class="card-title"><strong>${description}: </strong>${appointmentDesc}</div>
    </div>
  </div>

</div>
</body>
</html>

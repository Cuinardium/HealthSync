<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>

<spring:message code="appointments.title" var="title"/>
<spring:message code="appointments.noAppointments" var="noAppointments"/>
<spring:message code="appointments.pending" var="pending"/>
<spring:message code="appointments.rejected" var="rejected"/>
<spring:message code="appointments.confirmed" var="confirmed"/>
<spring:message code="appointments.cancelled" var="cancelled"/>
<spring:message code="appointments.history" var="history"/>
<spring:message code="appointments.cancel" var="cancel"/>


<html>
<head>
  <title>${title}</title>

  <!-- favicon -->
  <jsp:include page="../components/favicon.jsp"/>
  <link href="${mainCss}" rel="stylesheet"/>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
</head>

<body>
<jsp:include page="../components/header.jsp"/>

<div class="container pt-2">
  <ul id="nav" class="nav nav-tabs">
    <li class="nav-item">
      <a id="pending-tab" class="nav-link active bg-primary text-white" href="#pending">${pending}</a>
    </li>
    <li id="confirm-tab" class="nav-item">
      <a class="nav-link" href="#confirmed">${confirmed}</a>
    </li>
    <li id="rejected-tab" class="nav-item">
      <a class="nav-link" href="#rejected">${rejected}</a>
    </li>
    <li id="cancelled-tab" class="nav-item">
      <a class="nav-link" href="#cancelled">${cancelled}</a>
    </li>
    <li id="history-tab" class="nav-item">
      <a class="nav-link" href="#history">${history}</a>
    </li>
  </ul>

  <div class="cardsContainer">
    <div id="pending" class="tabContent active">
      <c:forEach items="${pendingAppointments}" var="appointment">
        <div class="card mt-3">
          <div class="card-header d-flex flex-row justify-content-between">
            <div class="align-self-center">
                ${appointment.date} ${appointment.timeBlock.blockBeginning}
            </div>
          </div>
          <div class="card-body">
            <p class="card-text">
                ${appointment.description}
            </p>
          </div>
        </div>

        <!-- TODO: appointment card
        - url to "appointment details" ????
        - buttons to cancell if status != rejected (para user)
        - button to reject cancell or accept (para doctor)
        -->

      </c:forEach>
      <c:if test="${empty pendingAppointments}">
        <div class="d-flex justify-content-center mt-3">
          <div class="alert alert-info">${noAppointments}</div>
        </div>
      </c:if>
    </div>
    <div id="confirmed" class="tabContent">
      <c:forEach items="${upcomingAppointments}" var="appointment">
        <div class="card mt-3">
          <div class="card-header d-flex flex-row justify-content-between">
            <div class="align-self-center">
                ${appointment.date} ${appointment.timeBlock.blockBeginning}
            </div>
            <c:url value="/my-appointments/${appointment.id}/update" var="updateUrl"/>
            <div class="flex-row">
              <form class="d-inline" method="post" action="${updateUrl}/?status=3">
                <button type="submit" class="align-self-end btn btn-danger">${cancel}</button>
              </form>
            </div>

          </div>
          <div class="card-body">
            <p class="card-text">
                ${appointment.description}
            </p>
          </div>
        </div>

        <!-- TODO: appointment card
        - url to "appointment details" ????
        - buttons to cancell if status != rejected (para user)
        - button to reject cancell or accept (para doctor)
        -->

      </c:forEach>
      <c:if test="${empty upcomingAppointments}">
        <div class="d-flex justify-content-center mt-3">
          <div class="alert alert-info">${noAppointments}</div>
        </div>
      </c:if>
    </div>
    <div id="rejected" class="tabContent">
      <c:forEach items="${rejectedAppointments}" var="appointment">
        <div class="card mt-3">
          <div class="card-header d-flex flex-row justify-content-between">
            <div class="align-self-center">
                ${appointment.date} ${appointment.timeBlock.blockBeginning}
            </div>
          </div>
          <div class="card-body">
            <p class="card-text">
                ${appointment.description}
            </p>
          </div>
        </div>

        <!-- TODO: appointment card
        - url to "appointment details" ????
        - buttons to cancell if status != rejected (para user)
        - button to reject cancell or accept (para doctor)
        -->

      </c:forEach>
      <c:if test="${empty rejectedAppointments}">
        <div class="d-flex justify-content-center mt-3">
          <div class="alert alert-info">${noAppointments}</div>
        </div>
      </c:if>
    </div>
    <div id="cancelled" class="tabContent">
      <c:forEach items="${cancelledAppointments}" var="appointment">
        <div class="card mt-3">
          <div class="card-header d-flex flex-row justify-content-between">
            <div class="align-self-center">
                ${appointment.date} ${appointment.timeBlock.blockBeginning}
            </div>
          </div>
          <div class="card-body">
            <p class="card-text">
                ${appointment.description}
            </p>
          </div>
        </div>

        <!-- TODO: appointment card
        - url to "appointment details" ????
        - buttons to cancell if status != rejected (para user)
        - button to reject cancell or accept (para doctor)
        -->

      </c:forEach>
      <c:if test="${empty cancelledAppointments}">
        <div class="d-flex justify-content-center mt-3">
          <div class="alert alert-info">${noAppointments}</div>
        </div>
      </c:if>
    </div>
    <div id="history" class="tabContent">
      <c:forEach items="${completedAppointments}" var="appointment">
        <div class="card mt-3">
          <div class="card-header d-flex flex-row justify-content-between">
            <div class="align-self-center">
                ${appointment.date} ${appointment.timeBlock.blockBeginning}
            </div>
          </div>
          <div class="card-body">
            <p class="card-text">
                ${appointment.description}
            </p>
          </div>
        </div>

        <!-- TODO: appointment card
        - url to "appointment details" ????
        - buttons to cancell if status != rejected (para user)
        - button to reject cancell or accept (para doctor)
        -->

      </c:forEach>
      <c:if test="${empty completedAppointments}">
        <div class="d-flex justify-content-center mt-3">
          <div class="alert alert-info">${noAppointments}</div>
        </div>
      </c:if>
    </div>
  </div>


</div>
</body>
</html>
<script>
  $('document').ready(function(){
    // Hide all tab content except for the active one
    $(".tabContent").not(".active").hide();

    $('li.nav-item a').click(function(){

      $('li.nav-item a').removeClass('active');
      $('li.nav-item a').removeClass('bg-primary');
      $('li.nav-item a').removeClass('text-white');
      $(this).addClass('active');
      $(this).addClass('bg-primary');
      $(this).addClass('text-white');

      var target = $(this).attr('href');
      $('.tabContent').hide();
      $(target).show();
      return false;
    });
  });
</script>



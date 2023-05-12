<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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

<spring:message code="appointments.modal.title" var="modalTitle"/>
<spring:message code="appointments.modal.desc" var="modalDesc"/>
<spring:message code="appointments.modal.confirm" var="modalConfirm"/>
<spring:message code="appointments.modal.deny" var="modalDeny"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${appointmentsCss}" rel="stylesheet"/>
    <script src="${myAppointmentsJs}"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</head>

<body>
<jsp:include page="../components/header.jsp"/>


    <div class="contentContainer">
        <form id="get-form" action="${myAppointmentUrl}" method="get" class="dateFilter">
            <div class="datePickerContainer">
                <div class="fromDatePicker">
                    <label for="from">${fromTitle}</label>
                    <input id="from" type="date" name="from" class="form-control" value="${from}"/>
                </div>
                <div class="toDatePicker">
                    <label for="to">${toTitle}</label>
                    <input id="to" type="date" name="to" class="form-control" value="${to}">
                </div>
            </div>

            <input type="hidden" id="selected_tab" name="selected_tab" value="${selectedTab}"/>

            <input id="get-button" type="submit" value="${filter}" class="btn btn-primary"/>
        </form>

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
            <c:forEach items="${tabs}" var="tab">
                <div id="${tab.tabName}" class="tabContent ${tab.isActive(selectedTab) ? 'active' : ''}">
                    <c:forEach items="${tab.appointments}" var="appointment">
                        <c:url value="/${appointment.id}/detailed_appointment" var="detailedUrl">
                            <c:param name="from" value="${from}" />
                            <c:param name="to" value="${to}" />
                            <c:param name="selected_tab" value="${selectedTab}" />
                        </c:url>
                        <div class="card">
                            <div class="card-header">
                                <a class="detailed-link" href="${detailedUrl}">${appointment.date} ${appointment.timeBlock.blockBeginning}</a>

                                <c:url value="/my-appointments/${appointment.id}/update" var="updateUrl"/>
                                <div class="cardButtonContainer">
                                    <c:forEach items="${tab.allowedActions}" var="action">
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
                                                    <div class="modal-body">
                                                            ${modalDesc}
                                                    </div>
                                                    <div class="modal-footer">
                                                        <form id="post-modal" method="post">
                                                            <div class="form-row">
                                                                <button type="button" class="btn btn-danger" onclick="closeModal()">${modalDeny}</button>
                                                                <button type="submit" class="btn btn-primary">${modalConfirm}</button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="card-body">
                                    ${appointment.description}
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
    function openModal(action){
        $('#modal').modal('show');
        $('#post-modal').attr('action', action);
    }
    function closeModal(){
        $('#modal').modal('hide')
    }
</script>

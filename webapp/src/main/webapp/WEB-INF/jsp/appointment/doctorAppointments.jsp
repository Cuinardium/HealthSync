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
<spring:message code="appointments.requests" var="requests"/>
<spring:message code="appointments.upcoming" var="upcoming"/>
<spring:message code="appointments.cancelled" var="cancelled"/>
<spring:message code="appointments.history" var="history"/>
<spring:message code="appointments.confirm" var="confirm"/>
<spring:message code="appointments.reject" var="reject"/>
<spring:message code="appointments.cancel" var="cancel"/>
<spring:message code="appointments.from" var="fromTitle"/>
<spring:message code="appointments.to" var="toTitle"/>
<spring:message code="appointments.filter" var="filter"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${appointmentsCss}" rel="stylesheet"/>
    <script src="${myAppointmentsJs}"></script>
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
            <li class="nav-item">
                <a id="request-tab"
                   class="nav-link tab ${selectedTab <= 0 || selectedTab >= 4 ? 'active bg-primary text-white' : ''}"
                   href="#requests">${requests}</a>
            </li>
            <li id="confirm-tab" class="nav-item">
                <a class="nav-link tab ${selectedTab == 1 ? 'active bg-primary text-white' : ''}"
                   href="#confirmed">${upcoming}</a>
            </li>
            <li id="cancelled-tab" class="nav-item">
                <a class="nav-link tab ${selectedTab == 2 ? 'active bg-primary text-white' : ''}"
                   href="#cancelled">${cancelled}</a>
            </li>
            <li id="history-tab" class="nav-item">
                <a class="nav-link tab ${selectedTab == 3 ? 'active bg-primary text-white' : ''}"
                   href="#history">${history}</a>
            </li>
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
                                        <form id="post-form" method="post" action="${updateUrl}/?status=${action.statusCode}">
                                            <button type="submit"
                                                    class="post-button btn ${action.buttonClass}">
                                                <spring:message code="${action.messageID}"/>
                                            </button>
                                        </form>
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

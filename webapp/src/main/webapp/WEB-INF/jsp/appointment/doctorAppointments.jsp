<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/my-appointments" var="myAppointmentUrl"/>

<c:url value="js/myAppointments.js" var="myAppointmentsJs"/>

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
    <script src="${myAppointmentsJs}"></script>
</head>

<body>
<jsp:include page="../components/header.jsp"/>

<div class="container pt-2">
    <form id="get-form" action="${myAppointmentUrl}" method="get" class="row justify-content-between">
        <div class="col">
            <label for="from">${from}</label>
            <input id="from" type="date" name="from" class="form-control" value="${from}"/>
        </div>
        <div class="col">
            <label for="to">${to}</label>
            <input id="to" type="date" name="to" class="form-control" value="${to}">
        </div>
        <input type="hidden" id="selected_tab" name="selected_tab" value="${selectedTab}"/>
        <div class="col align-self-end">
            <input id="get-button" type="submit" value="${filter}" class="btn btn-primary"/>
        </div>
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

        <!--
        // ID
        // active condition
        // items
        // status

        {tabName (string), boolean isActive(Integer selectedTab), Appointment[] items, AllowedActions[] }
         -->
        <div id="requests" class="tabContent ${selectedTab <= 0 || selectedTab >= 4 ? 'active' : ''}">  <!-- here, ID, active cond-->
            <c:forEach items="${pendingAppointments}" var="appointment">                                <!-- here items -->
                <c:url value="/${appointment.id}/detailed_appointment" var="detailedUrl">
                    <c:param name="from" value="${from}" />
                    <c:param name="to" value="${to}" />
                    <c:param name="selected_tab" value="${selectedTab}" />
                </c:url>
                <div class="card mt-3">
                    <div class="card-header d-flex flex-row justify-content-between">
                        <div class="align-self-center">
                            <a class="detailed-link" href="${detailedUrl}">${appointment.date} ${appointment.timeBlock.blockBeginning}</a>
                        </div>
                        <c:url value="/my-appointments/${appointment.id}/update" var="updateUrl"/>
                        <div class="flex-row">
                            <!-- for each with buttons -->
                            <form class="d-inline" method="post" action="${updateUrl}/?status=1">       <!-- here status code -->
                                <button type="submit"
                                        class="align-self-end btn btn-success post-button">${confirm}</button> <!-- here buttons -->
                            </form>
                            <form class="d-inline" method="post" action="${updateUrl}/?status=2">
                                <button type="submit"
                                        class="align-self-end btn btn-danger post-button">${reject}</button>
                            </form>
                        </div>

                    </div>
                    <div class="card-body">
                            ${appointment.description}
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty pendingAppointments}">                                              <!-- here empty items -->
                <div class="d-flex justify-content-center mt-3">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
        <div id="confirmed" class="tabContent ${selectedTab == 1 ? 'active' : ''}">
            <c:forEach items="${upcomingAppointments}" var="appointment">
                <c:url value="/${appointment.id}/detailed_appointment" var="detailedUrl">
                    <c:param name="from" value="${from}" />
                    <c:param name="to" value="${to}" />
                    <c:param name="selected_tab" value="${selectedTab}" />
                </c:url>
                <div class="card mt-3">
                    <div class="card-header d-flex flex-row justify-content-between">
                        <div class="align-self-center">
                            <a class="detailed-link" href="${detailedUrl}">${appointment.date} ${appointment.timeBlock.blockBeginning}</a>
                        </div>
                        <c:url value="/my-appointments/${appointment.id}/update" var="updateUrl"/>
                        <div class="flex-row">
                            <form class="d-inline" method="post" action="${updateUrl}/?status=3">
                                <button type="submit"
                                        class="align-self-end btn btn-danger post-button">${cancel}</button>
                            </form>
                        </div>

                    </div>
                    <div class="card-body">
                        <p class="card-text">
                                ${appointment.description}
                        </p>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty upcomingAppointments}">
                <div class="d-flex justify-content-center mt-3">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
        <div id="cancelled" class="tabContent ${selectedTab == 2 ? 'active' : ''}">
            <c:forEach items="${cancelledAppointments}" var="appointment">
                <c:url value="/${appointment.id}/detailed_appointment" var="detailedUrl">
                    <c:param name="from" value="${from}" />
                    <c:param name="to" value="${to}" />
                    <c:param name="selected_tab" value="${selectedTab}" />
                </c:url>
                <div class="card mt-3">
                    <div class="card-header d-flex flex-row justify-content-between">
                        <div class="align-self-center">
                            <a class="detailed-link" href="${detailedUrl}">${appointment.date} ${appointment.timeBlock.blockBeginning}</a>
                        </div>
                    </div>
                    <div class="card-body">
                        <p class="card-text">
                                ${appointment.description}
                        </p>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty cancelledAppointments}">
                <div class="d-flex justify-content-center mt-3">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
        <div id="history" class="tabContent ${selectedTab == 3 ? 'active' : ''}">
            <c:forEach items="${completedAppointments}" var="appointment">
                <c:url value="/${appointment.id}/detailed_appointment" var="detailedUrl">
                    <c:param name="from" value="${from}" />
                    <c:param name="to" value="${to}" />
                    <c:param name="selected_tab" value="${selectedTab}" />
                </c:url>
                <div class="card mt-3">
                    <div class="card-header d-flex flex-row justify-content-between">
                        <div class="align-self-center">
                            <a class="detailed-link" href="${detailedUrl}">${appointment.date} ${appointment.timeBlock.blockBeginning}</a>
                        </div>
                    </div>
                    <div class="card-body">
                        <p class="card-text">
                                ${appointment.description}
                        </p>
                    </div>
                </div>
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

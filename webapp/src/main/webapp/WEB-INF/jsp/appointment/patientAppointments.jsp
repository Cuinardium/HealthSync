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
<spring:message code="appointments.pending" var="pending"/>
<spring:message code="appointments.rejected" var="rejected"/>
<spring:message code="appointments.confirmed" var="confirmed"/>
<spring:message code="appointments.cancelled" var="cancelled"/>
<spring:message code="appointments.history" var="history"/>
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

        <input type="hidden" id="selected_tab" name="selected_tab" value="${selectedTab}" />

        <input id="get-button" type="submit" value="${filter}" class="btn btn-primary"/>
    </form>

    <ul id="nav" class="nav nav-tabs">
        <li class="nav-item">
            <a id="pending-tab" class="nav-link tab ${selectedTab <= 0 || selectedTab >= 5 ? 'active bg-primary text-white' : ''}" href="#pending">${pending}</a>
        </li>
        <li id="confirm-tab" class="nav-item">
            <a class="nav-link tab ${selectedTab == 1 ? 'active bg-primary text-white' : ''}" href="#confirmed">${confirmed}</a>
        </li>
        <li id="rejected-tab" class="nav-item">
            <a class="nav-link tab ${selectedTab == 2 ? 'active bg-primary text-white' : ''}" href="#rejected">${rejected}</a>
        </li>
        <li id="cancelled-tab" class="nav-item">
            <a class="nav-link tab ${selectedTab == 3 ? 'active bg-primary text-white' : ''}" href="#cancelled">${cancelled}</a>
        </li>
        <li id="history-tab" class="nav-item">
            <a class="nav-link tab ${selectedTab == 4 ? 'active bg-primary text-white' : ''}" href="#history">${history}</a>
        </li>
    </ul>

    <div class="cardsContainer">
        <div id="pending" class="tabContent ${selectedTab <= 0 || selectedTab >= 5 ? 'active' : ''}">
            <c:forEach items="${pendingAppointments}" var="appointment">
                <c:url value="/${appointment.id}/detailed_appointment" var="detailedUrl">
                    <c:param name="from" value="${from}" />
                    <c:param name="to" value="${to}" />
                    <c:param name="selected_tab" value="${selectedTab}" />
                </c:url>
                <div class="card">
                    <div class="card-header">
                        <a class="detailed-link" href="${detailedUrl}">${appointment.date} ${appointment.timeBlock.blockBeginning}</a>
                    </div>
                    <div class="card-body">
                            ${appointment.description}
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty pendingAppointments}">
                <div class="noAppointmentsMsg">
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
                <div class="card">
                    <div class="card-header">
                        <a class="detailed-link" href="${detailedUrl}">${appointment.date} ${appointment.timeBlock.blockBeginning}</a>

                        <c:url value="/my-appointments/${appointment.id}/update" var="updateUrl"/>
                        <div class="cardButtonContainer">
                            <form id="post-form" method="post" action="${updateUrl}/?status=3">
                                <button id="post-button" type="submit"
                                        class="post-button btn btn-danger">${cancel}</button>
                            </form>
                        </div>

                    </div>
                    <div class="card-body">
                            ${appointment.description}
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty upcomingAppointments}">
                <div class="noAppointmentsMsg">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
        <div id="rejected" class="tabContent ${selectedTab == 2 ? 'active' : ''}">
            <c:forEach items="${rejectedAppointments}" var="appointment">
                <c:url value="/${appointment.id}/detailed_appointment" var="detailedUrl">
                    <c:param name="from" value="${from}" />
                    <c:param name="to" value="${to}" />
                    <c:param name="selected_tab" value="${selectedTab}" />
                </c:url>
                <div class="card">
                    <div class="card-header">
                        <a class="detailed-link" href="${detailedUrl}">${appointment.date} ${appointment.timeBlock.blockBeginning}</a>
                    </div>
                    <div class="card-body">
                            ${appointment.description}
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty rejectedAppointments}">
                <div class="noAppointmentsMsg">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
        <div id="cancelled" class="tabContent ${selectedTab == 3 ? 'active' : ''}">
            <c:forEach items="${cancelledAppointments}" var="appointment">
                <c:url value="/${appointment.id}/detailed_appointment" var="detailedUrl">
                    <c:param name="from" value="${from}" />
                    <c:param name="to" value="${to}" />
                    <c:param name="selected_tab" value="${selectedTab}" />
                </c:url>
                <div class="card">
                    <div class="card-header">
                        <a class="detailed-link" href="${detailedUrl}">${appointment.date} ${appointment.timeBlock.blockBeginning}</a>
                    </div>
                    <div class="card-body">
                            ${appointment.description}
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty cancelledAppointments}">
                <div class="noAppointmentsMsg">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
        <div id="history" class="tabContent ${selectedTab == 4 ? 'active' : ''}">
            <c:forEach items="${completedAppointments}" var="appointment">
                <c:url value="/${appointment.id}/detailed_appointment" var="detailedUrl">
                    <c:param name="from" value="${from}" />
                    <c:param name="to" value="${to}" />
                    <c:param name="selected_tab" value="${selectedTab}" />
                </c:url>
                <div class="card">
                    <div class="card-header">
                        <a class="detailed-link" href="${detailedUrl}">${appointment.date} ${appointment.timeBlock.blockBeginning}</a>
                    </div>
                    <div class="card-body">
                            ${appointment.description}
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty completedAppointments}">
                <div class="noAppointmentsMsg">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
    </div>


</div>
</body>
</html>

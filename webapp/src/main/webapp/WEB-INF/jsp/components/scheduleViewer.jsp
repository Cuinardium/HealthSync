<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="timeEnumValues" scope="request" type="ar.edu.itba.paw.models.ThirtyMinuteBlock[]"/>
<jsp:useBean id="dayEnumValues" scope="request" type="java.time.DayOfWeek[]"/>
<jsp:useBean id="selectedAttendingHours" scope="request" type="ar.edu.itba.paw.models.AttendingHours"/>

<spring:message code="day.monday" var="monday"/>
<spring:message code="day.tuesday" var="tuesday"/>
<spring:message code="day.wednesday" var="wednesday"/>
<spring:message code="day.thursday" var="thursday"/>
<spring:message code="day.friday" var="friday"/>
<spring:message code="day.saturday" var="saturday"/>
<spring:message code="day.sunday" var="sunday"/>
<spring:message code="form.time" var="time"/>
<spring:message code="form.showAllTimes" var="showAllTimes"/>
<spring:message code="form.showWeekend" var="showWeekend"/>
<spring:message code="form.clear" var="clear"/>

<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/scheduleViewer.css" var="scheduleViewerCss"/>

<html>
<head>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${scheduleViewerCss}" rel="stylesheet"/>
</head>
<body>

<c:set value="${16}" var="minAttendingHour"/>
<c:forEach items="${dayEnumValues}" var="day" varStatus="status">
    <c:if test="${selectedAttendingHours.getAttendingBlocksForDay(day).size() > 0}">
        <c:set value="${minAttendingHour <= selectedAttendingHours.getAttendingBlocksForDay(day).get(0).ordinal() ?
                                        minAttendingHour
                                      : selectedAttendingHours.getAttendingBlocksForDay(day).get(0).ordinal()}"
               var="minAttendingHour"/>
    </c:if>
</c:forEach>

<c:set value="${35}" var="maxAttendingHour"/>
<c:forEach items="${dayEnumValues}" var="day" varStatus="status">
    <c:if test="${selectedAttendingHours.getAttendingBlocksForDay(day).size() > 0}">
        <c:set value="${maxAttendingHour >= selectedAttendingHours.getAttendingBlocksForDay(day).get(selectedAttendingHours.getAttendingBlocksForDay(day).size() - 1).ordinal() ?
                                        maxAttendingHour
                                      : selectedAttendingHours.getAttendingBlocksForDay(day).get(selectedAttendingHours.getAttendingBlocksForDay(day).size() - 1).ordinal()}"
               var="maxAttendingHour"/>
    </c:if>
</c:forEach>

<div class="scheduleContainer">
    <div class="timeLabelContainer">
        <div class="scheduleTitle">${time}</div>
        <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
            <div class="timeLabel ${status.index < minAttendingHour || status.index > maxAttendingHour ? "hidden" : ""}">
                    ${block.blockBeginning} - ${block.blockEnd}
            </div>
        </c:forEach>
    </div>

    <div class="daySchedule">
        <div class="scheduleTitle">${monday}</div>
        <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
            <div data-index="${status.index}"
                 class="timeBlock ${selectedAttendingHours.getAttendingBlocksForDay(dayEnumValues[0]).contains(block) ? "selected" : "unselected" } ${status.index < minAttendingHour || status.index > maxAttendingHour ? "hidden" : ""}">
                &nbsp;
            </div>
        </c:forEach>
    </div>
    <div class="daySchedule">
        <div class="scheduleTitle">${tuesday}</div>
        <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
            <div data-index="${status.index}"
                 class="timeBlock ${selectedAttendingHours.getAttendingBlocksForDay(dayEnumValues[1]).contains(block) ? "selected" : "unselected" } ${status.index < minAttendingHour || status.index > maxAttendingHour ? "hidden" : ""}">
                &nbsp;
            </div>
        </c:forEach>
    </div>
    <div class="daySchedule">
        <div class="scheduleTitle">${wednesday}</div>
        <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
            <div data-index="${status.index}"
                 class="timeBlock ${selectedAttendingHours.getAttendingBlocksForDay(dayEnumValues[2]).contains(block) ? "selected" : "unselected" } ${status.index < minAttendingHour || status.index > maxAttendingHour ? "hidden" : ""}">
                &nbsp;
            </div>
        </c:forEach>
    </div>
    <div class="daySchedule">
        <div class="scheduleTitle">${thursday}</div>
        <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
            <div data-index="${status.index}"
                 class="timeBlock ${selectedAttendingHours.getAttendingBlocksForDay(dayEnumValues[3]).contains(block) ? "selected" : "unselected" } ${status.index < minAttendingHour || status.index > maxAttendingHour ? "hidden" : ""}">
                &nbsp;
            </div>
        </c:forEach>
    </div>
    <div class="daySchedule">
        <div class="scheduleTitle">${friday}</div>
        <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
            <div data-index="${status.index}"
                 class="timeBlock ${selectedAttendingHours.getAttendingBlocksForDay(dayEnumValues[4]).contains(block) ? "selected" : "unselected" } ${status.index < minAttendingHour || status.index > maxAttendingHour ? "hidden" : ""}">
                &nbsp;
            </div>
        </c:forEach>
    </div>
    <c:set value="${selectedAttendingHours.getAttendingBlocksForDay(dayEnumValues[5]).isEmpty() && selectedAttendingHours.getAttendingBlocksForDay(dayEnumValues[6]).isEmpty()}" var="weekendEmpty"/>
    <div class="daySchedule ${weekendEmpty ? 'hidden' : ''}">
        <div class="scheduleTitle">${saturday}</div>
        <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
            <div data-index="${status.index}"
                 class="timeBlock ${selectedAttendingHours.getAttendingBlocksForDay(dayEnumValues[5]).contains(block) ? "selected" : "unselected" } ${status.index < minAttendingHour || status.index > maxAttendingHour ? "hidden" : ""}">
                &nbsp;
            </div>
        </c:forEach>
    </div>
    <div class="daySchedule ${weekendEmpty ? 'hidden' : ''}">
        <div class="scheduleTitle">${sunday}</div>
        <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
            <div data-index="${status.index}"
                 class="timeBlock ${selectedAttendingHours.getAttendingBlocksForDay(dayEnumValues[6]).contains(block) ? "selected" : "unselected" } ${status.index < minAttendingHour || status.index > maxAttendingHour ? "hidden" : ""}">
                &nbsp;
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
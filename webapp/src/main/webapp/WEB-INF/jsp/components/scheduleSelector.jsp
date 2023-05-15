<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="timeEnumValues" scope="request" type="ar.edu.itba.paw.models.ThirtyMinuteBlock[]"/>

<spring:message code="day.monday" var="monday"/>
<spring:message code="day.tuesday" var="tuesday"/>
<spring:message code="day.wednesday" var="wednesday"/>
<spring:message code="day.thursday" var="thursday"/>
<spring:message code="day.friday" var="friday"/>
<spring:message code="day.saturday" var="saturday"/>
<spring:message code="day.sunday" var="sunday"/>
<spring:message code="form.time" var="time"/>
<spring:message code="form.scheduleSelector" var="scheduleSelector"/>
<spring:message code="form.showAllTimes" var="showAllTimes"/>
<spring:message code="form.showWeekend" var="showWeekend"/>
<spring:message code="form.clear" var="clear"/>

<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/scheduleSelector.css" var="scheduleSelectorCss"/>
<c:url value="/js/scheduleSelector.js" var="scheduleSelectorJs"/>

<html>
<head>
  <link href="${mainCss}" rel="stylesheet"/>
  <link href="${scheduleSelectorCss}" rel="stylesheet"/>
  <script src="${scheduleSelectorJs}"></script>
</head>
<body>

<label class="d-flex justify-content-center">${scheduleSelector}</label>

<div class="scheduleContainer">
  <div class="timeLabelContainer">
    <div class="scheduleTitle">${time}</div>
    <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
      <div class="timeLabel ${status.index < 16 || status.index > 35 ? "hidden" : ""}">
          ${block.blockBeginning} - ${block.blockEnd}
      </div>
    </c:forEach>
  </div>

  <div class="daySchedule">
    <div class="scheduleTitle">${monday}</div>
    <form:select path="mondayAttendingHours" hidden="true" multiple="true">
      <c:forEach items="${timeEnumValues}" var="block">
        <form:option value="${block.ordinal()}"/>
      </c:forEach>
    </form:select>
    <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
      <div data-index="${status.index}"
           class="timeBlock ${form.mondayAttendingHours.contains(block.ordinal()) ? "selected" : "unselected" } ${status.index < 16 || status.index > 35 ? "hidden" : ""}">
        &nbsp;
      </div>
    </c:forEach>
  </div>
  <div class="daySchedule">
    <div class="scheduleTitle">${tuesday}</div>
    <form:select path="tuesdayAttendingHours" hidden="true" multiple="true">
      <c:forEach items="${timeEnumValues}" var="block">
        <form:option value="${block.ordinal()}"/>
      </c:forEach>
    </form:select>
    <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
      <div data-index="${status.index}"
           class="timeBlock ${form.tuesdayAttendingHours.contains(block.ordinal()) ? "selected" : "unselected" } ${status.index < 16 || status.index > 35 ? "hidden" : ""}">
        &nbsp;
      </div>
    </c:forEach>
  </div>
  <div class="daySchedule">
    <div class="scheduleTitle">${wednesday}</div>
    <form:select path="wednesdayAttendingHours" hidden="true" multiple="true">
      <c:forEach items="${timeEnumValues}" var="block">
        <form:option value="${block.ordinal()}"/>
      </c:forEach>
    </form:select>
    <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
      <div data-index="${status.index}"
           class="timeBlock ${form.wednesdayAttendingHours.contains(block.ordinal()) ? "selected" : "unselected" } ${status.index < 16 || status.index > 35 ? "hidden" : ""}">
        &nbsp;
      </div>
    </c:forEach>
  </div>
  <div class="daySchedule">
    <div class="scheduleTitle">${thursday}</div>
    <form:select path="thursdayAttendingHours" hidden="true" multiple="true">
      <c:forEach items="${timeEnumValues}" var="block">
        <form:option value="${block.ordinal()}"/>
      </c:forEach>
    </form:select>
    <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
      <div data-index="${status.index}"
           class="timeBlock ${form.thursdayAttendingHours.contains(block.ordinal()) ? "selected" : "unselected" } ${status.index < 16 || status.index > 35 ? "hidden" : ""}">
        &nbsp;
      </div>
    </c:forEach>
  </div>
  <div class="daySchedule">
    <div class="scheduleTitle">${friday}</div>
    <form:select path="fridayAttendingHours" hidden="true" multiple="true">
      <c:forEach items="${timeEnumValues}" var="block">
        <form:option value="${block.ordinal()}"/>
      </c:forEach>
    </form:select>
    <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
      <div data-index="${status.index}"
           class="timeBlock ${form.fridayAttendingHours.contains(block.ordinal()) ? "selected" : "unselected" } ${status.index < 16 || status.index > 35 ? "hidden" : ""}">
        &nbsp;
      </div>
    </c:forEach>
  </div>
  <div class="daySchedule hidden">
    <div class="scheduleTitle">${saturday}</div>
    <form:select path="saturdayAttendingHours" hidden="true" multiple="true">
      <c:forEach items="${timeEnumValues}" var="block">
        <form:option value="${block.ordinal()}"/>
      </c:forEach>
    </form:select>
    <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
      <div data-index="${status.index}"
           class="timeBlock ${form.saturdayAttendingHours.contains(block.ordinal()) ? "selected" : "unselected" } ${status.index < 16 || status.index > 35 ? "hidden" : ""}">
        &nbsp;
      </div>
    </c:forEach>
  </div>
  <div class="daySchedule hidden">
    <div class="scheduleTitle">${sunday}</div>
    <form:select path="sundayAttendingHours" hidden="true" multiple="true">
      <c:forEach items="${timeEnumValues}" var="block">
        <form:option value="${block.ordinal()}"/>
      </c:forEach>
    </form:select>
    <c:forEach items="${timeEnumValues}" var="block" varStatus="status">
      <div data-index="${status.index}"
           class="timeBlock ${form.sundayAttendingHours.contains(block.ordinal()) ? "selected" : "unselected" } ${status.index < 16 || status.index > 35 ? "hidden" : ""}">
        &nbsp;
      </div>
    </c:forEach>
  </div>
</div>
<div class="checkBoxContainer">
  <div class="form-check form-switch">
    <input id="show-all-times-check" class="form-check-input" type="checkbox">
    <label class="form-check-label">
      ${showAllTimes}
    </label>
  </div>
  <div class="form-check form-switch">
    <input id="show-weekend-check" class="form-check-input" type="checkbox">
    <label class="form-check-label">
      ${showWeekend}
    </label>
  </div>
  <div id="clear-button" class="btn btn-danger">
    ${clear}
  </div>
</div>
</body>
</html>
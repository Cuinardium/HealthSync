<%@ page import="ar.edu.itba.paw.models.ThirtyMinuteBlock" %>
<%@ page import="ar.edu.itba.paw.webapp.form.DoctorEditForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>

<c:url value="/doctor-edit" var="doctorEditUrl"/>

<spring:message code="profile.edit" var="title"/>
<spring:message code="form.name" var="name"/>
<spring:message code="form.name_hint" var="name_hint"/>
<spring:message code="form.lastname" var="lastname"/>
<spring:message code="form.lastname_hint" var="lastname_hint"/>
<spring:message code="form.city" var="city"/>
<spring:message code="form.city_hint" var="city_hint"/>
<spring:message code="form.address" var="address"/>
<spring:message code="form.address_hint" var="address_hint"/>
<spring:message code="form.specialization" var="specialization"/>
<spring:message code="form.specialization_hint" var="specialization_hint"/>
<spring:message code="form.healthcare" var="healthcare"/>
<spring:message code="form.healthcare_hint" var="healthcare_hint"/>
<spring:message code="form.email" var="email"/>
<spring:message code="form.email_hint" var="email_hint"/>
<spring:message code="profile.saveChanges" var="saveChanges"/>


<spring:message code="day.monday" var="monday"/>
<spring:message code="day.tuesday" var="tuesday"/>
<spring:message code="day.wednesday" var="wednesday"/>
<spring:message code="day.thursday" var="thursday"/>
<spring:message code="day.friday" var="friday"/>
<spring:message code="day.saturday" var="saturday"/>
<spring:message code="day.sunday" var="sunday"/>
<spring:message code="form.time" var="time"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${formsCss}" rel="stylesheet"/>
</head>

<body>
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<div class="formContainer generalPadding">
    <h1>${title}</h1>
    <form:form modelAttribute="doctorEditForm" class="card" action="${doctorEditUrl}"
               method="POST" enctype="multipart/form-data">
        <div class="formCol">
            <div class="formItem">
                <c:url value="/img/${user.getProfilePictureId()}" var="userImg"/>
                <img src="${userImg}" width="100" height="100" class="rounded-circle">
            </div>
            <div class="formItem">
                <form:label path="image">${image}</form:label>
                <form:input class="form-control" type="file" placeholder="${image_hint}" path="image"/>
                <form:errors path="image" cssClass="error" element="p"/>
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="name">${name}</form:label>
                <form:input class="form-control" type="text" placeholder="${name_hint}" path="name"/>
                <form:errors path="name" cssClass="error" element="p"/>
            </div>
            <div class="formItem">
                <form:label path="lastname">${lastname}</form:label>
                <form:input class="form-control" path="lastname" type="text" placeholder="${lastname_hint}"/>
                <form:errors path="lastname" cssClass="error" element="p"/>
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="cityCode">${city}</form:label>
                <form:select class="form-select" path="cityCode">
                    <form:option value="-1" disabled="true" hidden="true"> -- </form:option>
                    <c:forEach items="${cities}" var="city" varStatus="status">
                        <form:option value="${status.index}">
                            <spring:message code="${city.messageID}"/>
                        </form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="cityCode" cssClass="error" element="p"/>
            </div>
            <div class="formItem">
                <form:label path="address">${address}</form:label>
                <form:input class="form-control" path="address" type="text" placeholder="${address_hint}"/>
                <form:errors path="address" cssClass="error" element="p"/>
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="specialtyCode">${specialization}</form:label>
                <form:select class="form-select" path="specialtyCode">
                    <form:option value="-1" disabled="true" hidden="true"> -- </form:option>
                    <c:forEach items="${specialties}" var="specialty" varStatus="status">
                        <form:option value="${status.index}">
                            <spring:message code="${specialty.messageID}"/>
                        </form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="specialtyCode" cssClass="error" element="p"/>
            </div>
            <div class="formItem">
                <form:label path="healthInsuranceCode">${healthcare}</form:label>
                <form:select class="form-select" path="healthInsuranceCode">
                    <form:option value="-1" disabled="true" hidden="true"> -- </form:option>
                    <c:forEach items="${healthInsurances}" var="healthInsurance" varStatus="status">
                        <form:option value="${status.index}">
                            <spring:message code="${healthInsurance.messageID}"/>
                        </form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="healthInsuranceCode" cssClass="error" element="p"/>
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="email">${email}</form:label>
                <form:input class="form-control" path="email" type="text" placeholder="${email_hint}"/>
                <form:errors path="email" cssClass="error" element="p"/>
            </div>
        </div>

        <!-- hacerlo tipo col -->
        <div class="scheduleContainer">
            <div class="timeLabelContainer">
                <div class="scheduleTitle">Time</div>
                <c:forEach items="${timeEnumValues}" var="block">
                    <div class="timeLabel">
                            ${block.blockBeginning}
                    </div>
                </c:forEach>
            </div>

            <div class="daySchedule">
                <div class="scheduleTitle">${monday}</div>
                <form:input path="mondayAttendingHours" hidden="true"/>
                <c:forEach items="${timeEnumValues}" var="block">
                    <div data-bit="${block.blockAsBit}"
                         class="timeBlock ${block.isBlockSet(form.mondayAttendingHours) ? "selected" : "unselected" }">
                        &nbsp;
                    </div>
                </c:forEach>
            </div>
            <div class="daySchedule">
                <div class="scheduleTitle">${tuesday}</div>
                <form:input path="tuesdayAttendingHours" hidden="true"/>
                <c:forEach items="${timeEnumValues}" var="block">
                    <div data-bit="${block.blockAsBit}"
                         class="timeBlock ${block.isBlockSet(form.tuesdayAttendingHours) ? "selected" : "unselected" }">
                        &nbsp;
                    </div>
                </c:forEach>
            </div>
            <div class="daySchedule">
                <div class="scheduleTitle">${wednesday}</div>
                <form:input path="wednesdayAttendingHours" hidden="true"/>
                <c:forEach items="${timeEnumValues}" var="block">
                    <div data-bit="${block.blockAsBit}"
                         class="timeBlock ${block.isBlockSet(form.wednesdayAttendingHours) ? "selected" : "unselected" }">
                        &nbsp;
                    </div>
                </c:forEach>
            </div>
            <div class="daySchedule">
                <div class="scheduleTitle">${thursday}</div>
                <form:input path="thursdayAttendingHours" hidden="true"/>
                <c:forEach items="${timeEnumValues}" var="block">
                    <div data-bit="${block.blockAsBit}"
                         class="timeBlock ${block.isBlockSet(form.thursdayAttendingHours) ? "selected" : "unselected" }">
                        &nbsp;
                    </div>
                </c:forEach>
            </div>
            <div class="daySchedule">
                <div class="scheduleTitle">${friday}</div>
                <form:input path="fridayAttendingHours" hidden="true"/>
                <c:forEach items="${timeEnumValues}" var="block">
                    <div data-bit="${block.blockAsBit}"
                         class="timeBlock ${block.isBlockSet(form.fridayAttendingHours) ? "selected" : "unselected" }">
                        &nbsp;
                    </div>
                </c:forEach>
            </div>
            <div class="daySchedule">
                <div class="scheduleTitle">${saturday}</div>
                <form:input path="saturdayAttendingHours" hidden="true"/>
                <c:forEach items="${timeEnumValues}" var="block">
                    <div data-bit="${block.blockAsBit}"
                         class="timeBlock ${block.isBlockSet(form.saturdayAttendingHours) ? "selected" : "unselected" }">
                        &nbsp;
                    </div>
                </c:forEach>
            </div>
            <div class="daySchedule">
                <div class="scheduleTitle">${sunday}</div>
                <form:input path="sundayAttendingHours" hidden="true"/>
                <c:forEach items="${timeEnumValues}" var="block">
                    <div data-bit="${block.blockAsBit}"
                         class="timeBlock ${block.isBlockSet(form.sundayAttendingHours) ? "selected" : "unselected" }">
                        &nbsp;
                    </div>
                </c:forEach>
            </div>
        </div>

        <button type="submit" class="btn btn-primary submitButton">${saveChanges}</button>
    </form:form>
</div>
</body>
</html>

<script>

    function handleMove(event) {
        if (isDragging && (isSelecting === event.target.classList.contains("unselected"))) {
            updateInputBit(event.target);
        }
    }

    function handleDown(event) {
        isSelecting = event.target.classList.contains("unselected");
        updateInputBit(event.target)
    }

    function updateInputBit(element) {

        // Get input element from father element
        let input = element.parentElement.getElementsByTagName("input")[0];

        // Get bit from element
        let bit = BigInt(element.getAttribute("data-bit"));

        console.log("bit")
        console.log(bit.toString(2))

        // Negate bit at same index in input
        let newValue = BigInt(input.value) ^ bit;

        console.log("newValue")
        console.log((newValue).toString(2))

        input.value = newValue.toString();
        console.log(input.value)

        // Toggle class
        element.classList.toggle("selected");
        element.classList.toggle("unselected");
    }

    let isDragging = false;
    let isSelecting = false;

    document.querySelector(".scheduleContainer").addEventListener("mousedown", () => isDragging = true);
    document.querySelector(".scheduleContainer").addEventListener("mouseup", () => isDragging = false);

    document.querySelectorAll(".timeBlock").forEach(
        element => {
            element.addEventListener("mouseenter", (event) => handleMove(event));
            element.addEventListener("mousedown", (event) => handleDown(event))
        }
    )
</script>
<style>
    .selected {
        background-color: green;
        color: white;
    }

    .unselected {
        background-color: gray;
    }

    .scheduleContainer {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }

    .daySchedule {
        display: flex;
        flex-direction: column;
        align-content: center;
        padding-left: 1px;
        min-width: 100px;
    }

    .timeLabelContainer {
        display: flex;
        flex-direction: column;
        align-content: center;
        padding-left: 1px;
        min-width: 100px;
    }

    .timeBlock {
        border-width: 2px;
        border-style: inset;
        border-color: black;
        user-select: none;
    }

    .scheduleTitle {
        display: flex;
        justify-content: center;
    }

    .timeLabel {
        display: flex;
        justify-content: center;
        border-width: 2px;
        border-style: inset;
        border-color: black;
    }
</style>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="healthInsurances" scope="request" type="java.util.List"/>
<jsp:useBean id="currentHealthInsuranceCodes" scope="request" type="java.util.List"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/healthInsurancePicker.css" var="healthInsurancePickerCss"/>
<c:url value="/js/healthInsurancePicker.js" var="healthInsurancePickerJs"/>

<spring:message code="form.healthcare" var="healthcare"/>
<spring:message code="form.healthcare_hint" var="healthcare_hint"/>
<spring:message code="form.add" var="add"/>

<html>
<head>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${healthInsurancePickerCss}" rel="stylesheet"/>
    <script src="${healthInsurancePickerJs}"></script>
</head>
<body>
<div class="healthInsurancePickerContainer">
    <form:label path="healthInsuranceCodes">${healthcare}</form:label>
    <form:select id="healthInsuranceSelect" multiple="true" class="hidden" path="healthInsuranceCodes">
        <c:forEach items="${healthInsurances}" var="healthInsurance" varStatus="status">
            <form:option value="${status.index}"/>
        </c:forEach>
    </form:select>

    <div id="chipsContainer" class="chipsContainer">
        <c:forEach items="${healthInsurances}" var="healthInsurance" varStatus="status">
            <div class="chip ${currentHealthInsuranceCodes.contains(status.index) ? "" : "hidden"}">
                <spring:message code="${healthInsurance.messageID}"/>
                <button type="button" data-index="${status.index}" class="chipClose">
                    X
                </button>
            </div>
        </c:forEach>

        <div id="healthInsuranceDropdown" class="dropdown ${currentHealthInsuranceCodes.size() == healthInsurances.size() ? "hidden" : ""}">
            <span id="healthInsuranceDropdownLabel" class="${currentHealthInsuranceCodes.isEmpty() ? "" : "hidden"}">${add}</span>
            <button class="btn btn-outline-primary" type="button" data-bs-toggle="dropdown">
                <i class="fa fa-plus"></i>
            </button>
            <ul id="healthInsuranceDropdownMenu" class="dropdown-menu">
                <c:forEach items="${healthInsurances}" var="healthInsurance" varStatus="status">
                    <li>
                      <div class="dropdown-item healthInsuranceItem ${currentHealthInsuranceCodes.contains(status.index) ? "hidden" : "" }" data-index="${status.index}">
                            <spring:message code="${healthInsurance.messageID}"/>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <form:errors path="healthInsuranceCodes" cssClass="error" element="p"/>
</div>
</body>
</html>

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
</head>
<body>
<div class="healthInsurancePickerContainer">
    <form:label path="healthInsuranceCodes">${healthcare}</form:label>

    <div id="chipsContainer" class="chipsContainer">
        <c:forEach items="${healthInsurances}" var="healthInsurance" varStatus="status">
            <div class="chip ${currentHealthInsuranceCodes.contains(status.index) ? "" : "hidden"}">
                <spring:message code="${healthInsurance.messageID}"/>
            </div>
        </c:forEach>

    </div>
</div>
</body>
</html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/doctorRegister.css" var="doctorRegisterCss"/>

<c:url value="/patient-edit" var="patientEditUrl"/>

<spring:message code="profile.edit" var="title"/>
<spring:message code="form.name" var="name"/>
<spring:message code="form.name_hint" var="name_hint"/>
<spring:message code="form.lastname" var="lastname"/>
<spring:message code="form.lastname_hint" var="lastname_hint"/>
<spring:message code="form.healthcare" var="healthcare"/>
<spring:message code="form.healthcare_hint" var="healthcare_hint"/>
<spring:message code="form.email" var="email"/>
<spring:message code="form.email_hint" var="email_hint"/>
<spring:message code="profile.saveChanges" var="saveChanges"/>
<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${doctorRegisterCss}" rel="stylesheet"/>
</head>
<body>
<jsp:include page="../components/header.jsp"/>

<form:form modelAttribute="patientEditForm" action="${patientEditUrl}" method="POST">
    <div class="form-row container">
        <div class="form-item">
            <form:label path="name">${name}</form:label>
            <form:input type="text" placeholder="${name_hint}" path="name"/>
            <form:errors path="name" cssClass="error" element="p"/>
        </div>
        <div class="form-item">
            <form:label path="lastname">${lastname}</form:label>
            <form:input path="lastname" type="text" placeholder="${lastname_hint}"/>
            <form:errors path="lastname" cssClass="error" element="p"/>
        </div>
    </div>
    <div class="form-item">
        <form:label path="healthInsuranceCode">${healthcare}</form:label>
        <form:select path="healthInsuranceCode">
            <form:option value="-1" disabled="true" hidden="true"> -- </form:option>
            <c:forEach items="${healthInsurances}" var="healthInsurance" varStatus="status">
                <form:option value="${status.index}">
                    <spring:message code="${healthInsurance.messageID}"/>
                </form:option>
            </c:forEach>
        </form:select>
        <form:errors path="healthInsuranceCode" cssClass="error" element="p"/>
    </div>
    <div>
        <button type="submit" class="btn btn-primary">${saveChanges}</button>
    </div>
</form:form>
</body>
</html>

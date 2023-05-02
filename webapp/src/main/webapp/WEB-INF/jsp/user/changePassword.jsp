<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/doctorRegister.css" var="doctorRegisterCss"/>

<c:url value="/change-password" var="changePasswordUrl"/>

<spring:message code="profile.changePassword" var="title"/>
<spring:message code="form.password" var="password"/>
<spring:message code="form.password_hint" var="password_hint"/>
<spring:message code="form.cpassword" var="cpassword"/>
<spring:message code="form.cpassword_hint" var="cpassword_hint"/>
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

<form:form modelAttribute="changePasswordForm" action="${changePasswordUrl}" method="POST">
    <div class="form-row container">
    <div>
        <form:label path="password">${password}</form:label>
        <form:input type="password" path="password" placeholder="${password_hint}"/>
        <form:errors path="password" cssClass="error" element="p"/>
    </div>
    <div class="form-item">
        <form:label path="confirmPassword">${cpassword}</form:label>
        <form:input path="confirmPassword" type="password" placeholder="${cpassword_hint}"/>
        <form:errors path="confirmPassword" cssClass="error" element="p"/>
        <form:errors/>
    </div>
    <div>
        <button type="submit" class="btn btn-primary">${saveChanges}</button>
    </div>
</form:form>
</body>
</html>

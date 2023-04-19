<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/register_medic.css" var="registerMedicCss"/>
<c:url value="/register_medic" var="registerMedicUrl"/>

<spring:message code="registerMedic.title" var="title"/>
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
<spring:message code="form.password" var="password"/>
<spring:message code="form.password_hint" var="password_hint"/>
<spring:message code="form.cpassword" var="cpassword"/>
<spring:message code="form.cpassword_hint" var="cpassword_hint"/>
<spring:message code="registerMedic.submit" var="submit"/>



<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${registerMedicCss}" rel="stylesheet"/>
</head>

<body>
<!-- Navbar -->
<jsp:include page="../components/navBar.jsp"/>

<!-- Content -->
<div class="page-content p-5" id="content">
    <h1>${title}</h1>
    <form:form modelAttribute="medicRegisterForm" class="form-container" action="${register_medic}" method="POST">
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
        <div class="form-item">
            <div>
                <form:label path="city">${city}</form:label>
                <form:input path="city" type="text" placeholder="${city_hint}"/>
                <form:errors path="city" cssClass="error" element="p"/>
            </div>
            <div>
                <form:label path="address">${address}</form:label>
                <form:input path="address" type="text" placeholder="${address_hint}"/>
                <form:errors path="address" cssClass="error" element="p"/>
            </div>
        </div>
        <!-- dropdown menu -->
        <div class="form-item">
            <form:label path="specialization">${specialization}</form:label>
            <form:input path="specialization" type="text" placeholder="${specialization_hint}"/>
            <form:errors path="specialization" cssClass="error" element="p"/>
        </div>

        <!-- multiple option buttons -->
        <!-- TODO: buscar otro nombre para obra social -->
        <div class="form-item">
            <form:label path="healthcare">${healthcare}</form:label>
            <form:input path="healthcare" type="text" placeholder="${healthcare_hint}"/>
            <form:errors path="healthcare" cssClass="error" element="p"/>
        </div>
        <div class="form-item">
            <form:label path="email">${email}</form:label>
            <form:input path="email" type="text" placeholder="${email_hint}"/>
            <form:errors path="email" cssClass="error" element="p"/>
        </div>
        <div class="form-item">
            <form:label path="password">${password}</form:label>
            <form:input path="password" type="password" placeholder="${password_hint}"/>
            <form:errors path="password" cssClass="error" element="p"/>
        </div>
        <div class="form-item">
            <form:label path="confirmPassword">${cpassword}</form:label>
            <form:input path="confirmPassword" type="password" placeholder="${cpassword_hint}"/>
            <form:errors path="confirmPassword" cssClass="error" element="p"/>
        </div>
        <button type="submit" class="btn btn-primary">${submit}</button>
    </form:form>
</div>
</body>
</html>

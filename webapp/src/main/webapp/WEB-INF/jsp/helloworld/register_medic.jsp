<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="/resources/externalResources.jsp"/>

<html lang="en">
    <head>
        <title>Medic Registration</title>
        <link href="/css/main.css" rel="stylesheet"/>
        <link href="/css/register_medic.css" rel="stylesheet"/>
    </head>

    <body>
    <jsp:include page="../components/navBar.jsp"/>
    <div class="page-content p-5" id="content">
        <h1><spring:message code="registerMedic.title"/></h1>
        <c:url value="/register_medic" var="registerMedicUrl" />
        <form:form modelAttribute="medicRegisterForm" class="form-container" action="${register_medic}" method="POST">
            <div class="form-item">
                <form:label path="name"><spring:message code="form.name"/></form:label>
                <spring:message code="form.name_hint" var="name_hint"/>
                <form:input type="text" placeholder="${name_hint}" path="name"/>
                <form:errors path="name" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <form:label path="lastname"><spring:message code="form.lastname"/></form:label>
                <spring:message code="form.lastname_hint" var="lastname_hint"/>
                <form:input path="lastname" type="text" placeholder="${lastname_hint}"/>
                <form:errors path="lastname" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <div>
                    <form:label path="city"><spring:message code="form.city"/></form:label>
                    <spring:message code="form.city_hint" var="city_hint"/>
                    <form:input path="city" type="text" placeholder="${city_hint}" />
                    <form:errors path="city" cssClass="error" element="p"/>
                </div>
                <div>
                    <form:label path="address"><spring:message code="form.address"/></form:label>
                    <spring:message code="form.address_hint" var="address_hint"/>
                    <form:input path="address" type="text" placeholder="${address_hint}"/>
                    <form:errors path="address" cssClass="error" element="p"/>
                </div>
            </div>
            <!-- dropdown menu -->
            <div class="form-item">
                <form:label path="specialization"><spring:message code="form.specialization"/></form:label>
                <spring:message code="form.specialization_hint" var="specialization_hint"/>
                <form:input path="specialization" type="text" placeholder="${specialization_hint}"/>
                <form:errors path="specialization" cssClass="error" element="p"/>
            </div>

            <!-- multiple option buttons -->
            <!-- TODO: buscar otro nombre para obra social -->
            <div class="form-item" >
                <form:label path="healthcare"><spring:message code="form.healthcare"/></form:label>
                <spring:message code="form.healthcare_hint" var="healthcare_hint"/>
                <form:input path="healthcare" type="text" placeholder="${healthcare_hint}"/>
                <form:errors path="healthcare" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <form:label path="email"><spring:message code="form.email"/></form:label>
                <spring:message code="form.email_hint" var="email_hint"/>
                <form:input path="email" type="text" placeholder="${email_hint}"/>
                <form:errors path="email" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <form:label path="password"><spring:message code="form.password"/></form:label>
                <spring:message code="form.password_hint" var="password_hint"/>
                <form:input path="password" type="password" placeholder="${password_hint}"/>
                <form:errors path="password" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <form:label path="confirmPassword"><spring:message code="form.cpassword"/></form:label>
                <spring:message code="form.cpassword_hint" var="cpassword_hint"/>
                <form:input path="confirmPassword" type="password" placehodler="${cpassword_hint}"/>
                <form:errors path="confirmPassword" cssClass="error" element="p"/>
            </div>
            <button type="submit" class="btn btn-primary"><spring:message code="registerMedic.submit"/></button>
        </form:form>
    </div>
    </body>
</html>

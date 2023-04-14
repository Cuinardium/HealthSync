<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="/resources/externalResources.jsp"/>

<html lang="en">
    <head>
        <title>Medic Registration</title>
        <link href="/css/register_medic.css" rel="stylesheet"/>
    </head>

    <body>
        <h1><spring:message code="registerMedic.title"/></h1>
        <c:url value="/register_medic" var="registerMedicUrl" />
        <c:set var="name_hint" value='<spring:message code="form.name_hint"/>'/>
        <c:set var="lastname_hint" value='<spring:message code="form.lastname_hint"/>'/>
        <c:set var="email_hint" value='<spring:message code="form.email_hint"/>'/>
        <c:set var="healthcare_hint" value='<spring:message code="form.healthcare_hint"/>'/>
        <c:set var="city_hint" value='<spring:message code="form.city_hint"/>'/>
        <c:set var="address_hint" value='<spring:message code="form.address_hint"/>'/>
        <c:set var="specialization_hint" value='<spring:message code="form.specialization_hint"/>'/>
        <c:set var="password_hint" value='<spring:message code="form.password_hint"/>'/>
        <c:set var="cpassword_hint" value='<spring:message code="form.cpassword_hint"/>'/>
        <form:form modelAttribute="medicRegisterForm" class="form-container" action="${register_medic}" method="POST">
            <div class="form-item">
                <form:label path="name"><spring:message code="form.name"/></form:label>
                <form:input type="text" placeholder="${name_hint}" path="name"/>
                <form:errors path="name" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <form:label path="lastname"><spring:message code="form.lastname"/></form:label>
                <form:input path="lastname" type="text" placeholder="${lastname_hint}"/>
                <form:errors path="lastname" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <div>
                    <form:label path="city"><spring:message code="form.city"/></form:label>
                    <form:input path="city" type="text" placeholder="${city_hint}" />
                    <form:errors path="city" cssClass="error" element="p"/>
                </div>
                <div>
                    <form:label path="address"><spring:message code="form.address"/></form:label>
                    <form:input path="address" type="text" placeholder="${address_hint}"/>
                    <form:errors path="address" cssClass="error" element="p"/>
                </div>
            </div>
            <!-- dropdown menu -->
            <div class="form-item">
                <form:label path="specialization"><spring:message code="form.specialization"/></form:label>
                <form:input path="specialization" type="text" placeholder="${specialization_hint}"/>
                <form:errors path="specialization" cssClass="error" element="p"/>
            </div>

            <!-- multiple option buttons -->
            <!-- TODO: buscar otro nombre para obra social -->
            <div class="form-item" >
                <form:label path="healthcare"><spring:message code="form.healthcare"/></form:label>
                <form:input path="healthcare" type="text" placeholder="${healthcare_hint}"/>
                <form:errors path="healthcare" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <form:label path="email"><spring:message code="form.email"/></form:label>
                <form:input path="email" type="text" placeholder="${email_hint}"/>
                <form:errors path="email" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <form:label path="password"><spring:message code="form.password"/></form:label>
                <form:input path="password" type="password" placeholder="${password_hint}"/>
                <form:errors path="password" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <form:label path="confirmPassword"><spring:message code="form.cpassword"/></form:label>
                <form:input path="confirmPassword" type="password" placehodler="${cpassword_hint}"/>
                <form:errors path="confirmPassword" cssClass="error" element="p"/>
            </div>
            <button type="submit"><spring:message code="registerMedic.submit"/></button>
        </form:form>
    </body>
</html>

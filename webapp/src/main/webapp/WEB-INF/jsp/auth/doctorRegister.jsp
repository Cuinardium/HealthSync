<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/register_medic.css" var="registerMedicCss"/>

<c:url value="/doctor-register" var="doctorRegisterUrl"/>

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
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<div class="page-content p-5" id="content">
    <h1>${title}</h1>
    <form:form modelAttribute="medicRegisterForm" class="form-container container card" action="${doctorRegisterUrl}" method="POST">
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
        <div class="form-row container">
            <div class="form-item">
                <form:label path="cityCode">${city}</form:label>
                <form:select path="cityCode">
                <form:option value="-1" disabled="true" hidden="true"> -- </form:option>
                <c:forEach items="${cities}" var="city" varStatus="status">
                    <form:option value="${status.index}">
                        <spring:message code="${city.messageID}"/>
                    </form:option>
                </c:forEach>
                </form:select>
                <form:errors path="cityCode" cssClass="error" element="p"/>
            </div>
            <div class="form-item">
                <form:label path="address">${address}</form:label>
                <form:input path="address" type="text" placeholder="${address_hint}"/>
                <form:errors path="address" cssClass="error" element="p"/>
            </div>
        </div>
        <div class="form-row container">
        <!-- dropdown menu -->
        <div class="form-item">
            <form:label path="specialtyCode">${specialization}</form:label>
            <form:select path="specialtyCode">
                <form:option value="-1" disabled="true" hidden="true"> -- </form:option>
                <c:forEach items="${specialties}" var="specialty" varStatus="status">
                    <form:option value="${status.index}">
                        <spring:message code="${specialty.messageID}"/>
                    </form:option>
                </c:forEach>
            </form:select>
            <form:errors path="specialtyCode" cssClass="error" element="p"/>
        </div>

        <!-- multiple option buttons -->
        <!-- TODO: buscar otro nombre para obra social -->
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
            <form:errors/>
        </div>
        <button type="submit" class="btn btn-primary">${submit}</button>
    </form:form>
</div>
</body>
</html>

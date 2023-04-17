<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="/resources/externalResources.jsp"/>

<html>
<head>
    <title>Appointment</title>
    <link href="/css/main.css" rel="stylesheet"/>
    <link href="/css/register_medic.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="../components/navBar.jsp"/>
<div class="page-content p-5" id="content">
    <div class="bcontent">
    <h1> <spring:message code="appointment.title"/> </h1>
<c:url value="/appointment" var="appointmentUrl" />



<form:form modelAttribute="appointmentForm"  action="${appointmentUrl}" method="post">
    <div class="form-container">
    <div class="form-item">
        <form:label path="name" class="tag"><spring:message code="form.name"/></form:label>
        <div>
            <spring:message code="form.name_hint" var="name_hint"/>
        <form:input path="name" class="label" type="text" placeholder="${name_hint}"/>
        <form:errors path="name" cssClass="error" element="p"/>
        </div>
    </div>
    <div class="form-item">
        <form:label path="lastname" class="tag"><spring:message code="form.lastname"/></form:label>
        <div>
            <spring:message code="form.lastname_hint" var="lastname_hint"/>
        <form:input path="lastname" class="label" type="text" placeholder="${lastname_hint}"/>
        <form:errors path="lastname" cssClass="error" element="p"/>
        </div>
    </div>
    <div class="form-item">
        <form:label path="email" class="tag"><spring:message code="form.email"/></form:label>
        <div>
            <spring:message code="form.email_hint" var="email_hint"/>
        <form:input path="email" class="label" type="text" placeholder="${email_hint}"/>
        <form:errors path="email" cssClass="error" element="p"/>
        </div>
    </div>
    <div class="form-item">
        <form:label path="healthcare" class="tag"><spring:message code="form.healthcare"/></form:label>
        <div>
            <spring:message code="form.healthcare_hint" var="healthcare_hint"/>
        <form:input path="healthcare" class="label" type="text" placeholder="${healthcare_hint}"/>
            <form:errors path="healthcare" cssClass="error" element="p"/>
        </div>
    </div>
    <div class="form-item">
        <form:label path="date" class="tag"><spring:message code="form.date"/></form:label>
        <div>
        <form:input path="date" class="label" type="date"/>
        </div>
    </div>
    <div class="form-item">
        <form:label path="description" class="tag"><spring:message code="form.desc"/></form:label>
        <div>
            <spring:message code="form.desc_hint" var="desc_hint"/>
        <form:input path="description" class="label" type="text" placeholder="${desc_hint}"/>
            <form:errors path="description" cssClass="error" element="p"/>
        </div>
    </div>
        <form:input type="hidden" path="docEmail" value="${email}" />
    <div>
        <button type="submit" class="btn btn-primary" value="Create appointment"><spring:message code="appointment.submit"/></button>
    </div>
    </div>
</form:form>
</div>
</div>
</body>
</html>



<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="/resources/externalResources.jsp"/>
<html>
<body>
<div align="center">
    <h2> <spring:message code="appointment.title"/> </h2>
<c:url value="/appointment" var="appointmentUrl" />

<form:form modelAttribute="appointmentForm"  action="${appointmentUrl}" method="post">
    <div class="form">
    <div class="container">
        <form:label path="name" class="tag"><spring:message code="appointment.name"/></form:label>
        <div>
        <form:input path="name" class="label" type="text" placeholder='<spring:message code="appointment.name_hint"/>'/>
        <form:errors path="name" cssClass="error" element="p"/>
        </div>
    </div>
    <div class="container">
        <form:label path="lastname" class="tag"><spring:message code="appointment.lastname"/></form:label>
        <div>
        <form:input path="lastname" class="label" type="text" placeholder='<spring:message code="appointment.lastname_hint"/>'/>
        <form:errors path="lastname" cssClass="error" element="p"/>
        </div>
    </div>
    <div class="container">
        <form:label path="email" class="tag"><spring:message code="appointment.email"/></form:label>
        <div>
        <form:input path="email" class="label" type="text" placeholder='<spring:message code="appointment.email_hint"/>'/>
        <form:errors path="email" cssClass="error" element="p"/>
        </div>
    </div>
    <div class="container">
        <form:label path="healthcare" class="tag"><spring:message code="appointment.healthcare"/></form:label>
        <div>
        <form:input path="healthcare" class="label" type="text" placeholder='<spring:message code="appointment.healthcare_hint"/>'/>
            <form:errors path="healthcare" cssClass="error" element="p"/>
        </div>
    </div>
    <div class="container">
        <form:label path="date" class="tag"><spring:message code="appointment.date"/></form:label>
        <div>
        <form:input path="date" class="label" type="date"/>
        </div>
    </div>
    <div class="container">
        <form:label path="description" class="tag"><spring:message code="appointment.desc"/></form:label>
        <div>
        <form:input path="description" class="label" type="text" placeholder='<spring:message code="appointment.desc_hint"/>'/>
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
</body>
</html>

<style>
    .error{
        color:red;
    }
    .form{
        display: flex;
        flex-direction: column;
        row-gap: 10px;
    }
    .container{
        display: flex;
        flex-direction: row;
        justify-content: space-between;

    }
    .tag{
        align-self: flex-start;
    }
    .label{
        align-self: center;
    }
</style>

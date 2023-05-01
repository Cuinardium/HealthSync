<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/register_medic.css" var="registerCss"/>

<c:url value="/${medicId}/appointment" var="appointmentUrl"/>

<spring:message code="appointment.title" var="title"/>
<spring:message code="form.name" var="name"/>
<spring:message code="form.name_hint" var="name_hint"/>
<spring:message code="form.lastname_hint" var="lastname_hint"/>
<spring:message code="form.email_hint" var="email_hint"/>
<spring:message code="form.healthcare_hint" var="healthcare_hint"/>
<spring:message code="form.desc_hint" var="desc_hint"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${registerCss}" rel="stylesheet"/>
</head>
<body>
<jsp:include page="../components/header.jsp"/>

<div class="page-content p-5" id="content">
    <div class="bcontent">
        <h1>${title}</h1>


        <form:form modelAttribute="appointmentForm" action="${appointmentUrl}" method="post">
            <div class="form-container card">
                <div class="form-row container">
                <div class="form-item">
                    <form:label path="date" class="tag"><spring:message code="form.date"/></form:label>
                    <div>
                        <form:input path="date" class="label" type="datetime-local"/>
                    </div>
                    <form:errors/>
                </div>
                <div class="form-item">
                    <form:label path="description" class="tag"><spring:message code="form.desc"/></form:label>
                    <div>
                        <form:textarea path="description" class="label" type="text" placeholder="${desc_hint}"/>
                        <form:errors path="description" cssClass="error" element="p"/>
                    </div>
                </div>
                <div>
                    <button type="submit" class="btn btn-primary" value="Create appointment"><spring:message
                            code="appointment.submit"/></button>
                </div>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>



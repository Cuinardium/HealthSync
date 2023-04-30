<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/register_medic.css" var="registerCss"/>
<c:url value="/css/register_medic.css" var="registerMedicCss"/>

<c:url value="/register" var="registerUrl"/>

<spring:message code="register.title" var="title"/>
<spring:message code="form.name" var="name"/>
<spring:message code="form.name_hint" var="name_hint"/>
<spring:message code="form.lastname" var="lastname"/>
<spring:message code="form.lastname_hint" var="lastname_hint"/>
<spring:message code="form.healthcare" var="healthcare"/>
<spring:message code="form.healthcare_hint" var="healthcare_hint"/>
<spring:message code="form.email" var="email"/>
<spring:message code="form.email_hint" var="email_hint"/>
<spring:message code="form.password" var="password"/>
<spring:message code="form.password_hint" var="password_hint"/>
<spring:message code="form.cpassword" var="cpassword"/>
<spring:message code="form.cpassword_hint" var="cpassword_hint"/>
<spring:message code="register.submit" var="submit"/>


<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${registerCss}" rel="stylesheet"/>
    <link href="${registerMedicCss}" rel="stylesheet"/>
</head>
<body>
<div class="form-container">
    <h1>${title}</h1>
    <form:form modelAttribute="registerForm" action="${registerUrl}" method="post">
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
            <form:label path="email">
                ${email}
                <form:input path="email" type="text" placeholder='${email_hint}'/>
                <form:errors path="email" cssClass="error" element="p"/>
            </form:label>
        </div>
        <div>
            <form:label path="password">
                ${password}
                <form:input type="password" path="password" placeholder="${password_hint}"/>
                <form:errors path="password" cssClass="error" element="p"/>
            </form:label>
        </div>
        <div class="form-item">
            <form:label path="confirmPassword">${cpassword}</form:label>
            <form:input path="confirmPassword" type="password" placeholder="${cpassword_hint}"/>
            <form:errors path="confirmPassword" cssClass="error" element="p"/>
            <form:errors/>
        </div>

        <div>
            <button type="submit" class="btn btn-primary">${submit}</button>
        </div>

    </form:form>
</div>
</body>
</html>

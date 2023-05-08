<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>

<c:url value="/login" var="loginUrl"/>

<spring:message code="login.title" var="title"/>
<spring:message code="form.email" var="email"/>
<spring:message code="form.email_hint" var="email_hint"/>
<spring:message code="form.password" var="password"/>
<spring:message code="form.password_hint" var="password_hint"/>
<spring:message code="login.rememberMe" var="rememberMe"/>
<spring:message code="login.submit" var="submit"/>
<spring:message code="login.register" var="register"/>


<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${formsCss}" rel="stylesheet"/>
</head>
<body>
<!-- Header -->
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<div class="formContainer generalPadding">
    <h1>${title}</h1>
    <form:form modelAttribute="loginForm" class="card" action="${loginUrl}" method="POST">
        <div class="formRow">
            <div class="formItem">
                <form:label path="email">${email}</form:label>
                <form:input class="form-control" path="email" type="text" placeholder='${email_hint}'/>
                <form:errors path="email" cssClass="error" element="p"/>
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="password">${password}</form:label>
                <form:input class="form-control" type="password" path="password" placeholder="${password_hint}"/>
                <form:errors path="password" cssClass="error" element="p"/>
            </div>
        </div>
        <div class="formRow">
            <div class="form-check">
                <input class="form-check-input" type="checkbox" id="rememberme" name="rememberme">
                <label class="form-check-label" for="rememberme">${rememberMe}</label>
            </div>
        </div>

        <button type="submit" class="btn btn-primary submitButton">${submit}</button>

        <div class="formRow">
            <a href="/patient-register">${register}</a>
        </div>
    </form:form>
</div>
</body>
</html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/register_medic.css" var="registerCss"/>

<c:url value="/register" var="registerUrl"/>

<spring:message code="register.title" var="title"/>
<spring:message code="form.email" var="email"/>
<spring:message code="form.email_hint" var="email_hint"/>
<spring:message code="form.password" var="password"/>
<spring:message code="form.password_hint" var="password_hint"/>
<spring:message code="register.submit" var="submit"/>


<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${registerCss}" rel="stylesheet"/>
</head>
<body>
<div class="form-container">
    <h1>${title}</h1>
    <form:form modelAttribute="registerForm" action="${registerUrl}" method="post">
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

        <div>
            <button type="submit" class="btn btn-primary">${submit}</button>
        </div>

    </form:form>
</div>
</body>
</html>

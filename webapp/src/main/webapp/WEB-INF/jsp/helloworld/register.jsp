<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="/resources/externalResources.jsp"/>

<html>
<body>
    <h2> <spring:message code="register.title"/> </h2>
    <link href="/css/register_medic.css" rel="stylesheet"/>
    <c:url value="/register" var="registerUrl" />
    <form:form modelAttribute="registerForm" action="${registerUrl}" method="post">
        <div>
            <form:label path="email"><spring:message code="form.email"/>
                <spring:message code="form.email_hint" var="email_hint"/>
                <form:input path="email" type="text" placeholder='${email_hint}'/>
                <form:errors path="email" cssClass="error" element="p"/>
            </form:label>
        </div>
        <div>
            <form:label path="password"><spring:message code="form.password"/>
                <spring:message code="form.password_hint" var="password_hint"/>
                <form:input type="password" path="password" placeholder="${password_hint}"/>
                <form:errors path="password" cssClass="error" element="p"/>
            </form:label>
        </div>

        <div>
            <input type="submit"><spring:message code="register.submit"/>
        </div>


    </form:form>
</body>
</html>

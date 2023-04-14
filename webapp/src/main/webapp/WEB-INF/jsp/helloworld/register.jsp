<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="/resources/externalResources.jsp"/>

<html>
<body>
    <h2> <spring:message code="register.title"/> </h2>
    <link href="/css/register_medic.css" rel="stylesheet"/>
    <c:url value="/register" var="registerUrl" />
    <c:set var="email_hint" value='<spring:message code="form.email_hint"/>'/>
    <c:set var="password_hint" value='<spring:message code="form.password_hint"/>'/>
    <form:form modelAttribute="registerForm" action="${registerUrl}" method="post">
        <div>
            <form:label path="email"><spring:message code="form.email"/>
                <form:input path="email" type="text" placeholder='${email_hint}'/>
                <form:errors path="email" cssClass="error" element="p"/>
            </form:label>
        </div>
        <div>
            <form:label path="password"><spring:message code="form.password"/>
                <form:input type="password" path="password" placeholder="${password_hint}"/>
                <form:errors path="password" cssClass="error" element="p"/>
            </form:label>
        </div>

        <div>
            <input type="submit"><spring:message code="register.title"/>
        </div>


    </form:form>
</body>
</html>

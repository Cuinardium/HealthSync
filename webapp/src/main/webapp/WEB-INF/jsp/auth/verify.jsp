<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>

<c:url value="/renew-token" var="renewTokenUrl"/>
<c:url value="/login" var="loginUrl"/>

<spring:message code="verify.title" var="title"/>
<spring:message code="verify.tokenInvalid" var="tokenInvalidMsg"/>
<spring:message code="verify.alreadyVerified" var="alreadyVerifiedMsg"/>
<spring:message code="verify.resendMail" var="resendMail"/>
<spring:message code="verify.resendMailButton" var="resendMailButton"/>
<spring:message code="login.title" var="login"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
</head>
<body>
<!-- Header -->
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<div class="formContainer loginPadding">
    <h1>${title}</h1>
    <div class="card">
        <div class="card-body">
            <c:if test="${tokenInvalid}">
                <p>${tokenInvalidMsg}</p>
                <p>${resendMail}</p>
                <form method="POST" action="${renewTokenUrl}">
                    <button type="submit" class="btn btn-primary">${resendMail}</button>
                </form>
            </c:if>
            <c:if test="${alreadyVerified}">
                <p>${alreadyVerifiedMsg}</p>
                <a href="${loginUrl}" class="btn btn-primary">${login}</a>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>

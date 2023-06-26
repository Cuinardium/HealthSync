<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>

<c:url value="/renew-token" var="renewTokenUrl">
    <c:param name="id" value="${id}"/>
</c:url>
<c:url value="/login" var="loginUrl"/>

<spring:message code="verify.title" var="title"/>
<spring:message code="verify.tokenInvalid" var="tokenInvalidMsg"/>
<spring:message code="verify.alreadyVerified" var="alreadyVerifiedMsg"/>
<spring:message code="verify.resendMail" var="resendMail"/>
<spring:message code="verify.resendMailButton" var="resendMailButton"/>
<spring:message code="verify.success" var="successMsg"/>
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
<div class="generalPadding">
    <div class="verifyCardContainer">
        <div class="card">
            <div class="card-body">
                <div class="titleContainer">
                    <h1>${title}</h1>
                </div>
                <c:if test="${tokenInvalid}">
                    <h4>${tokenInvalidMsg}</h4>
                    <h6>${resendMail}</h6>
                    <form method="POST" action="${renewTokenUrl}">
                        <button type="submit" class="btn btn-primary">${resendMailButton}</button>
                    </form>
                </c:if>
                <c:if test="${alreadyVerified}">
                    <h4>${alreadyVerifiedMsg}</h4>
                    <a href="${loginUrl}" class="btn btn-primary">${login}</a>
                </c:if>
                <c:if test="${successful}">
                    <h4>${successMsg}</h4>
                    <a href="${loginUrl}" class="btn btn-primary">${login}</a>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
<style>

    .titleContainer {
        margin-bottom: 2rem;
    }


    .verifyCardContainer {
        display: flex;
        flex-direction: column;
        align-items: center;
    }
</style>
</html>

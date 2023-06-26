<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>

<spring:message code="renewToken.title" var="title"/>
<spring:message code="renewToken.unsuccessful" var="renewUnsuccesful"/>
<spring:message code="renewToken.successful" var="renewSuccessful"/>

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
    <div class="resendCardContainer">
        <div class="card">
            <div class="card-body">
                <h1>${title}</h1>
                <h4>${tokenRenewed? renewSuccessful: renewUnsuccesful}</h4>
            </div>
        </div>
    </div>
</div>
</body>

<style>
    .resendCardContainer {
        display: flex;
        flex-direction: column;
        align-items: center;
    }


</style>
</html>

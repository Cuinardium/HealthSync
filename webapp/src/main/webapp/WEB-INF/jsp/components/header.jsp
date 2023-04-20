<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- Variables -->
<c:url value="/css/main.css" var="mainCss" />
<c:url value="/css/header.css" var="headerCss" />

<c:url value="/" var="homeUrl" />
<c:url value="/doctorDashboard" var="dashboardUrl" />
<c:url value="/register_medic" var="registerMedicUrl" />

<spring:message code="home.home" var="home"/>
<spring:message code="home.checkDoctor" var="checkDoctor"/>
<spring:message code="home.doctorSignUp" var="doctorSignUp"/>

<html>
<head>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${headerCss}" rel="stylesheet"/>
</head>
<body>
    <header class="border-bottom">
        <div class="container head">
            <a class="title navbar-brand" href="${homeUrl}">
                <div class="health">Health</div>
                <div class="sync">Sync</div>
            </a>

            <div class="buttons">
                <a href="${homeUrl}" class="nav-link px-2 link-secondary">${home}</a>
                <a href="${dashboardUrl}" class="nav-link px-2 link-dark">${checkDoctor}</a>
            </div>

            <div class="buttons">
                <button type="button" class="btn btn-primary" onclick="window.location='${registerMedicUrl}';">${doctorSignUp}</button>
            </div>
        </div>
    </header>
</body>
</html>

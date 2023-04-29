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
<spring:message code="home.iAmDoctor" var="iAmDoctor"/>
<spring:message code="home.iAmPatient" var="iAmPatient"/>
<spring:message code="register.title" var="register"/>
<spring:message code="login.title" var="login"/>

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
                <a href="${dashboardUrl}" class="nav-link px-2 link-dark">Check Doctors</a>
            </div>

            <div class="buttons">

                <button type="button" class="btn btn-primary" onclick="window.location='#';">${login}</button>

                <nav class="navbar navbar-expand-lg">
                    <div class="container-fluid">
                        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                            <span class="navbar-toggler-icon"></span>
                        </button>
                        <div class="collapse navbar-collapse" id="navbarNavDropdown">
                            <ul class="navbar-nav">
                                <li class="nav-item dropdown">
                                    <button class="btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                        ${register}
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li><a class="dropdown-item" href="${registerMedicUrl}">${iAmDoctor}</a></li>
                                        <!-- TODO: Add patient register routing -->
                                        <li><a class="dropdown-item" href="#">${iAmPatient}</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
            </div>
            </div>

    </header>
</body>
</html>

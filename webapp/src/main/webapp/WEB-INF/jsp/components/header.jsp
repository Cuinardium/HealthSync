<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/header.css" var="headerCss"/>

<c:url value="/" var="homeUrl"/>
<c:url value="/doctorDashboard" var="dashboardUrl"/>
<c:url value="/my-appointments" var="myAppoitnmentUrl"/>
<c:url value="/doctor-register" var="doctorRegisterUrl"/>
<c:url value="/patient-register" var="patientRegisterUrl"/>
<c:url value="/doctor-edit" var="doctorEditUrl"/>
<c:url value="/patient-edit" var="patientEditUrl"/>
<c:url value="/change-password" var="changePasswordUrl"/>
<c:url value="/login" var="loginUrl"/>
<c:url value="/logout" var="logoutUrl"/>

<c:url value="/img/logo.svg" var="logo"/>

<spring:message code="home.home" var="home"/>
<spring:message code="home.checkDoctor" var="checkDoctor"/>
<spring:message code="register.title" var="register"/>
<spring:message code="login.title" var="login"/>
<spring:message code="header.iAmDoctor" var="iAmDoctor"/>
<spring:message code="header.iAmPatient" var="iAmPatient"/>
<spring:message code="header.editProfile" var="editProfile"/>
<spring:message code="header.changePassword" var="changePassword"/>
<spring:message code="header.logout" var="logout"/>

<html>
<head>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${headerCss}" rel="stylesheet"/>
</head>
<body>
<header class="horizontalPadding border-bottom">
    <div class="head">
        <a class="title navbar-brand" href="${homeUrl}">
            <img src="${logo}" alt="logo" class="logo">
            <div class="health title">Health</div>
            <div class="sync title">Sync</div>
        </a>

        <div class="buttons">
            <a href="${dashboardUrl}"><i class="fa-solid fa-user-doctor"></i></a>
        </div>

        <c:choose>
        <c:when test="${not empty user}">
            <nav class="navbar navbar-expand-lg">
                <div class="container-fluid">
                        <a href="${myAppoitnmentUrl}">
                            <i class="fa-solid fa-calendar-check"></i>
                        </a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#profileDropdown" aria-controls="profileDropdown" aria-expanded="false"
                            aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="profileDropdown">
                        <ul class="navbar-nav">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="" role="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <img src="/img/${user.getProfilePictureId()}" width="40" height="40" class="rounded-circle">
                                </a>
                                <ul class="dropdown-menu">
                                    <c:choose>
                                        <c:when test="${isDoctor}">
                                            <li><a class="dropdown-item" href="${doctorEditUrl}">${editProfile}</a></li>
                                        </c:when>
                                        <c:otherwise>
                                            <li><a class="dropdown-item" href="${patientEditUrl}">${editProfile}</a></li>
                                        </c:otherwise>
                                    </c:choose>
                                    <li><a class="dropdown-item" href="${changePasswordUrl}">${changePassword}</a></li>
                                    <li><a class="dropdown-item" href="${logoutUrl}">${logout}</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </c:when>
        <c:otherwise>
        <div class="buttons">
            <!-- TODO: ver si se puede dejar en "<button>" como antes -->
            <a href="${loginUrl}" class="btn btn-primary" role="button">${login}</a>

            <nav class="navbar navbar-expand-lg">
                <div class="container-fluid">
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false"
                            aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNavDropdown">
                        <ul class="navbar-nav">
                            <li class="nav-item dropdown">
                                <button class="btn dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                        ${register}
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="${doctorRegisterUrl}">${iAmDoctor}</a></li>
                                    <li><a class="dropdown-item" href="${patientRegisterUrl}">${iAmPatient}</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </div>
    </div>
    </c:otherwise>
    </c:choose>

</header>
</body>
</html>

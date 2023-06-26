<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/header.css" var="headerCss"/>

<c:url value="/" var="homeUrl"/>
<c:url value="/doctor-dashboard" var="dashboardUrl"/>
<c:url value="/my-appointments" var="myAppointmentsUrl"/>
<c:url value="/doctor-register" var="doctorRegisterUrl"/>
<c:url value="/patient-register" var="patientRegisterUrl"/>
<c:url value="/doctor-profile" var="doctorProfileUrl"/>
<c:url value="/patient-profile" var="patientProfileUrl"/>
<c:url value="/login" var="loginUrl"/>
<c:url value="/logout" var="logoutUrl"/>

<c:url value="/img/logo.svg" var="logo"/>

<spring:message code="home.home" var="home"/>
<spring:message code="home.checkDoctor" var="checkDoctor"/>
<spring:message code="home.myAppointments" var="myAppointments"/>
<spring:message code="register.title" var="register"/>
<spring:message code="login.title" var="login"/>
<spring:message code="header.iAmDoctor" var="iAmDoctor"/>
<spring:message code="header.iAmPatient" var="iAmPatient"/>
<spring:message code="header.profile" var="profile"/>
<spring:message code="header.logout" var="logout"/>

<!-- ALT img text -->
<spring:message code="user.alt.loggedUserImg" var="altLoggedUserImg"/>

<html>
<head>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${headerCss}" rel="stylesheet"/>
</head>
<body>
<header class="horizontalPadding border-bottom">
    <div class="head">
        <a class="title navbar-brand" href="${homeUrl}">
            <img src="${logo}" alt="HealthSync logo" class="logo">
            <div class="health title">Health</div>
            <div class="sync title">Sync</div>
        </a>

        <div class="buttons">
            <a href="${dashboardUrl}" class="headerLink">
                ${checkDoctor}
                <i class="fa-solid fa-user-doctor"></i>
            </a>

            <c:if test="${not empty user}">
                <a href="${myAppointmentsUrl}" class="headerLink">
                    ${myAppointments}
                    <i class="fa-solid fa-calendar-check"></i>
                </a>
                <c:if test="hasNotifications">
                    <i class="fa-solid fa-comments" style="color: #ff0000;"></i>
                </c:if>
            </c:if>
        </div>

        <c:choose>
        <c:when test="${not empty user}">
            <nav class="navbar navbar-expand-lg">
                <div class="container-fluid">
                    <c:choose>
                        <c:when test="${isDoctor}">
                            <i class="fa-solid fa-user-doctor"></i>
                            <spring:message code="profile.doctor" var="tooltipText"/>
                        </c:when>
                        <c:otherwise>
                            <i class="fa-solid fa-user"></i>
                            <spring:message code="profile.patient" var="tooltipText"/>
                        </c:otherwise>
                    </c:choose>
                    <div id="profileDropdown">
                        <ul class="navbar-nav">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="" role="button" data-bs-toggle="dropdown"
                                   aria-haspopup="true" aria-expanded="false" data-toggle="tooltip" data-placement="top" title="${tooltipText}">
                                    <c:url value="/img/${user.image == null ? (isDoctor ?\"doctorDefault.png\":\"patientDefault.png\") : user.image.imageId}" var="loggedUserImg"/>
                                    <img src="${loggedUserImg}" alt="${altLoggedUserImg}" width="40" height="40"
                                         class="rounded-circle">
                                </a>
                                <ul class="dropdown-menu">
                                    <c:choose>
                                        <c:when test="${isDoctor}">
                                            <li><a class="dropdown-item" href="${doctorProfileUrl}">${profile}</a></li>
                                        </c:when>
                                        <c:otherwise>
                                            <li><a class="dropdown-item" href="${patientProfileUrl}">${profile}</a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
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
            <a href="${loginUrl}" class="btn btn-primary" role="button">${login}</a>

            <nav class="navbar navbar-expand-lg">
                <div class="container-fluid">
                    <div id="navbarNavDropdown">
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

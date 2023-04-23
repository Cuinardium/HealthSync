<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- Variables -->
<c:url value="/css/main.css" var="mainCss" />
<c:url value="/css/navBar.css" var="navBarCss" />

<c:url value="/" var="homeUrl" />
<c:url value="/doctorDashboard" var="dashboardUrl" />
<c:url value="/logout" var="logoutUrl"/>

<c:url value="/img/userDefault.png" var="userDefaultImg"/>

<spring:message code="navbar.welcome" var="welcome"/>
<spring:message code="navbar.patient" var="patient"/>
<spring:message code="navbar.main" var="main"/>
<spring:message code="navbar.home" var="home"/>
<spring:message code="navbar.checkDoc" var="checkDoc"/>
<spring:message code="navbar.logout" var="logout"/>

<html>
<head>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${navBarCss}" rel="stylesheet"/>
</head>
<body>
<div class="container vertical-nav bg-white" id="sidebar">
    <div class="top">
        <div class="identity">
            <div class="media">
                <img src="${userDefaultImg}" alt="..." width="65" class="mr-3 rounded-circle img-thumbnail shadow-sm">
                <div class="media-body">
                    <h4 class="m-0">${welcome}</h4>
                    <p class="font-weight-light text-muted mb-0">${patient}</p>
                </div>
            </div>
        </div>

        <p class="text-gray font-weight-bold text-uppercase">${main}</p>

        <ul class="nav flex-column bg-white mb-0">
            <li class="nav-item">
                <a href="${homeUrl}" class="nav-link text-dark font-italic">
                    <i class="fa fa-house mr-3 text-primary fa-fw"></i>
                    ${home}
                </a>
            </li>
            <li class="nav-item">
                <a href="${dashboardUrl}" class="nav-link text-dark font-italic">
                    <i class="fa-solid fa-user-doctor mr-3 text-primary fa-fw"></i>
                    ${checkDoc}
                </a>
            </li>
            <%--    <li class="nav-item">--%>
            <%--      <a href="#" class="nav-link text-dark font-italic">--%>
            <%--        <i class="fa-solid fa-calendar mr-3 text-primary fa-fw"></i>--%>
            <%--        Calendar--%>
            <%--      </a>--%>
            <%--    </li>--%>
            <%--    <li class="nav-item">--%>
            <%--      <a href="#" class="nav-link text-dark font-italic">--%>
            <%--        <i class="fa-solid fa-check-to-slot mr-3 text-primary fa-fw"></i>--%>
            <%--        Results--%>
            <%--      </a>--%>
            <%--    </li>--%>
            <%--    <li class="nav-item">--%>
            <%--      <a href="#" class="nav-link text-dark font-italic">--%>
            <%--        <i class="fa-solid fa-prescription-bottle-medical mr-3 text-primary fa-fw"></i>--%>
            <%--        Prescriptions--%>
            <%--      </a>--%>
            <%--    </li>--%>
        </ul>
    </div>

    <!-- log out button at end of page -->
    <div class="logout">
        <form action="${logoutUrl}" method="post">
            <button type="submit" class="btn btn-primary btn-lg btn-block">${logout}</button>
        </form>
    </div>
<%--  <p class="text-gray font-weight-bold text-uppercase px-3 small py-4 mb-0">My settings</p>--%>

<%--  <ul class="nav flex-column bg-white mb-0">--%>
<%--    <li class="nav-item">--%>
<%--      <a href="#" class="nav-link text-dark font-italic">--%>
<%--        <i class="fa fa-user mr-3 text-primary fa-fw"></i>--%>
<%--        Profile--%>
<%--      </a>--%>
<%--    </li>--%>
<%--  </ul>--%>
</div>
</body>
</html>

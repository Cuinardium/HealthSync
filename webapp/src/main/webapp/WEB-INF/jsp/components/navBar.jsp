<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
  <c:url value="/css/main.css" var="mainCss" />
  <c:url value="/css/navBar.css" var="navBarCss" />
  <link href="${mainCss}" rel="stylesheet"/>
  <link href="${navBarCss}" rel="stylesheet"/>
</head>
<body>
<c:url value="/" var="home" />
<c:url value="/doctorDashboard" var="dashboard" />
<c:url value="/img/userDefault.png" var="userDefaultImg"/>

<div class="vertical-nav bg-white" id="sidebar">
  <div class="identity">
    <div class="media">
        <img src="${userDefaultImg}" alt="..." width="65" class="mr-3 rounded-circle img-thumbnail shadow-sm">
      <div class="media-body">
        <h4 class="m-0"><spring:message code="navbar.welcome"/></h4>
        <p class="font-weight-light text-muted mb-0"><spring:message code="navbar.patient"/></p>
      </div>
    </div>
  </div>

  <p class="text-gray font-weight-bold text-uppercase px-3 small pb-4 mb-0"><spring:message code="navbar.main"/></p>

  <ul class="nav flex-column bg-white mb-0">

    <li class="nav-item">
      <a href="${home}" class="nav-link text-dark font-italic bg-light">
        <i class="fa fa-house mr-3 text-primary fa-fw"></i>
        <spring:message code="navbar.home"/>
      </a>
    </li>
    <li class="nav-item">
      <a href="${dashboard}" class="nav-link text-dark font-italic bg-light">
        <i class="fa-solid fa-user-doctor mr-3 text-primary fa-fw"></i>
        <spring:message code="navbar.checkDoc"/>
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

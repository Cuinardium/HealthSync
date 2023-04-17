<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
  <link href="/css/main.css" rel="stylesheet"/>
  <link href="/css/navBar.css" rel="stylesheet"/>
</head>
<body>
<div class="vertical-nav bg-white" id="sidebar">
  <div class="identity">
    <div class="media">
      <img src="https://w7.pngwing.com/pngs/419/473/png-transparent-computer-icons-user-profile-login-user-heroes-sphere-black-thumbnail.png" alt="..." width="65" class="mr-3 rounded-circle img-thumbnail shadow-sm">
      <div class="media-body">
        <h4 class="m-0"><spring:message code="navbar.welcome"/></h4>
        <p class="font-weight-light text-muted mb-0"><spring:message code="navbar.patient"/></p>
      </div>
    </div>
  </div>

  <p class="text-gray font-weight-bold text-uppercase px-3 small pb-4 mb-0"><spring:message code="navbar.main"/></p>

  <ul class="nav flex-column bg-white mb-0">
    <li class="nav-item">
      <a href="/" class="nav-link text-dark font-italic bg-light">
        <i class="fa fa-house mr-3 text-primary fa-fw"></i>
        <spring:message code="navbar.home"/>
      </a>
    </li>
    <li class="nav-item">
      <a href="/doctorDashboard" class="nav-link text-dark font-italic bg-light">
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

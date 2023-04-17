<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
  <c:url value="/css/main.css" var="mainCss" />
  <c:url value="/css/header.css" var="headerCss" />
  <link href="${mainCss}" rel="stylesheet"/>
  <link href="${headerCss}" rel="stylesheet"/>
</head>
<body>
  <c:url value="/" var="home" />
  <c:url value="/doctorDashboard" var="dashboard" />

<header class="border-bottom">
  <div class="container head">
      <a class="title navbar-brand" href="${home}">
        <div class="health">Health</div>
        <div class="sync">Sync</div>
    </a>

        <div class="buttons">
      <a href="${home}" class="nav-link px-2 link-secondary"><spring:message code="home.home"/></a>
      <a href="${dashboard}" class="nav-link px-2 link-dark"><spring:message code="home.checkDoctor"/></a>
    </div>

    <c:url value="/register_medic" var="register_medic" />
    <div class="buttons">
      <button type="button" class="btn btn-primary" onclick="window.location='${register_medic}';"><spring:message code="home.doctorSignUp"/></button>
    </div>
  </div>
</header>
</body>
</html>

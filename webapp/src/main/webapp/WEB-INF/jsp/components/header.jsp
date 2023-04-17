<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
  <link href="/css/main.css" rel="stylesheet"/>
  <link href="/css/header.css" rel="stylesheet"/>
</head>
<body>
<header class="border-bottom">
  <div class="container head">
    <a class="title navbar-brand" href="/">
        <div class="health">Health</div>
        <div class="sync">Sync</div>
    </a>

    <div class="buttons">
      <a href="/" class="nav-link px-2 link-secondary"><spring:message code="home.home"/></a>
      <a href="/doctorDashboard" class="nav-link px-2 link-dark"><spring:message code="home.checkDoctor"/></a>
    </div>

    <div class="buttons">
      <button type="button" class="btn btn-primary" onclick="window.location='/register_medic';"><spring:message code="home.doctorSignUp"/></button>
    </div>
  </div>
</header>
</body>
</html>

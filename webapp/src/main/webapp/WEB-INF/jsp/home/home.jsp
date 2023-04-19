<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/home.css" var="homeCss"/>
<c:url value="/" var="home"/>
<c:url value="/img/homeDoctor.png" var="homeDoctorImg"/>

<spring:message code="home.home" var="title"/>
<spring:message code="home.welcome1" var="welcome1"/>
<spring:message code="home.welcome2" var="welcome2"/>
<spring:message code="home.aboutUsTitle" var="aboutUsTitle"/>
<spring:message code="home.aboutUsText1" var="aboutUsText1"/>
<spring:message code="home.aboutUsText2" var="aboutUsText2"/>

<html>
<head>
    <title>${title}</title>
    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${homeCss}" rel="stylesheet"/>
</head>
<body>
<!-- Header -->
<jsp:include page="../components/header.jsp"/>
<!-- Content -->
<div class="container px-5 pb-5">
    <div class="row gx-5">
        <div class="col-xxl-5">
            <div class="text text-xxl-start">
                <div class="fs-3 fw-light text-muted">${welcome1}</div>
                <h1 class="display-3 fw-bolder mb-5"><span class="text-gradient">${welcome2}</span></h1>
            </div>
        </div>
        <div class="col-xxl-7">
            <div class="d-flex justify-content-center mt-5 mt-xxl-0">
                <div class="profile bg-gradient-primary-to-secondary">
                    <img class="profile-img"
                         src="${homeDoctorImg}" alt="..."/>
                </div>
            </div>
        </div>
    </div>
</div>

<section class="about">
    <div class="container">
        <div class="row gx-5">
            <div class="col-xxl-8 text">
                <h2 class="display-5 fw-bolder"><span class="text-gradient">${aboutUsTitle}</span></h2>
                <p class="lead fw-light mb-4">${aboutUsText1}</p>
                <p class="text-muted">${aboutUsText2}</p>
            </div>
        </div>
    </div>
</section>


<footer class="border-top">
    <div class="container foot">
        <a class="title navbar-brand" href="${home}">
            <div class="health">Health</div>
            <div class="sync">Sync</div>
        </a>

        <div>
            <span class="text-body-secondary">&copy; 2023 HealthSync, Inc</span>
        </div>
    </div>
</footer>
</body>
</html>

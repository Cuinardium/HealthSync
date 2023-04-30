<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/home.css" var="homeCss"/>

<c:url value="/" var="homeUrl"/>

<c:url value="/img/homeDoctor.svg" var="homeDoctorImg"/>
<c:url value="/img/circle1.svg" var="circle1"/>
<c:url value="/img/circle2.svg" var="circle2"/>
<c:url value="/img/circle3.svg" var="circle3"/>


<spring:message code="home.home" var="title"/>
<spring:message code="home.welcome1" var="welcome1"/>
<spring:message code="home.welcome2" var="welcome2"/>
<spring:message code="home.circle1" var="circleText1"/>
<spring:message code="home.circle2" var="circleText2"/>
<spring:message code="home.circle3" var="circleText3"/>
<spring:message code="home.description1" var="description1"/>
<spring:message code="home.description2" var="description2"/>
<spring:message code="home.description3" var="description3"/>

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
    <div class="row welcome gx-5">
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

<section class="about bg-light py-10">
    <div class="container px-5">
        <div class="row gx-5 text-center">
            <div class="col-lg-4 mb-5 mb-lg-0">
                <img class="circles" src="${circle1}" alt="..."/>
                <h3>${circleText1}</h3>
                <p class="mb-0">${description1}</p>
            </div>
            <div class="col-lg-4 mb-5 mb-lg-0">
                <img class="circles" src="${circle2}" alt="..."/>
                <h3>${circleText2}</h3>
                <p class="mb-0">${description2}</p>
            </div>
            <div class="col-lg-4">
                <img class="circles" src="${circle3}" alt="..."/>
                <h3>${circleText3}</h3>
                <p class="mb-0">${description3}</p>
            </div>
        </div>
    </div>
</section>


<footer class="border-top">
    <div class="container foot">
        <a class="title navbar-brand" href="${homeUrl}">
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

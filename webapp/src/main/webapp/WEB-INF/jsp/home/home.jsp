<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/home.css" var="homeCss"/>

<c:url value="/js/categoriesCarousel.js" var="categoriesCarouselJs"/>

<c:url value="/" var="homeUrl"/>
<c:url value="/doctor-dashboard?specialtyCode=" var="doctorDashboardFilteredUrl"/>

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
<spring:message code="home.categories" var="categories"/>

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
<div class="generalPadding welcome">
    <div class="welcome1Container">
        <div class="sloganSmall">${welcome1}</div>
        <h1 class="sloganBig"><span class="text-gradient">${welcome2}</span></h1>
        <div class="searchBar">
            <jsp:include page="../components/searchBar.jsp"/>
        </div>
    </div>
    <div class="profile">
        <img class="profile-img" src="${homeDoctorImg}" alt="..."/>
    </div>
</div>

<div class="carouselSectionContainer generalPadding border-top">
    <h1 class="categoriesTitle">${categories}</h1>
    <div class="carouselContainer carousel slide" id="recipeCarousel">
        <a class="carouselNavButton" href="#recipeCarousel" role="button" data-bs-slide="prev">
            <i class="fa-solid fa-angle-left"></i>
        </a>
        <div class="carousel-inner" role="listbox">
            <c:forEach items="${featuredSpecialties}" var="specialty" varStatus="status">
                <c:url value="/img/specialty-${specialty.ordinal()}.jpg" var="img"/>
                <spring:message code="${specialty.messageID}" var="name"/>
                <div class="carousel-item ${status.first ? 'active' : ''}">
                    <div class="card">
                        <div class="card-img">
                            <img src="${img}" class="img-fluid">
                        </div>
                        <div class="card-img-overlay">${name.toLowerCase()}</div>
                        <a href="${doctorDashboardFilteredUrl}${specialty.ordinal()}" class="stretched-link"></a>
                    </div>
                </div>
            </c:forEach>
        </div>
        <a class="carouselNavButton" href="#recipeCarousel" role="button" data-bs-slide="next">
            <i class="fa-solid fa-angle-right"></i>
        </a>
    </div>
</div>

<section class="about generalPadding border-top">
    <div class="aboutCircleContainer">
        <img class="circles" src="${circle1}" alt="..."/>
        <h3>${circleText1}</h3>
        <p>${description1}</p>
    </div>
    <div class="aboutCircleContainer">
        <img class="circles" src="${circle2}" alt="..."/>
        <h3>${circleText2}</h3>
        <p>${description2}</p>
    </div>
    <div class="aboutCircleContainer">
        <img class="circles" src="${circle3}" alt="..."/>
        <h3>${circleText3}</h3>
        <p>${description3}</p>
    </div>
</section>

<footer class="foot border-top horizontalPadding">
    <a class="navbar-brand" href="${homeUrl}">
        <div class="health title">Health</div>
        <div class="sync title">Sync</div>
    </a>
    <div>
        <span class="text-body-secondary">&copy; 2023 HealthSync, Inc</span>
    </div>
</footer>
</body>
</html>
<script src="${categoriesCarouselJs}"></script>

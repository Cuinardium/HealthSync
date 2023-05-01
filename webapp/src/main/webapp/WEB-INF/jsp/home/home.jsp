<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/home.css" var="homeCss"/>

<c:url value="/js/categoriesCarousel.js" var="categoriesCarouselJs"/>

<c:url value="/" var="homeUrl"/>
<c:url value="/doctorDashboard?specialty=" var="doctorDashboardFilteredUrl"/>

<c:url value="/img/homeDoctor.svg" var="homeDoctorImg"/>

<c:url value="/img/circle1.svg" var="circle1"/>
<c:url value="/img/circle2.svg" var="circle2"/>
<c:url value="/img/circle3.svg" var="circle3"/>

<c:url value="/img/cardiologist.jpeg" var="cardiologist"/>
<c:url value="/img/dermatologist.jpg" var="dermatologist"/>
<c:url value="/img/neurologist.jpg" var="neurologist"/>
<c:url value="/img/nutritionist.jpg" var="nutritionist"/>
<c:url value="/img/pediatrics.jpg" var="pediatrics"/>
<c:url value="/img/urologist.jpg" var="urologist"/>
<c:url value="/img/ophtalmologist.jpg" var="ophtalmologist"/>


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

<div class="carouselContainer generalPadding border-top">
    <h1>${categories}</h1>
    <div id="recipeCarousel" class="carousel slide">
        <div class="carousel-inner" role="listbox">
            <div class="carousel-item active">
                <div class="card">
                    <div class="card-img">
                        <img src="${cardiologist}" class="img-fluid">
                    </div>
                    <div class="card-img-overlay">Cardiology</div>
                    <a href="${doctorDashboardFilteredUrl}cardiolog" class="stretched-link"></a>
                </div>
            </div>
            <div class="carousel-item">
                <div class="card">
                    <div class="card-img">
                        <img src="${dermatologist}" class="img-fluid">
                    </div>
                    <div class="card-img-overlay">Dermatology</div>
                    <a href="${doctorDashboardFilteredUrl}dermatolog" class="stretched-link"></a>
                </div>
            </div>
            <div class="carousel-item">
                <div class="card">
                    <div class="card-img">
                        <img src="${neurologist}" class="img-fluid">
                    </div>
                    <div class="card-img-overlay">Neurology</div>
                    <a href="${doctorDashboardFilteredUrl}neurolog" class="stretched-link"></a>
                </div>
            </div>
            <div class="carousel-item">
                <div class="card">
                    <div class="card-img">
                        <img src="${nutritionist}" class="img-fluid">
                    </div>
                    <div class="card-img-overlay">Nutrition</div>
                    <a href="${doctorDashboardFilteredUrl}nutrition" class="stretched-link"></a>
                </div>
            </div>
            <div class="carousel-item">
                <div class="card">
                    <div class="card-img">
                        <img src="${ophtalmologist}" class="img-fluid">
                    </div>
                    <div class="card-img-overlay">Ophthalmology</div>
                    <a href="${doctorDashboardFilteredUrl}ophthalmolog" class="stretched-link"></a>
                </div>
            </div>
            <div class="carousel-item">
                <div class="card">
                    <div class="card-img">
                        <img src="${pediatrics}" class="img-fluid">
                    </div>
                    <div class="card-img-overlay">Pediatrics</div>
                    <a href="${doctorDashboardFilteredUrl}pediatric" class="stretched-link"></a>
                </div>
            </div>
            <div class="carousel-item">
                <div class="card">
                    <div class="card-img">
                        <img src="${urologist}" class="img-fluid">
                    </div>
                    <div class="card-img-overlay">Urology</div>
                    <a href="${doctorDashboardFilteredUrl}urolog" class="stretched-link"></a>
                </div>
            </div>
        </div>
        <a class="carousel-control-prev bg-transparent w-aut" href="#recipeCarousel" role="button" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        </a>
        <a class="carousel-control-next bg-transparent w-aut" href="#recipeCarousel" role="button" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
        </a>
    </div>
</div>

<section class="about bg-light generalPadding border-top">
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
    <a class="title navbar-brand" href="${homeUrl}">
        <div class="health">Health</div>
        <div class="sync">Sync</div>
    </a>
    <div>
        <span class="text-body-secondary">&copy; 2023 HealthSync, Inc</span>
    </div>
</footer>
</body>
</html>
<script src="${categoriesCarouselJs}"></script>
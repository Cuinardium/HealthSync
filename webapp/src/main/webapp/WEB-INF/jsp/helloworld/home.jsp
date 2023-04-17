<html>
<head>
    <title>Home</title>

    <link href="/css/main.css" rel="stylesheet"/>
    <link href="/css/home.css" rel="stylesheet"/>
    <jsp:include page="/resources/externalResources.jsp"/>
</head>
<body>
<jsp:include page="../components/header.jsp"/>

<div class="container px-5 pb-5">
    <div class="row gx-5">
        <div class="col-xxl-5">
            <div class="text text-xxl-start">
                <div class="fs-3 fw-light text-muted">We can help you</div>
                <h1 class="display-3 fw-bolder mb-5"><span class="text-gradient">Live a healthier life</span></h1>
            </div>
        </div>
        <div class="col-xxl-7">
            <div class="d-flex justify-content-center mt-5 mt-xxl-0">
                <div class="profile bg-gradient-primary-to-secondary">
                    <img class="profile-img" src="https://i.pinimg.com/originals/a8/32/58/a83258b019c857bd3a6a9c68805d271d.png" alt="..." />
                </div>
            </div>
        </div>
    </div>
</div>

<section class="about">
    <div class="container">
        <div class="row gx-5">
            <div class="col-xxl-8 text">
                <h2 class="display-5 fw-bolder"><span class="text-gradient">About Us</span></h2>
                <p class="lead fw-light mb-4">HealthSync will make visiting the doctor easier than ever.</p>
                <p class="text-muted">You will be able to make doctors' appointments with just a few clicks. No more calling and waiting. Communicating with health professionals has never been so easy.</p>
            </div>
        </div>
    </div>
</section>

<footer class="border-top">
    <div class="container foot">
        <a class="title navbar-brand" href="/">
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

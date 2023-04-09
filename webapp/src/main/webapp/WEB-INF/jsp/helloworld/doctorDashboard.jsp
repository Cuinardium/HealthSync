<html>
<head>
    <link href="/css/main.css" rel="stylesheet"/>
    <jsp:include page="/resources/externalResources.jsp"/>
</head>

<body>
<div class="vertical-nav bg-white" id="sidebar">
    <div class="py-4 px-3 mb-4 bg-light">
        <div class="media d-flex align-items-center"><img src="https://bootstrapious.com/i/snippets/sn-v-nav/avatar.png" alt="..." width="65" class="mr-3 rounded-circle img-thumbnail shadow-sm">
            <div class="media-body">
                <h4 class="m-0">Jason Doe</h4>
                <p class="font-weight-light text-muted mb-0">Patient</p>
            </div>
        </div>
    </div>

    <p class="text-gray font-weight-bold text-uppercase px-3 small pb-4 mb-0">Main</p>

    <ul class="nav flex-column bg-white mb-0">
        <li class="nav-item">
            <a href="#" class="nav-link text-dark font-italic bg-light">
<%--                <span class="material-symbols-outlined mr-3 text-primary">home</span>--%>
                Home
            </a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link text-dark font-italic">
                Calendar
            </a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link text-dark font-italic">
                Results
            </a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link text-dark font-italic">
                Prescriptions
            </a>
        </li>
    </ul>

    <p class="text-gray font-weight-bold text-uppercase px-3 small py-4 mb-0">My settings</p>

    <ul class="nav flex-column bg-white mb-0">
        <li class="nav-item">
            <a href="#" class="nav-link text-dark font-italic">
                Profile
            </a>
        </li>
    </ul>
</div>

<div class="page-content p-5" id="content">
    <div class="column">
        <div class="row">
            <div class="col-sm-6 mb-3 mb-sm-0">
                <div class="card" style="width: 18rem;">
                    <img src="https://blogscdn.thehut.net/app/uploads/sites/1160/2017/05/main-dermatologist.jpg"
                         class="card-img-top" alt="A blonde dermatologist">
                    <div class="card-body">
                        <h5 class="card-title">Dr. Lopez</h5>
                        <p class="card-text">Dermatologist. Near Palermo, CABA.</p>
                        <a href="#" class="btn btn-primary">Book appointment</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Doctor Dashboard</title>
    <c:url value="/css/main.css" var="mainCss" />
    <c:url value="/css/doctorDashboard.css" var="doctorDashboardCss" />
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${doctorDashboardCss}" rel="stylesheet"/>
    <jsp:include page="/resources/externalResources.jsp"/>
</head>

<body>
<!-- NavBar-->
<jsp:include page="../components/navBar.jsp"/>

<!-- Content -->
<div class="page-content p-5" id="content">
    <div class = "row">
        <!-- Search Bar -->
        <div class="input-group">
            <input type="text" id="input" class="form-control" placeholder="Search" aria-label="Search" aria-describedby="basic-addon2">
            <button type="button" class="btn btn-primary" onclick="search();">
                <i class="fas fa-search"></i>
            </button>
        </div>
    </div>
    <form method="get" id="filters" action="doctorDashboard">
      <div class="row pt-3">
        <div class="col">
            <input type="text" class="form-control" id="city" name="city" placeholder="City"/>
        </div>

        <div class="col">
            <input type="text" class="form-control" id="specialty" name="specialty" placeholder="Specialty">

        </div>
        <div class="col">
            <input type="text" class="form-control" id="healthcare" name="healthcare" placeholder="Health Insurance">

        </div>
        <div class="col-auto">
            <input type="submit" class="btn btn-primary" value="Filter">
        </div>
      </div>
    </form>
    <div class="flex-container bcontent">
      <c:forEach items="${doctors}" var="doctor">
        <div class="card">
          <div class="row g-0">
                <div class="col-sm-5">
                    <img src="https://blogscdn.thehut.net/app/uploads/sites/1160/2017/05/main-dermatologist.jpg"
                         class="card-img-top" alt="A blonde dermatologist">
                </div>
                <div class="col-sm-7">
                    <div class="card-body">
                        <h5 class="card-title">${doctor.firstName} ${doctor.lastName}</h5>
                        <p class="card-text">${doctor.specialty}. ${doctor.address}, ${doctor.city}</p>
                        <p class="card-text">${doctor.healthInsurance}</p>
                        <a href="<c:url value="/${doctor.id}/appointment"/>" class="btn btn-primary">Book appointment</a>
                    </div>
                </div>
            </div>
        </div>
      </c:forEach>
   </div>
</div>
</body>
<script>
    let search = () => {
        let element = document.getElementById("input").value;
        window.location='/doctorDashboard?name=' + element;
    }
</script>
</html>

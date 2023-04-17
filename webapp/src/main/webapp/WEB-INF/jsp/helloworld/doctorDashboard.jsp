<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Doctor Dashboard</title>

    <link href="/css/main.css" rel="stylesheet"/>
    <link href="/css/doctorDasboard.css" rel="stylesheet"/>
    <jsp:include page="/resources/externalResources.jsp"/>
</head>

<body>
<!-- NavBar-->
<jsp:include page="../components/navBar.jsp"/>

<!-- Content -->
<div class="page-content p-5" id="content">
    <div class = "row">
        <!-- Search Bar -->
        <jsp:include page="../components/searchBar.jsp"/>
    </div>
    <form method="get" id="filters" action="doctorDashboard">
      <div class="row pt-3">
        <div class="col">
            <input type="text" class="form-control" id="city" name="city" placeholder="city"/>
        </div>

        <div class="col">
            <input type="text" class="form-control" id="specialty" name="specialty" placeholder="specialty">

        </div>
        <div class="col">
            <input type="text" class="form-control" id="healthcare" name="healthcare" placeholder="healthcare">

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
                      <a href="<c:url value="/${doctor.id}/appointment"/>" class="btn btn-primary">Book appointment</a>
                    </div>
                </div>
            </div>
        </div>
      </c:forEach>
   </div>
</div>
</body>

</html>

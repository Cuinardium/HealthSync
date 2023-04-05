<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="/resources/externalResources.jsp"/>
<html>
<body>
<h2> Appointment Form </h2>
<c:url value="/appointment" var="appointmentUrl" />
<form action="${appointmentUrl}" method="post">
    <div>
        <label for="name" class="form-label">First Name</label>
        <input id="name"  class="form-control" type="text" name="First name"/>
    </div>
    <div>
        <label for="lastname">Last Name</label>
        <input id="lastname" type="text" name="Last name"/>
    </div>
    <div>
        <label for="email">Email</label>
        <input id="email" type="text" name="Email"/>
    <div>
        <label for="healthcare">Healthcare System</label>
        <input id="healthcare" type="text" name="Healthcare system"/>
    </div>
    <div>
        <label for="date">Appointment Date</label>
        <input id="date" type="date" name="Appointment date"/>
    </div>
    <div>
        <label for="desc">Appointment description</label>
        <input id="desc" type="text" name="Appointment description"/>
    </div>
        <input type="hidden" id="doctor-email" value=${email} name="Doctor email">
    <div>
        <input type="submit" value="Create appointment"/>
    </div>


</form>
</body>
</html>
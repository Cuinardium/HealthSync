<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<body>
<h2> Appointment Form </h2>
<c:url value="/appointment" var="appointmentUrl" />
<form action="${appointmentUrl}" method="post">
    <div>
        <label for="name">First Name</label>
        <input id="name" type="text" name="First Name"/>
    </div>
    <div>
        <label for="lastname">Last Name</label>
        <input id="lastname" type="text" name="Last Name"/>
    </div>
    <div>
        <label for="healthcare">Healthcare System</label>
        <input id="healthcare" type="text" name="Healthcare System"/>
    </div>
    <div>
        <label for="date">Appointment Date</label>
        <input id="date" type="date" name="Appointment date"/>
    </div>
    <div>
        <label for="desc">Appointment description</label>
        <input id="desc" type="text" name="Appointment description"/>
    </div>

    <div>
        <input type="submit" value="Create appointment"/>
    </div>


</form>
</body>
</html>
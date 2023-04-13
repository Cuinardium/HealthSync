<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="/resources/externalResources.jsp"/>
<html>
<body>
<div align="center">
    <h2> Appointment Form </h2>
<c:url value="/appointment" var="appointmentUrl" />

<form action="${appointmentUrl}" method="post">
    <div class="form">
    <div class="container">
        <label for="name" class="tag">First Name</label>
        <input id="name" class="label" type="text" name="First name" placeholder="Enter first name"/>
    </div>
    <div class="container">
        <label for="lastname" class="tag">Last Name</label>
        <input id="lastname" class="label" type="text" name="Last name" placeholder="Enter last name"/>
    </div>
    <div class="container">
        <label for="email" class="tag">Email</label>
        <input id="email" class="label" type="text" name="Email" placeholder="Enter email"/>
    </div>
    <div class="container">
        <label for="healthcare" class="tag">Healthcare System</label>
        <div>
        <input id="healthcare" class="label" type="text" name="Healthcare system" placeholder="Enter healthcare system"/>
        </div>
    </div>
    <div class="container">
        <label for="date" class="tag">Appointment Date</label>
        <div>
        <input id="date" class="label" type="date" name="Appointment date"/>
        </div>
    </div>
    <div class="container">
        <label for="desc" class="tag">Appointment description</label>
        <div>
        <input id="desc" class="label" type="text" name="Appointment description" placeholder="Enter appointment description"/>
        </div>
    </div>
        <input type="hidden" id="doctor-email" value=${email} name="Doctor email">
    <div>
        <button type="submit" class="btn btn-primary" value="Create appointment">Create appointment</button>
    </div>
    </div>
</form>
</div>
</body>
</html>

<style>
    .form{
        display: flex;
        flex-direction: column;
        row-gap: 10px;
    }
    .container{
        display: flex;
        flex-direction: row;
        justify-content: space-between;

    }
    .tag{
        align-self: flex-start;
    }
    .label{
        align-self: center;
    }
</style>

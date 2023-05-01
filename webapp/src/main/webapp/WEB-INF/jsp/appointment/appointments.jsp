<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>

<spring:message code="appointments.title" var="title"/>
<spring:message code="appointments.noAppointments" var="noAppointments"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
</head>

<body>
    <jsp:include page="../components/header.jsp"/>

    <div class="cardsContainer">
        <c:forEach items="${appointments}" var="appointment">
            <h1>${appointment.date}</h1>
            <p>${appointment.description}</p>

            <!-- TODO: appointment card
                - url to "appointment details" ????
                - buttons to cancell if status != rejected (para user)
                - button to reject cancell or accept (para doctor)
            -->

        </c:forEach>
        <c:if test="${appointments.isEmpty()}">
            <div class="d-flex justify-content-center">
                <!-- TODO: style this vvvv -->
                <h4>${noAppointments}</h4>
            </div>
        </c:if>
    </div>
</body>
</html>



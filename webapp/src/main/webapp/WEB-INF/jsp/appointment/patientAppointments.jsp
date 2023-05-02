<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/my-appointments" var="myAppointmentUrl"/>

<spring:message code="appointments.title" var="title"/>
<spring:message code="appointments.noAppointments" var="noAppointments"/>
<spring:message code="appointments.pending" var="pending"/>
<spring:message code="appointments.rejected" var="rejected"/>
<spring:message code="appointments.confirmed" var="confirmed"/>
<spring:message code="appointments.cancelled" var="cancelled"/>
<spring:message code="appointments.history" var="history"/>
<spring:message code="appointments.cancel" var="cancel"/>
<spring:message code="appointments.from" var="from"/>
<spring:message code="appointments.to" var="to"/>
<spring:message code="appointments.filter" var="filter"/>


<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
</head>

<body>
<jsp:include page="../components/header.jsp"/>


<div class="container pt-2">

    <form id="get-form" action="${myAppointmentUrl}" method="get" class="row justify-content-between">
        <div class="col">
            <label for="from">${from}</label>
            <input id="from" type="date" name="from" class="form-control" value="${from}"/>
        </div>
        <div class="col">
            <label for="to">${to}</label>
            <input id="to" type="date" name="to" class="form-control" value="${to}">
        </div>
        <input type="hidden" id="selected_tab" name="selected_tab" value="${selectedTab}" />
        <div class="col align-self-end">
            <input id="get-button" type="submit" value="${filter}" class="btn btn-primary"/>
        </div>
    </form>
    <ul id="nav" class="nav nav-tabs">
        <li class="nav-item">
            <a id="pending-tab" class="nav-link ${selectedTab <= 0 || selectedTab >= 5 ? 'active bg-primary text-white' : ''}" href="#pending">${pending}</a>
        </li>
        <li id="confirm-tab" class="nav-item">
            <a class="nav-link ${selectedTab == 1 ? 'active bg-primary text-white' : ''}" href="#confirmed">${confirmed}</a>
        </li>
        <li id="rejected-tab" class="nav-item">
            <a class="nav-link ${selectedTab == 2 ? 'active bg-primary text-white' : ''}" href="#rejected">${rejected}</a>
        </li>
        <li id="cancelled-tab" class="nav-item">
            <a class="nav-link ${selectedTab == 3 ? 'active bg-primary text-white' : ''}" href="#cancelled">${cancelled}</a>
        </li>
        <li id="history-tab" class="nav-item">
            <a class="nav-link ${selectedTab == 4 ? 'active bg-primary text-white' : ''}" href="#history">${history}</a>
        </li>
    </ul>

    <div class="cardsContainer">
        <div id="pending" class="tabContent ${selectedTab <= 0 || selectedTab >= 5 ? 'active' : ''}">
            <c:forEach items="${pendingAppointments}" var="appointment">
                <div class="card mt-3">
                    <div class="card-header d-flex flex-row justify-content-between">
                        <div class="align-self-center">
                                ${appointment.date} ${appointment.timeBlock.blockBeginning}
                        </div>
                    </div>
                    <div class="card-body">
                        <p class="card-text">
                                ${appointment.description}
                        </p>
                    </div>
                </div>

                <!-- TODO: appointment card
                - url to "appointment details" ????
                - buttons to cancell if status != rejected (para user)
                - button to reject cancell or accept (para doctor)
                -->

            </c:forEach>
            <c:if test="${empty pendingAppointments}">
                <div class="d-flex justify-content-center mt-3">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
        <div id="confirmed" class="tabContent ${selectedTab == 1 ? 'active' : ''}">
            <c:forEach items="${upcomingAppointments}" var="appointment">
                <div class="card mt-3">
                    <div class="card-header d-flex flex-row justify-content-between">
                        <div class="align-self-center">
                                ${appointment.date} ${appointment.timeBlock.blockBeginning}
                        </div>
                        <c:url value="/my-appointments/${appointment.id}/update" var="updateUrl"/>
                        <div class="flex-row">
                            <form id="post-form" class="d-inline" method="post" action="${updateUrl}/?status=3">
                                <button id="post-button" type="submit"
                                        class="align-self-end btn btn-danger">${cancel}</button>
                            </form>
                        </div>

                    </div>
                    <div class="card-body">
                        <p class="card-text">
                                ${appointment.description}
                        </p>
                    </div>
                </div>

                <!-- TODO: appointment card
                - url to "appointment details" ????
                - buttons to cancell if status != rejected (para user)
                - button to reject cancell or accept (para doctor)
                -->

            </c:forEach>
            <c:if test="${empty upcomingAppointments}">
                <div class="d-flex justify-content-center mt-3">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
        <div id="rejected" class="tabContent ${selectedTab == 2 ? 'active' : ''}">
            <c:forEach items="${rejectedAppointments}" var="appointment">
                <div class="card mt-3">
                    <div class="card-header d-flex flex-row justify-content-between">
                        <div class="align-self-center">
                                ${appointment.date} ${appointment.timeBlock.blockBeginning}
                        </div>
                    </div>
                    <div class="card-body">
                        <p class="card-text">
                                ${appointment.description}
                        </p>
                    </div>
                </div>

                <!-- TODO: appointment card
                - url to "appointment details" ????
                - buttons to cancell if status != rejected (para user)
                - button to reject cancell or accept (para doctor)
                -->

            </c:forEach>
            <c:if test="${empty rejectedAppointments}">
                <div class="d-flex justify-content-center mt-3">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
        <div id="cancelled" class="tabContent ${selectedTab == 3 ? 'active' : ''}">
            <c:forEach items="${cancelledAppointments}" var="appointment">
                <div class="card mt-3">
                    <div class="card-header d-flex flex-row justify-content-between">
                        <div class="align-self-center">
                                ${appointment.date} ${appointment.timeBlock.blockBeginning}
                        </div>
                    </div>
                    <div class="card-body">
                        <p class="card-text">
                                ${appointment.description}
                        </p>
                    </div>
                </div>

                <!-- TODO: appointment card
                - url to "appointment details" ????
                - buttons to cancell if status != rejected (para user)
                - button to reject cancell or accept (para doctor)
                -->

            </c:forEach>
            <c:if test="${empty cancelledAppointments}">
                <div class="d-flex justify-content-center mt-3">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
        <div id="history" class="tabContent ${selectedTab == 4 ? 'active' : ''}">
            <c:forEach items="${completedAppointments}" var="appointment">
                <div class="card mt-3">
                    <div class="card-header d-flex flex-row justify-content-between">
                        <div class="align-self-center">
                                ${appointment.date} ${appointment.timeBlock.blockBeginning}
                        </div>
                    </div>
                    <div class="card-body">
                        <p class="card-text">
                                ${appointment.description}
                        </p>
                    </div>
                </div>

                <!-- TODO: appointment card
                - url to "appointment details" ????
                - buttons to cancell if status != rejected (para user)
                - button to reject cancell or accept (para doctor)
                -->

            </c:forEach>
            <c:if test="${empty completedAppointments}">
                <div class="d-flex justify-content-center mt-3">
                    <div class="alert alert-info">${noAppointments}</div>
                </div>
            </c:if>
        </div>
    </div>


</div>
</body>
</html>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Get all tab content
        var tabContents = document.querySelectorAll('.tabContent');

        // Hide all tab content except for the active one
        for (var i = 0; i < tabContents.length; i++) {
            if (!tabContents[i].classList.contains('active')) {
                tabContents[i].style.display = 'none';
            }
        }

        // Get all tab links
        var tabLinks = document.querySelectorAll('li.nav-item a');

        // Add click event listeners to each tab link
        for (i = 0; i < tabLinks.length; i++) {
            tabLinks[i].addEventListener('click', function (e) {
                e.preventDefault();

                // Remove 'active', 'bg-primary' and 'text-white' classes from all tab links
                for (var j = 0; j < tabLinks.length; j++) {
                    tabLinks[j].classList.remove('active');
                    tabLinks[j].classList.remove('bg-primary');
                    tabLinks[j].classList.remove('text-white');
                }

                // Add 'active', 'bg-primary' and 'text-white' classes to the clicked tab link
                this.classList.add('active');
                this.classList.add('bg-primary');
                this.classList.add('text-white');

                // Get the target tab content and hide all other tab content
                var target = this.getAttribute('href');
                for (j = 0; j < tabContents.length; j++) {
                    if (tabContents[j].getAttribute('id') === target.substring(1)) {
                        tabContents[j].style.display = 'block';
                    } else {
                        tabContents[j].style.display = 'none';
                    }
                }

                // Change the get-form, input with id selected_tab value to the current tab index
                document.getElementById('selected_tab').value = Array.from(this.parentNode.parentNode.children).indexOf(this.parentNode);
            });
        }
    });
</script>
<script>
    document.getElementById("post-button")?.addEventListener("click", function (event) {
        event.preventDefault();
        // get the values from the get-form
        var from = document.getElementById("get-form").elements.namedItem("from").value;
        var to = document.getElementById("get-form").elements.namedItem("to").value;

        // get the selected tab's index
        var selectedTab = document.querySelector("#nav .nav-link.active");
        var selectedIndex = Array.from(selectedTab.parentNode.parentNode.children).indexOf(selectedTab.parentNode);

        console.log(selectedTab)
        console.log(selectedIndex)

        // modify the action attribute of the post-form to include query parameters
        var postForm = document.getElementById("post-form");
        var postAction = postForm.getAttribute("action");
        postAction += "&from=" + encodeURIComponent(from) + "&to=" + encodeURIComponent(to) + "&selected_tab=" + encodeURIComponent(selectedIndex);
        postForm.setAttribute("action", postAction);

        // submit the post-form
        postForm.submit();
    });
</script>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>
<c:url value="/css/profile.css" var="profileCss"/>

<c:url value="/patient-edit" var="patientEditUrl"/>
<c:url value="/change-password" var="changePasswordUrl"/>

<spring:message code="profile.profile" var="title"/>
<spring:message code="form.name" var="name"/>
<spring:message code="form.lastname" var="lastname"/>
<spring:message code="form.healthcare" var="healthcare"/>
<spring:message code="form.email" var="email"/>
<spring:message code="profile.saveChanges" var="saveChanges"/>
<spring:message code="edit.modal.title" var="modalTitle"/>
<spring:message code="edit.modal.text" var="modalDesc"/>
<spring:message code="profile.edit.modal.button" var="modalButton"/>
<spring:message code="profile.edit" var="editProfile"/>
<spring:message code="profile.changePassword" var="changePassword"/>

<!-- ALT img text -->
<spring:message code="user.alt.loggedUserImg" var="altLoggedUserImg"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${formsCss}" rel="stylesheet"/>
    <link href="${profileCss}" rel="stylesheet"/>
</head>
<body>
<jsp:include page="../components/header.jsp"/>

<div class="generalPadding">
    <h1>${title}</h1>
    <div class="card">
        <div class="profileContainer">
            <div class="profileImageContainer">
                <c:url value="/img/${patient.image.imageId == null ? \"patientDefault.png\" : patient.image.imageId}" var="loggedUserImg"/>
                <img src="${loggedUserImg}" alt="${altLoggedUserImg}" width="200" height="200" class="rounded-circle">
            </div>
            <div class="profileData">
                <div class="profileRow">
                    <div class="profileItem">
                        <label for="firstName">${name}</label>
                        <input class="form-control" id="firstName" type="text" value="${patient.firstName}" disabled/>
                    </div>
                    <div class="profileItem">
                        <label for="lastName">${lastname}</label>
                        <input class="form-control" id="lastName" type="text" value="${patient.lastName}" disabled/>
                    </div>
                </div>
                <div class="profileRow">
                    <div class="profileItem">
                        <label>${healthcare}</label>
                        <div class="chip">
                            <spring:message code="${patient.healthInsurance.messageID}"/>
                        </div>
                    </div>
                    <div class="profileItem">
                        <label for="email">${email}</label>
                        <input class="form-control" id="email" type="text" value='${patient.email}' disabled/>
                    </div>
                </div>
            </div>
        </div>



        <div class="profileButtonContainer">
            <a type="button" href="${patientEditUrl}" class="btn btn-primary">${editProfile}</a>
            <a type="button" href="${changePasswordUrl}" class="btn btn-primary">${changePassword}</a>
        </div>

    </div>
</div>
</body>
</html>


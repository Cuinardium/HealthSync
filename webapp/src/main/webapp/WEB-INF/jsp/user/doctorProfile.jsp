<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>
<c:url value="/css/profile.css" var="profileCss"/>

<c:url value="/doctor-edit" var="doctorEditUrl"/>
<c:url value="/change-password" var="changePasswordUrl"/>

<spring:message code="profile.profile" var="title"/>
<spring:message code="profile.personalInfo" var="personalInfo"/>
<spring:message code="form.name" var="name"/>
<spring:message code="form.lastname" var="lastname"/>
<spring:message code="form.city" var="city"/>
<spring:message code="profile.location" var="location"/>
<spring:message code="form.address" var="address"/>
<spring:message code="profile.workInfo" var="workInfo"/>
<spring:message code="form.healthcare" var="healthcare"/>
<spring:message code="form.specialization" var="specialization"/>
<spring:message code="form.email" var="email"/>
<spring:message code="profile.schedule" var="schedule"/>
<spring:message code="profile.saveChanges" var="saveChanges"/>
<spring:message code="edit.modal.title" var="modalTitle"/>
<spring:message code="edit.modal.text" var="modalDesc"/>
<spring:message code="profile.edit.modal.button" var="modalButton"/>
<spring:message code="profile.edit" var="editProfile"/>
<spring:message code="profile.changePassword" var="changePassword"/>

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

<!-- Content -->
<div class="formContainer generalPadding">
    <h1>${title}</h1>
    <div class="card">
        <div class="profileContainer">
            <div class="profileImageContainer">
                <c:url value="/img/${doctor.profilePictureId == null ? \"doctorDefault.png\" : doctor.profilePictureId}" var="userImg"/>
                <img src="${userImg}" width="200" height="200" class="rounded-circle">
            </div>

            <div class="profileData">

                <div class="profileItem">
                    <div class="profileTitle">
                        <strong>${personalInfo}</strong>
                        <i class="fa-solid fa-user"></i>
                    </div>
                </div>

                <div class="profileRow">
                    <div class="profileItem">
                        <label for="firstName">${name}</label>
                        <input class="form-control" id="firstName" type="text" value="${doctor.firstName}" disabled/>
                    </div>
                    <div class="profileItem">
                        <label for="lastName">${lastname}</label>
                        <input class="form-control" id="lastName" type="text" value="${doctor.lastName}" disabled/>
                    </div>
                </div>

                <div class="profileItem">
                    <label for="email">${email}</label>
                    <input class="form-control" id="email" type="text" value="${doctor.email}" disabled/>
                </div>


            </div>
        </div>

        <div class="doctorData">
            <div class="profileItem">
                <div class="profileTitle">
                    <strong>${location}</strong>
                    <i class="fa-solid fa-location-dot"></i>
                </div>
            </div>

            <div class="profileRow">
                <div class="profileItem">
                    <label for="city">${city}</label>
                    <spring:message code="${doctor.city.messageID}" var="doctorCity"/>
                    <input class="form-control" id="city" value="${doctorCity}" disabled/>
                </div>
                <div class="profileItem">
                    <label for="address">${address}</label>
                    <input class="form-control" id="address" type="text" value="${doctor.address}" disabled/>
                </div>
            </div>

            <div class="profileItem">
                <div class="profileTitle">
                    <strong>${workInfo}</strong>
                    <i class="fa-solid fa-user-doctor"></i>
                </div>
            </div>

            <div class="profileRow">
                <div class="profileItem">
                    <label for="specialtyCode">${specialization}</label>
                    <spring:message code="${doctor.specialty.messageID}" var="doctorSpecialty"/>
                    <input class="form-control" id="specialtyCode" value="${doctorSpecialty}" disabled/>
                </div>


                <div class="profileItem">
                    <label>${healthcare}</label>
                    <div class="chipsContainer">
                        <c:forEach items="${doctor.healthInsurances}" var="healthInsurance" varStatus="status">
                            <div class="chip">
                                <spring:message code="${healthInsurance.messageID}"/>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="profileItem">
                <div class="profileTitle">
                    <strong>${schedule}</strong>
                    <i class="fa-solid fa-calendar"></i>
                </div>
            </div>

            <div class="profileItem">
                <div class="scheduleContainer">
                    <c:set var="timeEnumValues" scope="request" value="${thirtyMinuteBlocks}"/>
                    <c:set var="dayEnumValues" scope="request" value="${days}"/>
                    <c:set var="selectedAttendingHours" scope="request" value="${doctor.attendingHours}"/>
                    <jsp:include page="../components/scheduleViewer.jsp"/>
                </div>
            </div>
        </div>

        <div class="profileButtonContainer">
            <a type="button" href="${doctorEditUrl}" class="btn btn-primary">${editProfile}</a>
            <a type="button" href="${changePasswordUrl}" class="btn btn-primary">${changePassword}</a>
        </div>
    </div>
</div>
</body>
</html>


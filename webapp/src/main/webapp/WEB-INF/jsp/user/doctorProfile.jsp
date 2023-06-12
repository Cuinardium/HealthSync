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
<c:url value="/add-vacation" var="addVacationUrl"/>

<spring:message code="profile.profile" var="title"/>
<spring:message code="profile.personalInfo" var="personalInfo"/>
<spring:message code="form.locale" var="locale"/>
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

<spring:message code="profile.vacation.successTitle" var="vacationSuccessTitle"/>
<spring:message code="profile.vacation.successDescription" var="vacationSuccessDescription"/>
<spring:message code="profile.vacation.title" var="vacationTitle"/>
<spring:message code="profile.vacation.date" var="vacationDate"/>
<spring:message code="profile.vacation.time" var="vacationTime"/>
<spring:message code="profile.vacation.from" var="vacationFrom"/>
<spring:message code="profile.vacation.to" var="vacationTo"/>
<spring:message code="profile.vacation.button" var="vacationButton"/>
<spring:message code="profile.vacation.cancel" var="vacationCancel"/>
<spring:message code="profile.vacation.invalidVacation" var="invalidVacationMessage"/>

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

    <script>
        $(document).ready(function () {
            if (${successModal}) {
                $('#successModal').modal('show');
            }
            if(${showVacationModal}) {
                $('#vacationModal').modal('show');
            }
        })

        function closeSuccessModal() {
            $('#successModal').modal('hide');
        }

        function openVacationModal() {
            $('#vacationModal').modal('show');
        }

        function closeVacationModal() {
            $('#vacationModal').modal('hide');
        }
    </script>

</head>

<body>
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<div class="formContainer generalPadding">
    <h1>${title}</h1>
    <div class="card">
        <div class="profileContainer">
            <div class="profileImageContainer">
                <c:url value="/img/${doctor.image.imageId == null ? \"doctorDefault.png\" : doctor.image.imageId}" var="loggedUserImg"/>
                <img src="${loggedUserImg}" alt="${altLoggedUserImg}" width="200" height="200" class="rounded-circle">
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

                <div class="profileRow">
                    <div class="profileItem">
                        <label for="email">${email}</label>
                        <input class="form-control" id="email" type="text" value="${doctor.email}" disabled/>
                    </div>
                    <div class="profileItem">
                        <label>${locale}</label>
                        <div class="chip">
                            ${doctor.locale}
                        </div>
                    </div>
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
            <button class="btn btn-primary" onclick="openVacationModal()">${vacationTitle}</button>
        </div>
    </div>
    <div class="modal fade" id="vacationModal" role="dialog" aria-labelledby="modalLabel"
         aria-hidden="true">
        <form:form modelAttribute="doctorVacationForm" action="${addVacationUrl}" method="post">
        <div class="modal-dialog" role="dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">${vacationTitle}</h5>
                </div>
                <div class="modal-body">
                        <label>${vacationFrom}</label>
                        <div class="profileRow">
                            <div class="profileItem">
                                <form:label path="fromDate">${vacationDate}</form:label>
                                <form:input path="fromDate" type="date" placeholder="${vacationDate}"/>
                                <form:errors path="fromDate"/>
                            </div>
                            <div class="profileItem">
                                <form:label path="fromTime">${vacationTime}</form:label>
                                <form:select path="fromTime" cssClass="form-select">
                                    <c:forEach items="${timeEnumValues}" var="timeBlock">
                                        <form:option value="${timeBlock.blockBeginning}">
                                            ${timeBlock.blockBeginning}
                                        </form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>
                        <label>${vacationTo}</label>
                        <div class="profileRow">
                            <div class="profileItem">
                                <form:label path="toDate">${vacationDate}</form:label>
                                <form:input path="toDate" type="date"/>
                                <form:errors path="toDate"/>
                            </div>
                            <div class="profileItem">
                                <form:label path="toTime">${vacationTime}</form:label>
                                <form:select path="toTime" cssClass="form-select">
                                    <c:forEach items="${timeEnumValues}" var="timeBlock">
                                        <form:option value="${timeBlock.blockBeginning}">
                                            ${timeBlock.blockBeginning}
                                        </form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>

                    <c:if test="${invalidVacation}">
                        <div class="profileItem">
                            <label class="error">${invalidVacationMessage}</label>
                        </div>
                    </c:if>
                </div>
                <div class="modal-footer">
                    <button type="button" onclick="closeVacationModal()"
                            class="btn btn-danger">${vacationCancel}</button>
                    <button type="submit" class="btn btn-primary">${vacationButton}</button>
                </div>
                </form:form>
            </div>
        </div>
    </div>
    <div class="modal fade" id="successModal" role="dialog" aria-labelledby="modalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">${vacationSuccessTitle}</h5>
                </div>
                <div class="modal-body">
                    ${vacationSuccessDescription}
                </div>
                <div class="modal-footer">
                    <button type="button" onclick="closeSuccessModal()"
                            class="btn btn-primary">${modalButton}</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>


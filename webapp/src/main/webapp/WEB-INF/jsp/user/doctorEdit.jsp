<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>
<c:url value="/css/profile.css" var="profileCss"/>
<c:url value="/css/profileEdit.css" var="profileEditCss"/>
<c:url value="/js/profileEdit.js" var="profileEditJs"/>

<c:url value="/doctor-edit" var="doctorEditUrl"/>
<c:url value="/doctor-profile" var="doctorProfileUrl"/>

<spring:message code="profile.edit" var="title"/>
<spring:message code="profile.personalInfo" var="personalInfo"/>
<spring:message code="form.locale" var="locale"/>
<spring:message code="form.name" var="name"/>
<spring:message code="form.name_hint" var="name_hint"/>
<spring:message code="form.lastname" var="lastname"/>
<spring:message code="form.lastname_hint" var="lastname_hint"/>
<spring:message code="form.city" var="city"/>
<spring:message code="form.city_hint" var="city_hint"/>
<spring:message code="profile.location" var="location"/>
<spring:message code="form.address" var="address"/>
<spring:message code="form.address_hint" var="address_hint"/>
<spring:message code="profile.workInfo" var="workInfo"/>
<spring:message code="form.specialization" var="specialization"/>
<spring:message code="form.specialization_hint" var="specialization_hint"/>
<spring:message code="form.email" var="email"/>
<spring:message code="form.email_hint" var="email_hint"/>
<spring:message code="profile.schedule" var="schedule"/>
<spring:message code="profile.saveChanges" var="saveChanges"/>
<spring:message code="edit.modal.title" var="modalTitle"/>
<spring:message code="edit.modal.text" var="modalDesc"/>
<spring:message code="profile.edit.modal.button" var="modalButton"/>
<spring:message code="profile.edit.emailInUse.error" var="emailAlreadyInUseError"/>

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
    <link href="${profileEditCss}" rel="stylesheet"/>

    <script>
        $(document).ready(function () {
            if (${showModal}) {
                $('#modal').modal('show');
            }
        })
    </script>
    <script src="${profileEditJs}"></script>
</head>

<body>
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<div class="formContainer generalPadding">
    <div class="backButtonContainer">
        <a href="${doctorProfileUrl}" class="btn btn-primary backButton">
            <i class="fa-solid fa-arrow-left"></i>
        </a>
    </div>
    <h1>${title}</h1>
    <form:form modelAttribute="doctorEditForm" class="card" action="${doctorEditUrl}"
               method="POST" enctype="multipart/form-data">

        <div class="profileContainer">
            <div class="profileImageContainer">
                <c:url value="/img/${user.image == null ? \"patientDefault.png\" : user.image.imageId}"
                       var="loggedUserImg"/>
                <img src="${loggedUserImg}" alt="${altLoggedUserImg}" id="imgPreview" width="200" height="200"
                     class="rounded-circle">
                <div class="pfpEdit doctorPfpEdit">
                    <form:label path="image">
                        <span class="fa-stack">
                            <i class="fa fa-circle fa-2xl fa-stack-1x"></i>
                            <i class="fa-solid fa-pen fa-lg fa-stack-1x"></i>
                        </span>
                    </form:label>
                    <form:input type="file" placeholder="${image_hint}" path="image"/>
                </div>
                <form:errors path="image" cssClass="error" element="p"/>
            </div>

            <div class="profileData">
                <div class="profileTitle">
                    <strong>${personalInfo}</strong>
                    <i class="fa-solid fa-user"></i>
                </div>

                <div class="profileRow">
                    <div class="profileItem">
                        <form:label path="name">${name}</form:label>
                        <form:input class="form-control" type="text" placeholder="${name_hint}" path="name"/>
                        <form:errors path="name" cssClass="error" element="p"/>
                    </div>
                    <div class="profileItem">
                        <form:label path="lastname">${lastname}</form:label>
                        <form:input class="form-control" path="lastname" type="text" placeholder="${lastname_hint}"/>
                        <form:errors path="lastname" cssClass="error" element="p"/>
                    </div>
                </div>

                <div class="profileRow">
                    <div class="profileItem">
                        <form:label path="email">${email}</form:label>
                        <form:input class="form-control" path="email" type="text" placeholder="${email_hint}"/>
                        <form:errors path="email" cssClass="error" element="p"/>
                        <c:if test="${emailAlreadyInUse}">
                            <div class="formRow">
                                <p class="error">
                                        ${emailAlreadyInUseError}
                                </p>
                            </div>
                        </c:if>
                    </div>
                    <div class="profileItem">
                        <form:label path="locale">${locale}</form:label>
                        <form:select class="form-select" path="locale">
                            <!-- TODO: make it not hard coded? -->
                            <form:option value="es"> es </form:option>
                            <form:option value="en"> en </form:option>
                        </form:select>
                    </div>
                </div>
            </div>
        </div>

        <div class="doctorData">
            <div class="profileTitle titleMarginTop">
                <strong>${location}</strong>
                <i class="fa-solid fa-location-dot"></i>
            </div>

            <div class="profileRow">
                <div class="profileItem">
                    <form:label path="cityCode">${city}</form:label>
                    <form:select class="form-select" path="cityCode">
                        <form:option value="-1" disabled="true" hidden="true"> -- </form:option>
                        <c:forEach items="${cities}" var="city" varStatus="status">
                            <form:option value="${status.index}">
                                <spring:message code="${city.messageID}"/>
                            </form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="cityCode" cssClass="error" element="p"/>
                </div>
                <div class="profileItem">
                    <form:label path="address">${address}</form:label>
                    <form:input class="form-control" path="address" type="text" placeholder="${address_hint}"/>
                    <form:errors path="address" cssClass="error" element="p"/>
                </div>
            </div>

            <div class="profileTitle titleMarginTop">
                <strong>${workInfo}</strong>
                <i class="fa-solid fa-user-doctor"></i>
            </div>

            <div class="profileRow">
                <div class="profileItem">
                    <form:label path="specialtyCode">${specialization}</form:label>
                    <form:select class="form-select" path="specialtyCode">
                        <form:option value="-1" disabled="true" hidden="true"> -- </form:option>
                        <c:forEach items="${specialties}" var="specialty" varStatus="status">
                            <form:option value="${status.index}">
                                <spring:message code="${specialty.messageID}"/>
                            </form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="specialtyCode" cssClass="error" element="p"/>
                </div>


                <div class="profileItem">
                    <!-- Health Insurance Picker -->
                    <c:set var="healthInsurances" value="${healthInsurances}" scope="request"/>
                    <c:set var="currentHealthInsuranceCodes" value="${currentHealthInsuranceCodes}" scope="request"/>
                    <jsp:include page="../components/healthInsurancePicker.jsp"/>
                </div>
            </div>

            <div class="profileTitle titleMarginTop">
                <strong>${schedule}</strong>
                <i class="fa-solid fa-calendar"></i>
            </div>

            <!-- Doctor schedule selector -->
            <c:set var="timeEnumValues" value="${timeEnumValues}" scope="request"/>
            <jsp:include page="../components/scheduleSelector.jsp"/>
        </div>

        <div class="profileButtonContainer">
            <button type="submit" class="btn btn-primary submitButton">${saveChanges}</button>
        </div>
    </form:form>

    <div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="modalLabel"
         aria-hidden="true" data-bs-backdrop="static">
        <div class="modal-dialog" role="dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalLabel">${modalTitle}</h5>
                </div>
                <div class="modal-body">
                    ${modalDesc}
                </div>
                <div class="modal-footer">
                    <a type="button" href="${doctorProfileUrl}" class="btn btn-primary">${modalButton}</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

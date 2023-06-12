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

<c:url value="/patient-edit" var="patientEditUrl"/>
<c:url value="/patient-profile" var="patientProfileUrl"/>

<spring:message code="profile.edit" var="title"/>
<spring:message code="form.name" var="name"/>
<spring:message code="form.name_hint" var="name_hint"/>
<spring:message code="form.lastname" var="lastname"/>
<spring:message code="form.lastname_hint" var="lastname_hint"/>
<spring:message code="form.healthcare" var="healthcare"/>
<spring:message code="form.locale" var="locale"/>
<spring:message code="form.healthcare_hint" var="healthcare_hint"/>
<spring:message code="form.email" var="email"/>
<spring:message code="form.email_hint" var="email_hint"/>
<spring:message code="profile.saveChanges" var="saveChanges"/>
<spring:message code="edit.modal.title" var="modalTitle"/>
<spring:message code="edit.modal.text" var="modalDesc"/>
<spring:message code="profile.edit.modal.button" var="modalButton"/>
<spring:message code="profile.edit.emailInUse.error" var="emailAlreadyInUseError"/>
<spring:message code="profile.personalInfo" var="personalInfo"/>

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

<div class="formContainer generalPadding">
    <div class="backButtonContainer">
        <a href="${patientProfileUrl}" class="btn btn-primary backButton">
            <i class="fa-solid fa-arrow-left"></i>
        </a>
    </div>
    <h1>${title}</h1>
    <form:form modelAttribute="patientEditForm" class="card" action="${patientEditUrl}" method="POST"
               enctype="multipart/form-data">
        <div class="profileContainer">
            <div class="profileImageContainer">
                <c:url value="/img/${user.image == null ? \"patientDefault.png\" : user.image.imageId}"
                       var="loggedUserImg"/>
                <img src="${loggedUserImg}" alt="${altLoggedUserImg}" id="imgPreview" width="200" height="200"
                     class="rounded-circle">
                <div class="pfpEdit">
                    <form:label path="image">
                        <span class="fa-stack">
                            <i class="fa fa-circle fa-2xl fa-stack-1x"></i>
                            <i class="fa-solid fa-pen fa-lg fa-stack-1x"></i>
                        </span>
                    </form:label>
                    <form:input type="file" placeholder="${image_hint}" path="image"/>
                </div>
            </div>
            <form:errors path="image" cssClass="error" element="p"/>
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
                        <form:input id="imgUpload" class="form-control" path="lastname" type="text"
                                    placeholder="${lastname_hint}"/>
                        <form:errors path="lastname" cssClass="error" element="p"/>
                    </div>
                </div>
                <div class="profileRow">
                    <div class="profileItem">
                        <form:label path="healthInsuranceCode">${healthcare}</form:label>
                        <form:select class="form-select" path="healthInsuranceCode">
                            <form:option value="-1" disabled="true" hidden="true"> -- </form:option>
                            <c:forEach items="${healthInsurances}" var="healthInsurance" varStatus="status">
                                <form:option value="${status.index}">
                                    <spring:message code="${healthInsurance.messageID}"/>
                                </form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors path="healthInsuranceCode" cssClass="error" element="p"/>
                    </div>
                    <div class="profileItem">
                        <form:label path="email">${email}</form:label>
                        <form:input class="form-control" path="email" type="text" placeholder='${email_hint}'/>
                        <form:errors path="email" cssClass="error" element="p"/>
                        <c:if test="${emailAlreadyInUse}">
                            <div class="formRow">
                                <p class="error">
                                        ${emailAlreadyInUseError}
                                </p>
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="profileRow">
                    <div class="profileItem">
                        <form:label path="locale">${locale}</form:label>
                        <form:select class="form-select" path="locale">
                            <!-- TODO: make it not hard coded? -->
                            <form:option value="es"> es </form:option>
                            <form:option value="en"> en </form:option>
                        </form:select>
                    </div>
                    <!-- for alignment purposes -->
                    <div class="profileItem">
                    </div>
                </div>
            </div>
        </div>

        <div class="profileButtonContainer">
            <button type="submit" class="btn btn-primary">${saveChanges}</button>
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
                    <a type="button" href="${patientProfileUrl}" class="btn btn-primary">${modalButton}</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

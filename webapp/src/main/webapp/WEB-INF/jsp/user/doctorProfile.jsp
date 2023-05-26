<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>

<c:url value="/doctor-edit" var="doctorEditUrl"/>
<c:url value="/change-password" var="changePasswordUrl"/>

<spring:message code="profile.profile" var="title"/>
<spring:message code="form.name" var="name"/>
<spring:message code="form.name_hint" var="name_hint"/>
<spring:message code="form.lastname" var="lastname"/>
<spring:message code="form.lastname_hint" var="lastname_hint"/>
<spring:message code="form.city" var="city"/>
<spring:message code="form.city_hint" var="city_hint"/>
<spring:message code="form.address" var="address"/>
<spring:message code="form.address_hint" var="address_hint"/>
<spring:message code="form.specialization" var="specialization"/>
<spring:message code="form.specialization_hint" var="specialization_hint"/>
<spring:message code="form.email" var="email"/>
<spring:message code="form.email_hint" var="email_hint"/>
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

</head>

<body>
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<div class="formContainer generalPadding">
    <h1>${title}</h1>
    <form:form modelAttribute="doctorEditForm" class="card" enctype="multipart/form-data">
        <div class="formCol">
            <div class="formItem">
                <c:url value="/img/${user.getProfilePictureId() == null ? \"doctorDefault.png\" : user.getProfilePictureId()}" var="userImg"/>
                <img src="${userImg}" width="100" height="100" class="rounded-circle">
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="name">${name}</form:label>
                <form:input class="form-control" type="text" placeholder="${name_hint}" path="name" disabled="true"/>
            </div>
            <div class="formItem">
                <form:label path="lastname">${lastname}</form:label>
                <form:input class="form-control" path="lastname" type="text" placeholder="${lastname_hint}" disabled="true"/>
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="cityCode">${city}</form:label>
                <form:select class="form-select" path="cityCode" disabled="true">
                    <c:forEach items="${cities}" var="city" varStatus="status">
                        <form:option value="${status.index}">
                            <spring:message code="${city.messageID}"/>
                        </form:option>
                    </c:forEach>
                </form:select>
            </div>
            <div class="formItem">
                <form:label path="address">${address}</form:label>
                <form:input class="form-control" path="address" type="text" placeholder="${address_hint}" disabled="true"/>
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="specialtyCode">${specialization}</form:label>
                <form:select class="form-select" path="specialtyCode" disabled="true">
                    <c:forEach items="${specialties}" var="specialty" varStatus="status">
                        <form:option value="${status.index}">
                            <spring:message code="${specialty.messageID}"/>
                        </form:option>
                    </c:forEach>
                </form:select>
            </div>

            <!-- Health Insurance Picker -->
            <c:set var="healthInsurances" value="${healthInsurances}" scope="request"/>
            <c:set var="currentHealthInsuranceCodes" value="${currentHealthInsuranceCodes}" scope="request"/>
            <jsp:include page="../components/healthInsuranceViewer.jsp"/>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="email">${email}</form:label>
                <form:input class="form-control" path="email" type="text" placeholder="${email_hint}" disabled="true"/>
            </div>
        </div>

        <div class="formRow">
            <a type="button" href="${doctorEditUrl}" class="btn btn-primary">${editProfile}</a>
            <a type="button" href="${changePasswordUrl}" class="btn btn-primary">${changePassword}</a>
        </div>
    </form:form>
</div>
</body>
</html>


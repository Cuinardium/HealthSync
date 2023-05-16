<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>

<c:url value="/doctor-edit" var="doctorEditUrl"/>

<spring:message code="profile.edit" var="title"/>
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
    <form:form modelAttribute="doctorEditForm" class="card" action="${doctorEditUrl}"
               method="POST" enctype="multipart/form-data">
        <div class="formCol">
            <div class="formItem">
                <c:url value="/img/${user.getProfilePictureId() == null ? \"doctorDefault.png\" : user.getProfilePictureId()}" var="userImg"/>
                <img src="${userImg}" width="100" height="100" class="rounded-circle">
            </div>
            <div class="formItem">
                <form:label path="image">${image}</form:label>
                <form:input class="form-control" type="file" placeholder="${image_hint}" path="image"/>
                <form:errors path="image" cssClass="error" element="p"/>
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="name">${name}</form:label>
                <form:input class="form-control" type="text" placeholder="${name_hint}" path="name"/>
                <form:errors path="name" cssClass="error" element="p"/>
            </div>
            <div class="formItem">
                <form:label path="lastname">${lastname}</form:label>
                <form:input class="form-control" path="lastname" type="text" placeholder="${lastname_hint}"/>
                <form:errors path="lastname" cssClass="error" element="p"/>
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
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
            <div class="formItem">
                <form:label path="address">${address}</form:label>
                <form:input class="form-control" path="address" type="text" placeholder="${address_hint}"/>
                <form:errors path="address" cssClass="error" element="p"/>
            </div>
        </div>
        <div class="formRow">
            <div class="formItem">
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

            <!-- Health Insurance Picker -->
            <c:set var="healthInsurances" value="${healthInsurances}" scope="request"/>
            <c:set var="currentHealthInsuranceCodes" value="${currentHealthInsuranceCodes}" scope="request"/>
            <jsp:include page="../components/healthInsurancePicker.jsp"/>
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="email">${email}</form:label>
                <form:input class="form-control" path="email" type="text" placeholder="${email_hint}"/>
                <form:errors path="email" cssClass="error" element="p"/>
            </div>
        </div>

        <!-- Doctor schedule selector -->
        <c:set var="timeEnumValues" value="${timeEnumValues}" scope="request"/>
        <jsp:include page="../components/scheduleSelector.jsp"/>

        <button type="submit" class="btn btn-primary submitButton">${saveChanges}</button>
    </form:form>
</div>
</body>
</html>

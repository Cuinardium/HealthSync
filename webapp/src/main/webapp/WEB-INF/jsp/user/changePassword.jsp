<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>


<c:url value="/change-password" var="changePasswordUrl"/>
<c:url value="/" var="successfulUrl"/>

<spring:message code="profile.changePassword" var="title"/>
<spring:message code="form.password" var="password"/>
<spring:message code="form.password_hint" var="password_hint"/>
<spring:message code="form.cpassword" var="cpassword"/>
<spring:message code="form.cpassword_hint" var="cpassword_hint"/>
<spring:message code="profile.saveChanges" var="saveChanges"/>
<spring:message code="profile.oldPassword" var="oldPassword"/>
<spring:message code="profile.oldPassword_hint" var="oldPassword_hint"/>
<spring:message code="profile.oldPasswordDoesNotMatchError" var="oldPasswordDoesNotMatchError"/>
<spring:message code="edit.modal.title" var="modalTitle"/>
<spring:message code="edit.modal.text" var="modalDesc"/>
<spring:message code="register.modal.button" var="modalButton"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${formsCss}" rel="stylesheet"/>

    <script>
        $(document).ready(function(){
            if(${showModal}){
                $('#modal').modal('show');
            }
        })
    </script>
</head>
<body>
<jsp:include page="../components/header.jsp"/>

<!-- Content -->
<div class="formContainer generalPadding">
    <h1>${title}</h1>
<form:form modelAttribute="changePasswordForm" class="card" action="${changePasswordUrl}" method="POST">
    <div class="formRow">
        <div class="formItem">
            <form:label path="oldPassword">${oldPassword}</form:label>
            <form:input class="form-control" type="password" path="oldPassword" placeholder="${oldPassword_hint}"/>
            <form:errors path="oldPassword" cssClass="error" element="p"/>
            <c:if test="${oldPasswordDoesNotMatch}">
                <p class="error">
                    ${oldPasswordDoesNotMatchError}
                </p>
            </c:if>
        </div>
    </div>
    <div class="formRow">
        <div class="formItem">
            <form:label path="password">${password}</form:label>
            <form:input class="form-control" type="password" path="password" placeholder="${password_hint}"/>
            <form:errors path="password" cssClass="error" element="p"/>
        </div>
    </div>
    <div class="formRow">
        <div class="formItem">
            <form:label path="confirmPassword">${cpassword}</form:label>
            <form:input class="form-control" path="confirmPassword" type="password" placeholder="${cpassword_hint}"/>
            <form:errors path="confirmPassword" cssClass="error" element="p"/>
            <form:errors cssClass="error" element="p"/>
        </div>
    </div>
    <button type="submit" class="btn btn-primary submitButton">${saveChanges}</button>
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
                    <a type="button" href="${successfulUrl}" class="btn btn-primary">${modalButton}</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

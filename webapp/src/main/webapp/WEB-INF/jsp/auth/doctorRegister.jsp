<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>


<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>

<c:url value="/doctor-register" var="doctorRegisterUrl"/>
<c:url value="/" var="successfulUrl"/>

<spring:message code="registerMedic.title" var="title"/>
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
<spring:message code="form.password" var="password"/>
<spring:message code="form.password_hint" var="password_hint"/>
<spring:message code="form.cpassword" var="cpassword"/>
<spring:message code="form.cpassword_hint" var="cpassword_hint"/>
<spring:message code="registerMedic.submit" var="submit"/>
<spring:message code="register.modal.title" var="modalTitle"/>
<spring:message code="register.modal.text" var="modalDesc"/>
<spring:message code="register.modal.button" var="modalButton"/>
<spring:message code="register.emailInUse.error" var="emailAlreadyInUseError"/>

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
    <form:form modelAttribute="doctorRegisterForm" id="regForm" class="card" action="${doctorRegisterUrl}"
               method="POST">
        <c:if test="${emailAlreadyInUse}">
          <div class="formRow">
            <p class="error">
              ${emailAlreadyInUseError}
            </p>
          </div>
        </c:if>
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
            <!-- dropdown menu -->
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
        </div>
        <div class="formRow">
            <div class="formItem">
                <form:label path="email">${email}</form:label>
                <form:input class="form-control" path="email" type="text" placeholder="${email_hint}"/>
                <form:errors path="email" cssClass="error" element="p"/>
            </div>
            <div class="formItem">
                <form:label path="password">${password}</form:label>
                <form:input class="form-control" path="password" type="password" placeholder="${password_hint}"/>
                <form:errors path="password" cssClass="error" element="p"/>
            </div>
            <div class="formItem">
                <form:label path="confirmPassword">${cpassword}</form:label>
                <form:input class="form-control" path="confirmPassword" type="password"
                            placeholder="${cpassword_hint}"/>
                <form:errors path="confirmPassword" cssClass="error" element="p"/>
                <form:errors/>
            </div>
        </div>
        <div class="formRow">
            <!-- Health Insurance Picker -->
            <c:set var="healthInsurances" value="${healthInsurances}" scope="request"/>
            <c:set var="currentHealthInsuranceCodes" value="${currentHealthInsurances}" scope="request"/>
            <div class="formItem">
                <jsp:include page="../components/healthInsurancePicker.jsp"/>
            </div>
        </div>
            <!-- Doctor schedule selector -->
            <c:set var="timeEnumValues" value="${timeEnumValues}" scope="request"/>
            <jsp:include page="../components/scheduleSelector.jsp"/>

        <button type="submit" class="btn btn-primary submitButton">${submit}</button>

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
</div>
</body>
</html>


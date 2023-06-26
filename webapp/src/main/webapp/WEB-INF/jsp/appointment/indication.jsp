<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="indication.title" var="title"/>
<spring:message code="indication.description" var="description"/>
<spring:message code="indication.description.placeholder" var="descriptionPlaceholder"/>
<spring:message code="indication.submit" var="submit"/>

<spring:message code="indication.modal.title" var="modalTitle"/>
<spring:message code="indication.modal.text" var="modalDesc"/>
<spring:message code="indication.modal.button" var="modalButton"/>


<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>

<c:url value="/${appointmentId}/indication" var="indicationUrl"/>
<c:url value="/${appointmentId}/detailed-appointment" var="appointmentUrl"/>

<html>
<head>
    <title>${title}</title>

    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${formsCss}" rel="stylesheet"/>

    <script>
        $(document).ready(function(){
            if(${showModal}){
                $('#modalSuccess').modal('show');
            }
        })
    </script>
</head>
<body>
<jsp:include page="../components/header.jsp"/>
<div class="generalPadding">
    <div class="backButtonContainer">
        <a href="${appointmentUrl}" class="btn btn-primary backButton">
            <i class="fa-solid fa-arrow-left"></i>
        </a>
    </div>
    <div class="formContainer">
        <h1>${title}</h1>
        <form:form modelAttribute="indicationForm" class="card" action="${indicationUrl}"
                   method="POST">

            <div class="formItem">
                <form:label cssClass="formLabel" path="indications">${description}</form:label>
                <form:textarea cols="50" rows="10" class="form-control" type="text" placeholder="${descriptionPlaceholder}" path="indications"/>
                <form:errors path="indications" cssClass="error" element="p"/>
            </div>

            <button type="submit" class="btn btn-primary submitButton">${submit}</button>
        </form:form>

        <div class="modal fade" id="modalSuccess" tabindex="-1" role="dialog" aria-labelledby="modalLabel"
             aria-hidden="true" data-bs-backdrop="static">
            <div class="modal-dialog" role="dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalLabelSuccess">${modalTitle}</h5>
                    </div>
                    <div class="modal-body">
                        ${modalDesc}
                    </div>
                    <div class="modal-footer">
                        <a type="button" href="${appointmentUrl}" class="btn btn-primary">${modalButton}</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="review.title" var="title"/>
<spring:message code="review.rating" var="rating"/>
<spring:message code="review.description" var="description"/>
<spring:message code="review.description.placeholder" var="descriptionPlaceholder"/>
<spring:message code="review.submit" var="submit"/>

<spring:message code="review.modal.title" var="modalTitle"/>
<spring:message code="review.modal.text" var="modalDesc"/>
<spring:message code="review.modal.button" var="modalButton"/>



<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>
<c:url value="/css/review.css" var="reviewCss"/>
<c:url value="/js/review.js" var="reviewJs"/>

<c:url value="/${doctorId}/review" var="reviewUrl"/>
<c:url value="/${doctorId}/detailed-doctor" var="doctorUrl"/>

<html>
<head>
    <title>${title}</title>

    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${formsCss}" rel="stylesheet"/>
    <link href="${reviewCss}" rel="stylesheet"/>

    <script src="${reviewJs}"></script>

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
        <a href="${doctorUrl}" class="btn btn-primary backButton">
            <i class="fa-solid fa-arrow-left"></i>
        </a>
    </div>
    <div class="formContainer">
        <h1>${title}</h1>
        <form:form modelAttribute="reviewForm" class="card" action="${reviewUrl}"
                   method="POST">

            <div class="formItem">
                <form:label cssClass="formLabel" path="rating">${rating}</form:label>
                <div class="starContainer">
                    <form:input id="ratingInput" path="rating" hidden="true"/>
                    <c:forEach begin="1" end="5" step="1" var="i">
                        <div class="star ${selectedRating >= i ? "selected" : "unselected"}" data-index="${i}">
                            <i class="fa fa-3x fa-star"></i>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="formItem">
                <form:label cssClass="formLabel" path="description">${description}</form:label>
                <form:textarea cols="50" rows="10" class="form-control" type="text" placeholder="${descriptionPlaceholder}" path="description"/>
                <form:errors path="description" cssClass="error" element="p"/>
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
                        <a type="button" href="${doctorUrl}" class="btn btn-primary">${modalButton}</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

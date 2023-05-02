<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="registerCss"/>

<c:url value="/${doctorId}/appointment" var="appointmentUrl"/>

<spring:message code="appointment.title" var="title"/>
<spring:message code="form.date" var="date"/>
<spring:message code="form.desc" var="desc"/>
<spring:message code="form.hour" var="hour"/>
<spring:message code="form.noHourAvailable" var="noHourAvailable"/>
<spring:message code="form.desc_hint" var="desc_hint"/>
<spring:message code="appointment.submit" var="submit"/>

<html>
<head>
    <title>${title}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <link href="${registerCss}" rel="stylesheet"/>
</head>
<body>
<jsp:include page="../components/header.jsp"/>

<div class="page-content p-5" id="content">
    <div class="bcontent">
        <h1>${title}</h1>


        <form:form modelAttribute="appointmentForm" action="${appointmentUrl}" method="post">
            <div class="form-container card d-flex align-items-start">
                <div class="pt-3 form-row container justify-content-between">
                    <div class="form-item">
                        <form:label path="date" class="tag">${date}</form:label>
                        <div>
                            <form:input path="date" class="label" type="date" onchange="getWithDate(event, this)"/>
                            <form:errors/>
                        </div>
                    </div>


                    <c:choose>
                        <c:when test="${empty availableHours}">
                            <div class="form-item ">
                                <label class="tag">${hour}</label>
                                <div class="alert alert-info align-self-center">
                                        ${noHourAvailable}
                                </div>
                            </div>

                        </c:when>
                        <c:otherwise>
                            <div class="form-item">
                                <form:label path="block" class="tag">${hour}</form:label>
                                <form:select id="block" path="block" class="form-control form-control-lg">
                                    <form:option value="-1" disabled="true" hidden="true"> -- </form:option>
                                    <c:forEach items="${availableHours}" var="timeBlock" varStatus="status">
                                        <form:option value="${timeBlock.blockBeginning}">
                                            ${timeBlock.blockBeginning}
                                        </form:option>
                                    </c:forEach>
                                </form:select>
                                <form:errors path="block" cssClass="error" element="p"/>
                            </div>

                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="form-row container justify-content-between">
                    <div class="pl- form-item">
                        <form:label path="description" class="tag">${desc}</form:label>
                        <div>
                            <form:textarea path="description" id="description" class="label" type="text"
                                           placeholder="${desc_hint}"/>
                            <form:errors path="description" cssClass="error" element="p"/>
                        </div>
                    </div>
                </div>

                <div class="p-2 flex-d align-self-end">
                  <button type="submit" class="btn btn-primary" value="Create appointment">${submit}</button>
                </div>
            </div>
        </form:form>
    </div>
</div>
</body>

<!-- JS -->
<script>
    function getWithDate(ev, object) {
        console.log(object.id + " - " + ev + ": " + object.value);
        window.location.replace("${appointmentUrl}?date=" + object.value + "&desc=" + document.getElementById("description").value);
    }
</script>

</html>



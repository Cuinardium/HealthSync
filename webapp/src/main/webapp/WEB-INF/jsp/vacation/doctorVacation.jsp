<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Include -->
<jsp:include page="/resources/externalResources.jsp"/>

<!--Variables -->
<c:url value="/css/main.css" var="mainCss"/>
<c:url value="/css/forms.css" var="formsCss"/>
<c:url value="/css/profile.css" var="profileCss"/>


<c:url value="/doctor-vacation" var="addVacationUrl"/>
<c:url value="/delete-vacation" var="deleteVacationUrl"/>
<c:url value="/cancel-vacation-appointments" var="cancelVacationAppointmentsUrl"/>

<spring:message code="vacation.title" var="title"/>
<spring:message code="vacation.addSuccessTitle" var="vacationAddSuccessTitle"/>
<spring:message code="vacation.addSuccessDescription" var="vacationAddSuccessDescription"/>
<spring:message code="vacation.addTitle" var="vacationAddTitle"/>
<spring:message code="vacation.date" var="vacationDate"/>
<spring:message code="vacation.time" var="vacationTime"/>
<spring:message code="vacation.from" var="vacationFrom"/>
<spring:message code="vacation.to" var="vacationTo"/>
<spring:message code="vacation.addButton" var="vacationAddButton"/>
<spring:message code="vacation.cancel" var="vacationCancel"/>
<spring:message code="vacation.addInvalidVacation" var="addInvalidVacationMessage"/>
<spring:message code="profile.edit.modal.button" var="modalButton"/>
<spring:message code="vacation.appointmentsInVacation" var="appointmentsInVacation"/>
<spring:message code="vacation.appointmentsInVacationCancelReasonTitle" var="appointmentCancelReason"/>
<spring:message code="vacation.cancelAppointmentsInVacation" var="cancelAppointments"/>
<spring:message code="vacation.doNotCancelAppointmentsInVacation" var="doNotCancelAppointments"/>
<spring:message code="vacation.noVacations" var="noVacations"/>
<spring:message code="vacation.deleteTitle" var="deleteTitle"/>
<spring:message code="vacation.deleteDescription" var="deleteDescription"/>
<spring:message code="vacation.deleteDeny" var="deleteDeny"/>
<spring:message code="vacation.deleteConfirm" var="deleteConfirm"/>

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
            if (${showVacationSuccessModal}) {
                $('#addVacationSuccessModal').modal('show');
            }
            if (${showAddVacationModal}) {
                $('#addVacationModal').modal('show');
            }
        })

        function closeAddVacationSuccessModal() {
            $('#addVacationSuccessModal').modal('hide');
        }

        function openAddVacationModal() {
            $('#addVacationModal').modal('show');
        }

        function closeAddVacationModal() {
            $('#addVacationModal').modal('hide');
        }

        function openDeleteVacationModal(i) {
            $('#' + i).modal('show')
        }

        function closeDeleteVacationModal() {
            $('#deleteVacationModal').modal('hide')
        }

        function showCancelReason() {
            document.getElementById('appointmentCancelReason').classList.remove('hidden');
        }

        function hideCancelReason() {
            document.getElementById('appointmentCancelReason').classList.add('hidden');
        }

    </script>
</head>
<body>

<jsp:include page="../components/header.jsp"/>

<div class="generalPadding vacationContainer">
    <div class="buttonsContainer">
        <button class="btn btn-primary" onclick="openAddVacationModal()">${vacationAddTitle}</button>
    </div>
    <c:forEach items="${vacations}" var="vacation" varStatus="status">
        <div class="card">
            <div class="card-body">
                <div class="vacationCardContent">
                    <div class="vacationCardItem">
                        <strong>${vacationFrom}</strong>
                        <div>
                                ${vacation.fromDate} ${vacation.fromTime.blockBeginning}
                        </div>

                    </div>
                    <div class="vacationCardItem">
                        <strong>${vacationTo}</strong>
                        <div>
                                ${vacation.toDate} ${vacation.toTime.blockEnd}
                        </div>

                    </div>
                        <button onclick="openDeleteVacationModal('${status.index}')"
                                class="btn btn-danger btn-sm">
                                ${vacationCancel}
                        </button>
                </div>
            </div>
        </div>

        <div class="modal fade" id="${status.index}" tabindex="-1" role="dialog"
             aria-labelledby="modalLabel"
             aria-hidden="true">
            <div class="modal-dialog" role="dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalLabel">${deleteTitle}</h5>
                    </div>
                    <form:form modelAttribute="deleteVacationForm" action="${deleteVacationUrl}" method="post">
                        <form:input path="fromDate" hidden="true" value="${vacation.fromDate}"/>
                        <form:input path="fromTime" hidden="true" value="${vacation.fromTime.blockBeginning}"/>
                        <form:input path="toDate" hidden="true" value="${vacation.toDate}"/>
                        <form:input path="toTime" hidden="true" value="${vacation.toTime.blockBeginning}"/>

                        <div class="modal-body">
                                ${deleteDescription}
                        </div>
                        <div class="modal-footer">
                            <div class="cardButtonContainer">
                                <button type="button" class="btn btn-danger"
                                        onclick="closeModal()">${deleteDeny}</button>
                                <button type="submit"
                                        class="btn btn-primary">${deleteConfirm}</button>
                            </div>

                        </div>
                    </form:form>
                </div>
            </div>
        </div>

    </c:forEach>
    <c:if test="${empty vacations}">
        <div class="noVacationsMsg">
            <div class="alert alert-info">${noVacations}</div>
        </div>
    </c:if>


</div>


<div class="modal fade" id="addVacationModal" role="dialog" aria-labelledby="modalLabel"
     aria-hidden="true">

    <form:form modelAttribute="doctorVacationForm" action="${addVacationUrl}" method="post">
    <div class="modal-dialog" role="dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">${vacationAddTitle}</h5>
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
                <label>${appointmentsInVacation}</label>
                <div class="profileRow">
                    <div class="profileItem">
                        <form:radiobutton path="cancelAppointmentsInVacation" value="true"
                                          onclick="showCancelReason()"/>
                            ${cancelAppointments}
                    </div>
                    <div class="profileItem">
                        <form:radiobutton path="cancelAppointmentsInVacation" value="false"
                                          onclick="hideCancelReason()"/>
                            ${doNotCancelAppointments}
                    </div>
                </div>

                <c:if test="${doctorVacationForm.cancelAppointmentsInVacation}">
                    <form:textarea id="appointmentCancelReason" cols="30" rows="10" class="form-control" type="text"
                                   placeholder="${appointmentCancelReason}" path="cancelReason"/>
                </c:if>

                <c:if test="${isVacationInvalid}">
                    <div class="profileItem">
                        <label class="error">${addInvalidVacationMessage}</label>
                    </div>
                </c:if>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="closeAddVacationModal()"
                        class="btn btn-danger">${vacationCancel}</button>
                <button type="submit" class="btn btn-primary">${vacationAddButton}</button>
            </div>
            </form:form>
        </div>
    </div>
</div>
<div class="modal fade" id="addVacationSuccessModal" role="dialog" aria-labelledby="modalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">${vacationAddSuccessTitle}</h5>
            </div>
            <div class="modal-body">
                ${vacationAddSuccessDescription}
            </div>

            <div class="modal-footer">
                <button type="button" onclick="closeAddVacationSuccessModal()"
                        class="btn btn-primary">${modalButton}</button>
            </div>
        </div>
    </div>
</div>
</body>
<style>
    .hidden {
        display: none;
    }

    .vacationContainer {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .buttonsContainer {
        display: flex;
        flex-direction: row;
        justify-content: end;
        width: 50%;
        margin-bottom: 1rem;
    }

    .vacationCardContent{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }


    .vacationCardContent > .btn {
        margin-left: auto;
    }

    .vacationCardItem {
        display: flex;
        flex-direction: column;
        align-items: start;
        margin-right: 1rem;
    }

    .vacationContainer > .card {
        width: 50%;
        margin-bottom: 1rem;
        background-color: white;
    }

</style>
</html>

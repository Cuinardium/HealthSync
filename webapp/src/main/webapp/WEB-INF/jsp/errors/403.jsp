<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="error.403" var="error"/>

<html>
<head>
    <title>${error}</title>

    <!-- favicon -->
    <jsp:include page="../components/favicon.jsp"/>
</head>
<body>
    <h1>Error 403</h1>
    <h2>${error}</h2>
</body>
</html>

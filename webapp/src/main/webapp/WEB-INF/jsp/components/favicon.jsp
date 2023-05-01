<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!--Variables -->
<c:url value="/icons/favicon.png" var="faviconSvg"/>
<c:url value="/icons/favicon.png" var="faviconPng"/>

<html>
    <head>
        <link rel="icon" type="image/svg+xml" href="${faviconSvg}">
        <link rel="icon" type="image/png" href="${faviconPng}">
    </head>
</html>

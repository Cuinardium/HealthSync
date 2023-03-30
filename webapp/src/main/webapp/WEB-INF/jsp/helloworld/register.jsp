<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<body>
    <h2> Register </h2>
    <c:url value="/register" var="registerUrl" />
    <form action="${registerUrl}" method="post">
        <div>
            <label>Email
                <input type="text" name="email"/>
            </label>
        </div>
        <div>
            <label>Password
                <input type="password" name="password"/>
            </label>
        </div>

        <div>
            <input type="submit" value="Lets go!"/>
        </div>
            
    
    </form>

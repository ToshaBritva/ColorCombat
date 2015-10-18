<%-- 
    Document   : login
    Created on : 09.05.2015, 18:18:57
    Author     : boris
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<c:url value="/CSS/bootstrap.css"/>" rel="stylesheet" />
        <title>JSP Page</title>
    </head>
    <body>


        <div class="container" style="width: 300px;">
            <c:url value="/j_spring_security_check" var="loginUrl" />
            <form action="j_security_check" method="post">


                <h2 class="form-signin-heading">ERROR</h2>

                <input type="text" class="form-control" name="j_username" placeholder="Username" required autofocus value="">
                <input type="password" class="form-control" name="j_password" placeholder="Password" required value="">
                <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>
            </form>
        </div>


    </body>
</html>

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
        <link href="<c:url value="/CSS/MainPageCSS.css" />" rel="stylesheet" />
        <title>ColorCombat</title>
    </head>
    <body>
        
        <form class="form-horizontal col-md-4 col-md-offset-4" action="j_security_check" method="post">
            <fieldset>

            <legend class="top-buffer">Вход в ColorCombat</legend>

            <div class="form-group">
                  <input type="text" class="form-control input-md" name="j_username" placeholder="Логин" required autofocus value="">
            </div>

            <div class="form-group">
                <input type="password" class="form-control input-md" name="j_password" placeholder="Пароль" required value="">
                <%-- <span class="help-block text-right"><a href="<c:url value="#"/>">Забыли пароль?</a></span> --%> 
            </div>

            <div class="form-group">
                <button class="btn btn-default btn-primary btn-block btn-success" type="submit">Войти</button>
                <div class="top-buffer" style="text-align: center;">
                    <a href="<c:url value="/register"/>">Создать аккаунт</a>
                </div>
            </div>

            </fieldset>
          </form>

    </body>
</html>

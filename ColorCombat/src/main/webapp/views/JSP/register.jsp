<%-- 
    Document   : register
    Created on : 20.05.2015, 15:55:19
    Author     : boris
--%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
        <link href="<c:url value="/CSS/bootstrap.css"/>" rel="stylesheet" />
        <link href="<c:url value="/CSS/MainPageCSS.css" />" rel="stylesheet" />
        <title>Регистрация</title>
    </head>
    <body>

        <sf:form method="POST" commandName="user" class="form-horizontal col-md-4 col-md-offset-4">
            <fieldset>

            <legend class="top-buffer">Регистрация</legend>

            <div class="form-group">
              <sf:input class="form-control" path="nickname" size="15" id="nickname" placeholder="Введите никнейм"/>
              <sf:errors path="nickname"/>
            </div>

            <div class="form-group">
              <sf:input path="email" class="form-control" type="email" placeholder="Email" size="30" id="inputEmail"/>
              <sf:errors path="email" />
            </div>

              <div class="form-group">
                <sf:password path="password" size="30" showPassword="true" class="form-control" id="inputPassword" placeholder="Введите пароль"/>
                <sf:errors path="password" />
            </div>

            <div class="form-group row">
                <div class="col-md-6">
                  <input type="submit" class="btn btn-primary btn-block" name="commit"  value="Регистрация">
                </div>
                <div class="col-md-6">
                  <input type="reset" class="btn btn-default btn-block" value="Очистить форму">
                </div>
            </div>

            </fieldset>
        </sf:form>

    </body>
</html>

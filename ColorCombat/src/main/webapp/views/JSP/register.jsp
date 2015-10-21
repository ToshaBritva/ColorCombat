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
        <title>JSP Page</title>
    </head>
    <body>

        <h2>Регистрация</h2>
        <sf:form method="POST" commandName="user" class="form-horizontal">
            <fieldset>

                <div class="form-group">
                    <label class="control-label col-xs-3" for="nickname">Никнейм</label>

                    <div class="col-xs-9">
                        <sf:input class="form-control" path="nickname" size="15" id="nickname" placeholder="Введите никнейм"/>
                        <sf:errors path="nickname"  />
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-xs-3" for="inputEmail">Email:</label>
                    <div class="col-xs-9">
                        <sf:input path="email" class="form-control" type="email" placeholder="Email" size="30" id="inputEmail"/>
                        <sf:errors path="email" />
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-xs-3" for="inputPassword">Пароль:</label>
                    <div class="col-xs-9">
                        <sf:password path="password" size="30" showPassword="true" class="form-control" id="inputPassword" placeholder="Введите пароль"/>
                        <sf:errors path="password" />
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-xs-offset-3 col-xs-9">
                        <input type="submit"  name="commit" class="btn btn-primary" value="Регистрация">
                        <input type="reset" class="btn btn-default" value="Очистить форму">
                    </div>
                </div>
            </fieldset>
        </sf:form>
        <script>
        </script>
    </body>
</html>

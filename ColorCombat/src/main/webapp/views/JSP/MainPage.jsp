<%-- 
    Document   : MainPage
    Created on : 10.10.2015, 14:59:47
    Author     : Pushkin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
        <script src="<c:url value="/js/MainPageCreator.js" />"></script>
        <link href="<c:url value="/CSS/MainPageCSS.css" />" rel="stylesheet" />
        <%--<link href="<c:url value="/CSS/bootstrap-theme.css" />" rel="stylesheet" />--%>
        <link href="<c:url value="/CSS/bootstrap.css" />" rel="stylesheet" />
        <%--<link href="<c:url value="/CSS/bootstrap-responsive.css" />" rel="stylesheet" />
        <link href="<c:url value="/CSS/docs.css" />" rel="stylesheet" />
        <link href="<c:url value="/CSS/prettify.css" />" rel="stylesheet" />>--%>
    </head>
        <body>
            <div class="container">
                
                <div class="row header">
                    <div id="NickName" class="col-sm-6">
                        <h2>NAME</h2>
                    </div>
                    <div id="Profile" class="col-sm-2 col-sm-offset-2 top-buffer">
                        <button id="bntProfile" class="btn btn-primary btn-block">Профиль</button>
                    </div>
                    <div id="exit" class="col-sm-2 top-buffer">
                        <button id="idExit" class="btn btn-primary btn-block">Exit</button>
                    </div>
                </div>
                
                <div id="content" class="row top-buffer">
                    <div id="Menu" class="col-sm-6 ">
                        <div id="new_Btn1" class="row new_Btn">
                            <button id="CreateLobby" class="btn btn-primary btn-lg btn-block">Создать лобби</button>
                        </div>
                        <div id="new_Btn2" class="row new_Btn">
                            <button id="FindLobby" class="btn btn-primary btn-lg btn-block">Найти лобби</button>
                        </div>
                        <div id="new_Btn3" class="row new_Btn">
                            <button id="FindGame" class="btn btn-primary btn-lg btn-block">Найти игру</button>
                        </div>
                        <div id="new_Btn4" class="row new_Btn ">
                            <button id="Scoreboard" class="btn btn-primary btn-lg btn-block">Таблица лидеров</button>
                        </div>
                    </div>
                    
                    <div class="col-sm-6">
                        <li class="list-group-item list-group-item-info text-left">
                            <h4 class="list-group-item-heading">Контакты</h4>
                        </li>
                        <div id="Contacts" class="list-group text-left">
                            <a href="#" class="list-group-item text-left">Вася</a>
                            <a href="#" class="list-group-item text-left">Вася </a>
                            <a href="#" class="list-group-item text-left">Вася </a>
                            <a href="#" class="list-group-item text-left">Вася </a>
                        </div>
                    </div>
                </div>
            </div>
        </body>
</html>

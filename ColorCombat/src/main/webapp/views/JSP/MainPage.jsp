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
        <script src="<c:url value="/js/MainPageCreator.js" />"></script>
        <link href="<c:url value="/CSS/MainPageCSS.css" />" rel="stylesheet" />
        <%--<link href="<c:url value="/CSS/bootstrap-theme.css" />" rel="stylesheet" />--%>
        <link href="<c:url value="/CSS/bootstrap.css" />" rel="stylesheet" />
        <link href="<c:url value="/CSS/bootstrap-responsive.css" />" rel="stylesheet" />
        <link href="<c:url value="/CSS/docs.css" />" rel="stylesheet" />
        <link href="<c:url value="/CSS/prettify.css" />" rel="stylesheet" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
    </head>
        <body class="container-fluid">
            <div class="row header show-grid">
                <div id="NickName" class="span6">NAME</div>
                <div id="Profile" class="span2 offset2">Profile</div>
                <div id="exit" class="span2 ">
                    <button id="idExit" class="btn btn-primary btn-lg btn-block">Exit</button>
                </div>
            </div>
            <div id="content" class="row show-grid">
                <div id="Menu" class="span6 ">
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
                <div id="Contact" class="span5 offset1">
                    Contacts
                    Contacts
                </div>
            </div>
        </body>
</html>

<%-- 
    Document   : FindLobbiesPage
    Created on : 11.10.2015, 21:39:05
    Author     : Niyaz
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Найти лобби</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
        <link href="<c:url value="/CSS/MainPageCSS.css" />" rel="stylesheet" />
        <script src="<c:url value="/js/LobbyScripts.js"/>"></script>
        <link href="<c:url value="/CSS/bootstrap.css"/>" rel="stylesheet" />
    </head>
    <body>
        <div class="container">
            <div id='Content' class="row top-buffer">
                <div class="col-md-6">
                    <table id='LobbiesTable' class='table table-hover table-striped'>
                        <thead>
                            <tr class="info">
                              <th style="width: 60%">Владелец лобби</th>
                              <th style="width: 30%">Количество игроков</th>
                              <th style="width: 10%"></th>
                            </tr>
                        </thead>
                        <tbody id="LobbiesBody">
                            <tr class='lobbyRow'>
                                <th id='Nick'>Вася</th>
                                <th id='Slots'>3/4</th>
                                <th> <span class="glyphicon glyphicon-bed"></span> </th>
                            </tr>
                            <tr class='lobbyRow'>
                              <th id='Nick'>Петя</th>
                              <th id='Slots'>1/4</th>
                              <th><span class="glyphicon glyphicon-bed"></span></th>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class='Row'>
                <div class='col-md-3'>
                    <button id="CreateLobby" class="btn btn-primary btn-lg btn-block" onClick='location.href="<c:url value="/MyLobby"/>"'>Создать лобби</button>
                </div>
                <div class='col-md-3'>
                    <button id="Update" class="btn btn-primary btn-lg btn-block" onclick="removePlayerFromTable('Петя')">Обновить</button>
                </div>
            </div>
        </div>
    </body>
</html>

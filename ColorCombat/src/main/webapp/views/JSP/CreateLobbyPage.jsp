<%-- 
    Document   : CreateLobbyPage
    Created on : 11.10.2015, 16:28:06
    Author     : Niyaz
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Моё лобби</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
        <link href="<c:url value="/CSS/MainPageCSS.css" />" rel="stylesheet" />
        <link href="<c:url value="/CSS/bootstrap.css"/>" rel="stylesheet" />
        <script src="<c:url value="/js/LobbyScripts.js"/>"></script>

    </head>
        <body>
            <div class="container">
                <div class="row header">
                    <div id="NickName" class="col-sm-6">
                        <h2>NAME</h2>
                    </div>
                </div>
                
                <div id='Content' class="row">
                    <div class="col-md-6">
                        <table id='LobbyTable' class='table'>
                            <thead>
                                <tr class="info ">
                                  <th style="width: 70%">Ник</th>
                                  <th style="width: 30%">Готовность</th>
                                </tr>
                            </thead>
                            <tr id='Slot0'>
                                <th id='Nick'>Свободный слот <span class="glyphico"></span></th>
                                <th id='Status'></th>
                            </tr>
                            <tr id='Slot1'>
                              <th id='Nick'>Свободный слот</th>
                              <th id='Status'></th>
                            </tr>
                            <tr id='Slot2'>
                              <th id='Nick'>Свободный слот</th>
                              <th id='Status'></th>
                            </tr>
                        </table>
                    </div>
                </div>
                
                <div class='Row'>
                    <div class='col-md-3 col-md-offset-3'>
                        <button id="StartGame" class="btn btn-primary btn-lg btn-block" onclick="removePlayerFromTable('Петя')">Начать игру</button>
                    </div>
                </div>
            </div>
        </body>
</html>

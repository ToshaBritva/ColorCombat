<%-- 
    Document   : game
    Created on : 08.10.2015, 21:58:53
    Author     : boris
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ColorCombat</title>
        <script src="https://cdn.rawgit.com/konvajs/konva/0.9.5/konva.min.js"></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
        <script src="<c:url value="/js/socketJS.js" />"></script>
        <script src="../js/game.js" type="text/javascript"></script>

        <link href="../CSS/stylesTimr.css" rel="stylesheet" type="text/css"/>
        <link href="../CSS/materialize.min.css" rel="stylesheet" type="text/css"/>
        <link href="../CSS/bootstrap.css" rel="stylesheet" type="text/css"/>
        <link href="../CSS/game.css" rel="stylesheet" type="text/css"/>
    </head>

    <body onload="onloadPage()">
        <div class="row" id="Status">
            <div class="col-xs-3">
                <h3>В ожидании</h3>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-8 ">
                <div id="gameField" class="inactive z-depth-2" ></div>
            </div>
            <div class="col-xs-4 ">
                <div class="table-responsive inactive z-depth-1">
                    <table class="table table-condensed" id='ScoreTable'></table>
                </div>
                <div class="timerDiv clock inactive z-depth-1 ">
                    00:00:00 
                </div>
            </div>
        </div>


        <div id="MsgDng" class="alert alert-warning ">
            <a class="close" data-dismiss="alert" href="#">×</a>
            <p>warning</p>
        </div> 

        <div id="Msg" class="alert alert-success ">
            <a class="close" data-dismiss="alert" href="#">×</a>
            <p>success</p>
        </div>

        <script src="<c:url value="/js/canvasLogic.js" />"></script>
       

    </body>
</html>


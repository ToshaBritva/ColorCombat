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
    </head>
    <body>
        <div id="gameField"></div>
        <div id="currentTable"></div>
        <script src="<c:url value="/js/canvasLogic.js" />"></script>
        <select onchange="chenge()" id = "Role">
            <option>1</option>
            <option>2</option>
            <option>3</option>
            <option>4</option>
        </select>
    </body>
</html>


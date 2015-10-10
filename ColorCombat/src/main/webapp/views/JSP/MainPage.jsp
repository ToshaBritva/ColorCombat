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
        <%-- <style src="<c:url value="/CSS/MainPageCSS.css" />"></style>--%>
        <link href="<c:url value="/CSS/MainPageCSS.css" />" rel="stylesheet" />
        <link href="<c:url value="/CSS/bootstrap-theme.css" />" rel="stylesheet" />
        <link href="<c:url value="/CSS/bootstrap.css" />" rel="stylesheet" />
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
    </head>
    <body onload="CreateMainPage()" class="container-fluid">
        
    </body>
</html>

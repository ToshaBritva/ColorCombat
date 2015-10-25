<%-- 
    Document   : ProfilePage
    Created on : 12.10.2015, 18:05:33
    Author     : Niyaz
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<c:url value="/CSS/MainPageCSS.css" />" rel="stylesheet" />
        <link href="<c:url value="/CSS/bootstrap.css"/>" rel="stylesheet" />
        <title>Профиль</title>
    </head>
    <body>
        <div class='container'>
            <div class='header row'>
                <div id="NickName" class="col-sm-2">
                    <h2><%=request.getUserPrincipal().getName().toString()%></h2>
                </div>
                <div id="Rating" class="col-sm-3">
                    <h2><Рейтинг></h2>
                </div>
            </div>
            
            <div id="content">
                <div class='row'>
                    <div class='col-sm-6'>
                          <div class="form-group text-left">
                            <label for="about">О себе:</label>
                            <textarea class="form-control" rows="6" id="about" style='resize: none; margin-left: 10px' placeholder="Расскажите о себе..."></textarea>
                            <div class='text-center'>
                                <button id="CreateLobby" class="btn btn-primary btn-lg top-buffer" style='margin-left: 10px' onClick='location.href="<c:url value="/MyLobby"/>"'>
                                    Сохранить
                                </button>
                            </div>
                          </div>
                    </div>
                </div>
                <div class='row top-buffer'>
                    <table id='GameHistory' class='table table-hover'>
                        <thead>
                            <tr class="info">
                              <th style="width: 30%">Дата</th>
                              <th style="width: 40%">Набранные очки</th>
                              <th style="width: 30%">Итог</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th>20.12.2005</th>
                                <th>1180</th>
                                <th>II место</th>
                            </tr>
                            <tr class='success'>
                                <th>20.12.2005</th>
                                <th>1280</th>
                                <th>I место</th>
                            </tr>
                            <tr class='success'>
                                <th>21.12.2005</th>
                                <th>1480</th>
                                <th>I место</th>
                            </tr>
                            <tr>
                                <th>23.12.2005</th>
                                <th>960</th>
                                <th>III место</th>
                            </tr>
                            <tr>
                                <th>25.12.2005</th>
                                <th>960</th>
                                <th>IV место</th>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
                                    
                                    
        </div>
    </body>
</html>

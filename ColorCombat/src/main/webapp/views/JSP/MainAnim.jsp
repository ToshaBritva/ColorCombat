<%-- 
    Document   : MainAnim
    Created on : 12.10.2015, 0:14:08
    Author     : Pushkin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ColorCombat</title>
        <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
        <link href="<c:url value="/CSS/MainPageCSS.css" />" rel="stylesheet" />
        <link href="<c:url value="/CSS/bootstrap.css"/>" rel="stylesheet" />
        <script src="<c:url value="/js/bootstrap.min.js" />"></script>
    </head>
    <body>
        <body>
            <div class="container">
                
                <div class="row header">
                    <div id="NickName" class="col-sm-6">
                        <h2>NAME </h2>
                    </div>
                    <div id="Profile" class="col-sm-2 col-sm-offset-2 top-buffer">
                        <button id="bntProfile" class="btn btn-primary btn-block">Профиль</button>
                    </div>
                    <div id="exit" class="col-sm-2 top-buffer">
                        <button id="idExit" class="btn btn-primary btn-block">Exit</button>
                    </div>
                </div>
                
                <div id="content" class="top-buffer">
                    <div id="Menu" class="col-sm-6">
                        <div id="new_Btn1" class="row new_Btn">
                            <button type="button" class="btn btn-primary btn-lg btn-block" data-toggle="modal" data-target="#CrLPModal">
                                Создать лобби
                            </button>
                        </div>
                        <div id="new_Btn2" class="row new_Btn">
                            <button id="FindLobby" class="btn btn-primary btn-lg btn-block" data-toggle="modal" data-target="#FindLobbyModal">Найти лобби</button>
                        </div>
                        <div id="new_Btn3" class="row new_Btn">
                            <button id="FindGame" class="btn btn-primary btn-lg btn-block" data-toggle="modal" data-target="#GoFindModal">Найти игру</button>
                        </div>
                        <div id="new_Btn4" class="row new_Btn ">
                            <button id="Scoreboard" class="btn btn-primary btn-lg btn-block" data-toggle="modal" data-target="#TOfLiders">Таблица лидеров</button>
                        </div>
                    </div>
                    
                    <div class="col-sm-6">
                        
            
                        <div class="list-group text-left">
                                <li class="list-group-item list-group-item-info">
                                    <h4 class="list-group-item-heading">Контакты</h4>
                                </li>
                                <div id="Contacts">
                                    <a href="#" class="list-group-item">Вася</a>
                                    <a href="#" class="list-group-item">Петя </a>
                                </div>
                        </div>
                        
                        <div class="col-sm-6 top-buffer">
                                <input id="inpContactName" class="form-control" placeholder="Ник пользователя">
                            </div>
                        <div class="col-sm-6 top-buffer">
                            <button id="AddContact" class="btn btn-primary btn-block">Добавить</button>
                        </div>
                        
                    </div>
                </div>
                </div>
            
            
        <!-- создать лобби -->
            <div class="modal fade" id="CrLPModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel1">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel1">Лобби NAME</h4>
                  </div>
                  <div class="modal-body">               
                                        <div id='Content' >
                                                <table id='LobbyTable' class='table'>
                                                    <thead>
                                                        <tr class="info ">
                                                          <th style="width: 70%">Ник</th>
                                                          <th style="width: 30%">Готовность</th>
                                                        </tr>
                                                    </thead>
                                                    <tr id='Slot0'>
                                                        <th id='Nick'>Свободный слот <button type="button" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></button></th> 
                                                        <th id='Status'> </th>
                                                    </tr>
                                                    <tr id='Slot1'>
                                                      <th id='Nick'>Свободный слот<button type="button" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></button></th>
                                                      <th id='Status'></th>
                                                    </tr>
                                                    <tr id='Slot2'>
                                                      <th id='Nick'>Свободный слот<button type="button" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></button></th>
                                                      <th id='Status'></th>
                                                    </tr>
                                                </table>
                                        </div>
                    </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
                    <button type="button" class="btn btn-primary">Начать</button>
                </div>
              </div>
            </div>
        </div>
        <!-- Таблица лидеров -->
            
            <div class="modal fade" id="TOfLiders" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                      <h4 class="modal-title" id="myModalLabel">Таблица лидеров</h4>
                    </div>
                    <div class="modal-body">
                                        <div id='ContentTBL' >
                                            <table id='ReitingTable' class='table'>
                                                    <thead>
                                                        <tr class="info ">
                                                          <th style="width: 10%">№</th>
                                                          <th style="width: 60%">Ник</th>
                                                          <th style="width: 30%">Очки</th>
                                                        </tr>
                                                    </thead>
                                                    <tr id='Slot0'>
                                                        <th>1</th>
                                                        <th id='Nick'>Свободный слот </th> 
                                                        <th id='Status'> </th>
                                                    </tr>
                                                    <tr id='Slot1'>
                                                      <th>2</th>
                                                      <th id='Nick'>Свободный слот</th>
                                                      <th id='Status'></th>
                                                    </tr>
                                                    <tr id='Slot2'>
                                                      <th>3</th>
                                                      <th id='Nick'>Свободный слот</th>
                                                      <th id='Status'></th>
                                                    </tr>
                                                </table>
                                        </div>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
                   
                    </div>
                  </div>
                </div>
              </div>
                
                
         <!-- Поиск лобби -->       
        <div class="modal fade" id="FindLobbyModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel1">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel1">Выберите лобби</h4>
                  </div>
                  <div class="modal-body">          
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

                                       
                    <div class="modal-footer">
                      <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
                      <button type="button" class="btn btn-primary  " data-dismiss="modal" data-toggle="modal" data-target="#CrLPModal">Создать лобби</button>
                      <button type="button" class="btn btn-success " onclick="removePlayerFromTable('Петя')">Обновить</button>
                    </div>
                  </div>
                </div>
              </div>    
         <!-- Быстрый Поиск лобби -->  
         <div class="modal fade" id="GoFindModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel1">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel1">Производим поиск соперников<i class="fa fa-spinner fa-spin"></i></h4>
                  </div>
                  

                                       
                <div class="modal-footer">
                  <button type="button" class="btn btn-default" data-dismiss="modal">Отменить поиск</button>
                  
                </div>
            </div>
          </div>
        </div>     
                      
        </body>

        
</html>

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
        <link href="https://gitcdn.github.io/bootstrap-toggle/2.2.0/css/bootstrap-toggle.min.css" rel="stylesheet">
        <script src="https://gitcdn.github.io/bootstrap-toggle/2.2.0/js/bootstrap-toggle.min.js"></script>
        <script src="<c:url value="/js/MainAnim.js" />"></script>
        
        <script src="<c:url value="/js/socialSocket.js" />"></script>
        

    </head>
    <body>

        <div class="container">

            <div class="row header">
                <div id="NickName" class="col-sm-6">
                    <h2><%=request.getUserPrincipal().getName().toString()%></h2>
                </div>
                <div id="Profile" class="col-sm-2 col-sm-offset-2 top-buffer">
                    <button id="bntProfile" class="btn btn-primary btn-block" data-toggle="modal" data-target="#ProfileModal">Профиль</button>
                </div>
                <div id="exit" class="col-sm-2 top-buffer">
                    <button id="idExit" class="btn btn-primary btn-block" onClick='location.href = "<c:url value="/logout"/>"'>Exit</button>
                </div>
            </div>

            <div id="content" class="top-buffer">
                <div id="Menu" class="col-sm-6">
                    <div id="new_Btn1" class="row new_Btn">
                        <button type="button" class="btn btn-primary btn-lg btn-block" onclick="createLobby()" data-toggle="modal" data-target="#CrLPModal">
                            Создать лобби
                        </button>
                    </div>
                    <div id="new_Btn2" class="row new_Btn">
                        <button id="FindLobby" class="btn btn-primary btn-lg btn-block" data-toggle="modal" onclick="showLobbyList()" data-target="#FindLobbyModal">
                            Найти лобби
                        </button>
                    </div>
                    <div id="new_Btn3" class="row new_Btn">
                        <button id="FindGame" class="btn btn-primary btn-lg btn-block" data-toggle="modal" data-target="#GoFindModal">
                            Найти игру
                        </button>
                    </div>
                    <div id="new_Btn4" class="row new_Btn ">
                        <button id="Scoreboard" class="btn btn-primary btn-lg btn-block" data-toggle="modal" data-target="#TOfLiders">
                            Таблица лидеров
                        </button>
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

        <!-- Создать лобби -->
        <div class="modal fade" id="CrLPModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel1">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">Лобби <%=request.getUserPrincipal().getName().toString()%></h4>
                    </div>
                    <div class="modal-body">               
                        <div id='Content' >
                            <table id='LobbyTable' class='table'>
                                <thead>
                                    <tr class="info ">
                                        <th style="width: 70%">Ник</th>
                                        <th style="width: 5%"></th>
                                        <th style="width: 25%; text-align: center"   >Готовность</th>
                                    </tr>
                                </thead>
                                <tr id='Slot0'>
                                    <th id='Nick' class="Nick">Свободный слот<button type="button" class="close" aria-label="Close"></button></th> 
                                    <th class='Kick'></th>
                                    <th class='Status'> </th>
                                </tr>
                                <tr id='Slot1'>
                                    <th id='Nick' class="Nick">Свободный слот<button type="button" class="close" aria-label="Close"></button></th>
                                    <th class='Kick'></th>
                                    <th class='Status'></th>
                                </tr>
                                <tr id='Slot2'>
                                    <th id='Nick' class="Nick">Свободный слот<button type="button" class="close" aria-label="Close"></button></th>
                                    <th class='Kick'></th>
                                    <th class='Status'></th>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Расформировать лобби</button>
                        <button type="button" class="btn btn-primary" id="startGame" onclick="startGame()">Начать</button>
                    </div>
                </div>
            </div>
        </div>
                    
        <!-- показать лобби -->
        <div class="modal fade" id="JoinLobby" tabindex="-1" role="dialog" aria-labelledby="myModalLabel1">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">Лобби <%=request.getUserPrincipal().getName().toString()%></h4>
                    </div>
                    <div class="modal-body">               
                        <div id='Content' >
                            <table id='LobbyTable' class='table'>
                                <thead>
                                    <tr class="info ">
                                        <th style="width: 70%">Ник</th>
                                        <th style="width: 30%; text-align: center"   >Готовность</th>
                                    </tr>
                                </thead>
                                <tr id='Slot0'>
                                    <th id='Nick' class="Nick">Свободный слот<button type="button" class="close" aria-label="Close"></button></th> 
                                    <th class='Status'> </th>
                                </tr>
                                <tr id='Slot1'>
                                    <th id='Nick' class="Nick">Свободный слот<button type="button" class="close" aria-label="Close"></button></th>
                                    <th class='Status'></th>
                                </tr>
                                <tr id='Slot2'>
                                    <th id='Nick' class="Nick">Свободный слот<button type="button" class="close" aria-label="Close"></button></th>
                                    <th class='Status'></th>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Покинуть лобби</button>
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
                                    <th id='Nick'>вася </th> 
                                    <th id='Col'>12312331 </th>
                                </tr>
                                <tr id='Slot1'>
                                    <th>2</th>
                                    <th id='Nick'>Петя</th>
                                    <th id='Col'>2132134</th>
                                </tr>
                                <tr id='Slot2'>
                                    <th>3</th>
                                    <th id='Nick'>Repz</th>
                                    <th id='Col'>123</th>
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
                                    <th id='Host'>Петя</th>
                                    <th id='Slots'>1/4</th>
                                    <th><span class="glyphicon glyphicon-plus"></span></th>
                                </tr>
                            </tbody>
                        </table>                    
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
                        <button type="button" class="btn btn-primary  " onclick="createLobby()" data-dismiss="modal" data-toggle="modal" data-target="#CrLPModal">Создать лобби</button>
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
                        <h4 class="modal-title" id="myModalLabel1">Производим поиск соперников <i class="fa fa-spinner fa-spin"></i></h4>
                    </div>                       
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Отменить поиск</button>
                    </div>
                </div>
            </div>
        </div>    

        <!-- Профиль -->       
        <div class="modal fade" id="ProfileModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel1">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="Nickname"><%=request.getUserPrincipal().getName().toString()%></h4>
                        <small id="Rating"><Рейтинг></small>
                    </div>
                    <div class="modal-body">          
                        <div id="content">
                            <div class="form-group text-left">
                                <label for="about">О себе:</label>
                                <textarea class="form-control" rows="3" id="about" style='resize: none;' placeholder="Расскажите о себе..."></textarea>
                                <div class='text-center'>
                                    <button id="SaveProfile" class="btn btn-primary btn-default top-buffer" style='margin-left: 10px' data-dismiss="modal">
                                        Сохранить
                                    </button>
                                </div>
                            </div>
                            <div class='top-buffer'>
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
    </body> 

    <script type="text/javascript"> $(document).ready(onloadPage());</script>


</html>

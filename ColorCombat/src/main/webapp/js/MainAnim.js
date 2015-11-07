/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function onloadPage (){
    $('#MsgDng').fadeOut(0);
    $.fn.bootstrapSwitch.defaults.size = 'large';
    $.fn.bootstrapSwitch.defaults.onColor = 'success';
    $.fn.bootstrapSwitch.defaults.onText = 'готов';
    $.fn.bootstrapSwitch.defaults.offText = 'не готов';
    $.fn.bootstrapSwitch.defaults.offColor = 'danger';
    $(".switchBTM").bootstrapSwitch();
    addPlayerToTable('asdsad');
}

//Скрипты MainPage
function addContact(contactName) 
{
    var Contact = '<a href="#" class="list-group-item text-left">' + contactName + '</a>'; //Заменить # на валидную ссылку
    $("#Contacts").append(Contact);
}

function ShowMSGDng(MSG)
{
    $('#MsgDng p').text(MSG);
    $('#MsgDng').fadeIn();
    setTimeout(function()
    {
        $('#MsgDng').fadeOut(); ;
    }, 5000);
}
function ShowMSG(MSG)
{
    $('#Msg p').text(MSG);
    $('#Msg').fadeIn();
    setTimeout(function()
    {
        $('#Msg').fadeOut(); ;
    }, 1000);
}

//Скрипты лобби
function getSlotNum(nickname)
{
    for (var i=0; i<3; i++)
    {
        if($("#CrLPModal #Slot" + i + " #Nick" ).text()===nickname)
        {
            return i;
        }
    }
    return -1;
}
    

function changeStatus(nickname) ///Сменить статус игрока 
{
    var slotNum = getSlotNum(nickname);
    
    var curStatus = $("#CrLPModal #Slot" + slotNum + " .bootstrap-switch-on").text()=="";
    if (curStatus)
    {
        $("#CrLPModal #Slot" + slotNum + ' .bootstrap-switch-handle-off').click();
    }
    else
    {
        $("#CrLPModal #Slot" + slotNum + ' .bootstrap-switch-handle-on').click();
    }
}

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function createLobby()
{
    socSocket.send('{"target":"createLobby"}');
    
    $( "#leaveLobby" ).click(function() 
    {
        destroyLobby();
    });
}

function destroyLobby()
{
    socSocket.send('{"target":"destroyLobby"}');
}

function joinLobby(userHost)
{
    
    socSocket.send('{"target":"joinLobby", "userHost":"' + userHost + '"}');
    //Нужно дописать, проверить получилось ли подключение
    
    $.getJSON('MainPage/getLobby?host=' + userHost, {}, function(json)
    { 
        $('#CrLPModal .modal-title').text(userHost);
        
        for (var i = 0, len = json.members; i < len; i++)
        {
            addPlayerToTable(json.members[i].nickname);
            
            //Тут должна быть установка статуса игрока. Пушкин, соберись!
        }
        
        $('.modal').modal('hide');
        
        $( "#leaveLobby" ).click(function() {
            leaveLobby();
        });
        
        $('#CrLPModal').modal('show');
    }); 
}

function leaveLobby()
{
    $('.modal').modal('hide');
    socSocket.send('{"target":"leaveLobby"}');
}

//Добавить игрока в окне лобби
function addPlayerToTable(nickname) 
{
    var slotNum = getSlotNum('Свободный слот');
    $("#CrLPModal #Slot" + slotNum + " #Nick").text(nickname);
    $("#CrLPModal #Slot" + slotNum + " #Status").append('<input class="switchBTM" type="checkbox"  data-size="mini">');

    $(".switchBTM").bootstrapSwitch();
}

//Очистить слот в окне лобби
function setSlotEmpty(slotNum)
{
    $("#CrLPModal #Slot" + slotNum + " #Nick").text('Свободный слот');
    $("#CrLPModal #Slot" + slotNum + " #Status").text('');
    $("#CrLPModal #Slot" + slotNum).removeClass();
}

function clearLobby()
{
    $("#CrLPModal #Nick").text('Свободный слот');
    $("#CrLPModal #Status").text('');
    //$("#CrLPModal").removeClass();
}

//удалить игрока в окне лобби
function removePlayerFromTable(nickname)
{
    var slotNum = getSlotNum(nickname);
    setSlotEmpty(slotNum);
}

//показать список лобби
function showLobbyList()
{
    $("#LobbiesBody").empty();

    $.getJSON('MainPage/getLobbyList', {}, function(json)
    { 
        for (var i = 0, len = json.length; i < len; i++) 
        {
            var row = $('<tr/>', {class:  'lobbyRow' });
            row.append('<th id="Host">' + json[i].lobbyName + '</th>');
            row.append('<th id="Slots">' + json[i].busySlotNum + '/4' + '</th>');
            var joinButton = $('<a href=#></a>');
            joinButton = joinButton.append($('<span/>', {class: 'glyphicon glyphicon-plus', onclick: 'joinLobby("' + json[i].lobbyName + '")'}));
            joinButton = $('<th/>').append(joinButton);
            row.append(joinButton);
            $("#LobbiesBody").append(row);
        }
    }); 
}

function changeStatus(nickname) ///Сменить статус игрока 
{
    var slotNum = getSlotNum(nickname);

    var curStatus = $("#CrLPModal #Slot" + slotNum + " .bootstrap-switch-on").text()=="";
    if (curStatus)
    {
        $("#CrLPModal #Slot" + slotNum + ' .bootstrap-switch-handle-off').click();
    }
    else
    {
        $("#CrLPModal #Slot" + slotNum + ' .bootstrap-switch-handle-on').click();
    }
}
function setStatusOn(nickname)
{
    var slotNum = getSlotNum(nickname);
    $("#CrLPModal #Slot" + slotNum + ' .bootstrap-switch-handle-off').click();
}
function setStatusOff(nickname)
{
    var slotNum = getSlotNum(nickname);
    $("#CrLPModal #Slot" + slotNum + ' .bootstrap-switch-handle-on').click();
}


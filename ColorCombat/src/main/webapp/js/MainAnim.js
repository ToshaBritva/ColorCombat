/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var lobbyNow=new lobby();
function slot(num)
{
    this.empty=true;
    this.name;
    this.slot=num;
    this.locked;
    this.setStatus=function(StatBool)
    {
        //установка в след значение автомат лок
    };
    this.clear=function()
    {
        $("#Slot" + this.slot + " .Nick").text('Свободный слот');
        $("#Slot" + this.slot + " .Status").text('');
        this.emty=true;
        //установка в след значение автомат лок
    };

    
}
function lobby()
{
    this.slots = [new slot(0),new slot(1), new slot(2)];
    this.host;
    this.nameModal;
    this.init=function(nameModal,host)
    {
        //установить имя лобби
        $('#'+nameModal+' #myModalLabel1').text(host);
        this.host=host;
        this.nameModal=nameModal;
        for (i=0;i<3;i++)
            this.slots[i].clear();
        
        
       
    };
    this.add=function(name)
    {
        temp=this.slots[0];
        i=0;
        while (!temp.empty)
        {
            i++;
            temp=this.slots[i];
        }
        temp.name=name;
        temp.empty=false;
        if (name !== $(".header #NickName h2").text())
            temp.locked='disable';
        else
            temp.locked='enable';
        $('#'+this.nameModal+" #Slot" + temp.slot + " .Nick").text(temp.name);
        $('#'+this.nameModal+" #Slot" + temp.slot + " .Status").append('<input name="'+name+'" type="checkbox" data-toggle="toggle" data-on="готов" data-off="не готов" data-onstyle="success" data-offstyle="danger" data-size="mini">');
        $('#'+this.nameModal+" #Slot" + temp.slot + " .Status input").bootstrapToggle();
        $('#'+this.nameModal+" #Slot" + temp.slot + " .Status input").bootstrapToggle(temp.locked);
        if (temp.locked==='enable')
        $('#'+this.nameModal+" #Slot" + temp.slot + " .Status input").change(function() {
            if($(this).prop('checked'))
                if($(this).prop('checked'))
                    sendStatus('ready');
                else
                    sendStatus('notReady');

        });
    };
    this.delete=function(name)
    {
        temp=this.slots[0];
        i=0;
        while (temp.name!==name && i<3)
        {
            i++;
            temp=this.slots[i];
        }
        temp.clear();
        //в сокет сообщение
    };
    this.setStatus=function(name,status)
    {
        temp=this.slots[0];
        i=0;
        while (temp.name!==name && i<3)
        {
            i++;
            temp=this.slots[i];
        }
        $('#'+this.nameModal+" #Slot" + temp.slot + " .Status input").bootstrapToggle('enable');
        if (status === "ready")
            $('#'+this.nameModal+" #Slot" + temp.slot + " .Status input").bootstrapToggle('on');
        else
            $('#'+this.nameModal+" #Slot" + temp.slot + " .Status input").bootstrapToggle('off');
        
        $('#'+this.nameModal+" #Slot" + temp.slot + " .Status input").bootstrapToggle(temp.locked);
    };
};
function onloadPage (){
    $('#MsgDng').fadeOut(0);
    $('#CrLPModal').on('hidden.bs.modal', function (e) {
        // do something...
        destroyLobby();
        ShowMSG("Закрыто создание лобби")
    });
    $('#CrLPModal').on('show.bs.modal', function (e) {
        // do something...
        lobbyNow.init("CrLPModal", $(".header #NickName h2").text());
        ShowMSG("Открыто создание лобби");
    });
    $('#JoinLobby').on('hidden.bs.modal', function (e) {
        // do something...
        leaveLobby();
        ShowMSG("Закрыто показать лобби")
    });
    $('#JoinLobby').on('show.bs.modal', function (e) {
        // do something...
        ShowMSG("Открыто показать лобби");
    });
    $('#TOfLiders').on('hidden.bs.modal', function (e) {
        // do something...
        ShowMSG("Закрыто Таблица лидеров ")
    });
    $('#TOfLiders').on('show.bs.modal', function (e) {
        // do something...
        ShowMSG("Открыто Таблица лидеров ")
    });
    $('#FindLobbyModal').on('hidden.bs.modal', function (e) {
        // do something...
        ShowMSG("Закрыто Поиск лобби  ")
    });
    $('#FindLobbyModal').on('show.bs.modal', function (e) {
        // do something...
        ShowMSG("Открыто Поиск лобби  ")
    });
    $('#GoFindModal').on('hidden.bs.modal', function (e) {
        // do something...
        ShowMSG("Закрыто Быстрый Поиск лобби  ")
    });
    $('#GoFindModal').on('show.bs.modal', function (e) {
        // do something...
        ShowMSG("Открыто Быстрый Поиск лобби  ")
    });
    $('#ProfileModal').on('hidden.bs.modal', function (e) {
        // do something...
        ShowMSG("Закрыто Профиль  ")
    });
    $('#ProfileModal').on('show.bs.modal', function (e) {
        // do something...
        ShowMSG("Открыто Профиль")
    });
   
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

function createLobby()
{
    socSocket.send('{"target":"createLobby"}');
}

function destroyLobby()
{
    socSocket.send('{"target":"destroyLobby"}');
}

function joinLobby(userHost)
{
    lobbyNow.init('JoinLobby', userHost);
    
    $.getJSON('MainPage/getLobby?host=' + userHost, {}, function(json)
    { 
        
        
        for (var i = 0, len = json.members.length; i < len; i++)
        {
            lobbyNow.add(json.members[i].nickname);
            if(json.members[i].status === "ready")
            {
                
            }
            else
            {
                
            }
        }
        
        $('.modal').modal('hide');
        
        $( "#leaveLobby" ).click(function() {
            leaveLobby();
        });
        
        $('#JoinLobby').modal('show');
        
        socSocket.send('{"target":"joinLobby", "userHost":"' + userHost + '"}');
        var nickname = $(".header #NickName h2").text();
        lobbyNow.add(nickname);
    }); 
    
}

function leaveLobby()
{
    socSocket.send('{"target":"leaveLobby"}');
}

function sendStatus(status)
{
    socSocket.send('{"target":"setStatus", "status":"' + status + '"}');
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

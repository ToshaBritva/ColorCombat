/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var websocket;

//При загрузке страницы коннектимся к сокету
function onloadPage() {
    
    var wsUri = "ws://" + document.location.host + document.location.pathname + "/server";
    websocket = new WebSocket(wsUri);
    
    websocket.onerror = function (evt) {
        onError(evt);
    };

    websocket.onopen = function (evt) {
        onOpen(evt);
    };

    websocket.onmessage = function (evt) {
        onMessage(evt);
    };
    
    $(this).keyup(function(event){
        if(event.keyCode == 13){
            $("#onMainPage").click();
        }
    });
}

//Действия по возникновению ошибки в соеденении с сокетом
function onError(evt) {
    ShowMSG("Ошибка подключения. Попробуйте перезагрузить страницу.");
}

//Действия по открытию соеденения с сокетом
function onOpen(evt) {
    console.log("подключено");
}

//Действия при получении сообщения
function onMessage(evt) {
    json = JSON.parse(evt.data);

    //Определяем цель сообщения
    switch (json.target) {
        case "removeBonus":
            removeBonus(json.value);
            break;
        case "spawnBonus":
        case "movePlayer":
            drawChanges(json.value);
            break
        case "time":
            setTime(json.value);
            break
        case "countdown":
            countdown(json.value);
            break;
        case "gameStatus":
            changeStatus(json.value);
            break
        case "winner":
            gameOver(json.value);
            break;
    }

}

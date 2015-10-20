/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var wsUri = "ws://" + document.location.host + document.location.pathname + "/server";
var websocket = new WebSocket(wsUri);


websocket.onerror = function (evt) {
    onError(evt);
};

websocket.onopen = function (evt) {
    onOpen(evt);
};

websocket.onmessage = function (evt) {
    onMessage(evt);
};

//Действия по возникновению ошибки в соеденении с сокетом
function onError(evt) {
    alert("ошибка подключения");
}

//Действия по открытию соеденения с сокетом
function onOpen(evt) {
    alert("подключено");
}

//Действия при получении сообщения
function onMessage(evt) {
    console.log(evt.data);
    drawChanges(evt.data);
}


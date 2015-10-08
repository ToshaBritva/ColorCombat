/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var wsUri = "ws://" + document.location.host + document.location.pathname + "server";
var websocket = new WebSocket(wsUri);

websocket.onerror = function (evt) {
    
};
websocket.onopen = function(evt) { onOpen(evt) };
function onError(evt) {
    
}
function onOpen() {
    alert("подключено")
}
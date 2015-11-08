var wsUri = "ws://" + document.location.host + document.location.pathname + "/socialsocket";
var socSocket = new WebSocket(wsUri);

socSocket.onerror = function (evt) {
    alert("Невозможно сединиться с сервером");
};

socSocket.onopen = function (evt) {
    
};

socSocket.onmessage = function (evt) 
{
    var json = evt.data;
    json = JSON.parse(json);

    //Определяем цель сообщения
    switch (json.target) 
    {
        case "removePlayer":
            removePlayerFromTable(json.nickname);
            break;
        case "joinPlayer":
            addPlayerToTable(json.nickname);
            break;
        case "setStatus":
            if(json.status === "ready")
            {
                setStatusOn(json.nickname);
            }
            else
            {
                setStatusOff(json.nickname);
            }
            break;
        case "kicked":
            $('.modal').modal('hide');
            ShowMSGDng("Хост расформировал лобби или вы были выгнаны :(");
            break;
    }
    
};
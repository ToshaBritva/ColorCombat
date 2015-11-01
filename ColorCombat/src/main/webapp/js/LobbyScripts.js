/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    socSocket.send('{"target":"joinLobby", "userHost":"' + userHost + '"}');
}

function leaveLobby()
{
    socSocket.send('{"target":"leaveLobby"}');
}

function setMaster(nickname)
{
    $('#CrLPModal .modal-title').text(nickname);
}

function getSlotNum(nickname)
{
    for (var i=0; i<3; i++)
    {
        if($("#Slot" + i + " #Nick" ).text()===nickname)
        {
            return i;
        }
    }
}
    
function changeStatus(nickname) ///Сменить статус игрока (поменять цвет и текст)
{
    var slotNum = getSlotNum(nickname);
    
    var curStatus = $("#Slot" + slotNum + " #Status").text();
    $("#Slot" + slotNum).removeClass();
    if(curStatus==='Готов')
    {
        $("#Slot" + slotNum + " #Status").text('Не готов');
        $("#Slot" + slotNum).toggleClass('danger');
    }
    else
    {
        $("#Slot" + slotNum + " #Status").text('Готов');
        $("#Slot" + slotNum).toggleClass('success');
    }
}

function addPlayerToTable(nickname) //Добавить игрока в таблицу лобби
{
    var slotNum = getSlotNum('Свободный слот');
    $("#Slot" + slotNum + " #Nick").text(nickname);
    $("#Slot" + slotNum + " #Status").text('Не готов');
    $("#Slot" + slotNum).toggleClass('danger');
}

function removePlayerFromTable(nickname)
{
    var slotNum = getSlotNum(nickname);
    
    $("#Slot" + slotNum + " #Nick").text('Свободный слот');
    $("#Slot" + slotNum + " #Status").text('');
    $("#Slot" + slotNum).removeClass();
}

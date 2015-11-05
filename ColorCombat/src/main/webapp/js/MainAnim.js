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
function addPlayerToTable(nickname) //Добавить игрока в таблицу лобби
{
    var slotNum = getSlotNum('Свободный слот');
    $("#CrLPModal #Slot" + slotNum + " #Nick").text(nickname);
    $("#CrLPModal #Slot" + slotNum + " #Status").append('<input class="switchBTM" type="checkbox"  data-size="mini">');
   
    $(".switchBTM").bootstrapSwitch();
}

function removePlayerFromTable(nickname)
{
    var slotNum = getSlotNum(nickname);
    
    $("#CrLPModal #Slot" + slotNum + " #Nick").text('Свободный слот');
    $("#CrLPModal #Slot" + slotNum + " #Status").text('');
    $("#CrLPModal #Slot" + slotNum).removeClass();
}

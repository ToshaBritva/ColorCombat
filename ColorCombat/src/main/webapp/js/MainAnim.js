/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function onloadPage (){
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
    

function changeStatus(nickname) ///Сменить статус игрока (поменять цвет и текст)
{
    var slotNum = getSlotNum(nickname);
    
    var curStatus = $("#CrLPModal #Slot" + slotNum + " #Status").text();
    $("#CrLPModal #Slot" + slotNum).removeClass();
    if(curStatus==='Готов')
    {
        $("#CrLPModal #Slot" + slotNum + " #Status").text('Не готов');
        $("#CrLPModal #Slot" + slotNum).toggleClass('danger');
    }
    else
    {
        $("#CrLPModal #Slot" + slotNum + " #Status").text('Готов');
        $("#CrLPModal #Slot" + slotNum).toggleClass('success');
    }
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
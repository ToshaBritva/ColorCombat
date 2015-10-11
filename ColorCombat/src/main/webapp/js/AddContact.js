/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function addContact(contactName) //не протестировано
{
    var Contact = '<a href="#" class="list-group-item text-left">' + contactName + '</a>'; //Заменить # на валидную ссылку
    $("#Contacts").Apend(Contact);
}


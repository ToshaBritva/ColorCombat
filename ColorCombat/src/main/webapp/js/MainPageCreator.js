/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function CreateMainPage()
{
   var mydiv = $('<div/>', {
    class:  'row header show-grid' 
    });
    $('body').append(mydiv);
    
    mydiv = $('<div/>', {
    id:  'NickName' ,
    class: 'span6'
    });
    $('.header').append(mydiv);
    $('#NickName').append('NAME');
    
    mydiv = $('<div/>', {
    id:  'Profile',
    class: 'span2 offset2'
    });
    $('.header').append(mydiv);
    $('#Profile').append('Profile');
    
    mydiv = $('<div/>', {
    id:  'exit' ,
    class: 'span2 '
    });
    $('.header').append(mydiv);
     
    mydiv = $('<button/>', {
        id: 'idExit',
     class: 'btn btn-primary btn-lg btn-block'
    });
    $('.header #exit').append(mydiv);
    $('#idExit').append("Exit");
     
    var mydiv = $('<div/>', {
        id:'content',
    class:  'row show-grid' 
    });
    $('body').append(mydiv);
    
    mydiv = $('<div/>', {
    id:  'Menu',
    class: 'span6 '
    });
    $('#content').append(mydiv);
            // $('#Menu').append('Menu');
    
    mydiv = $('<div/>', {
    id:  'Contact',
    class: 'span5  offset1'
    });
    $('#content').append(mydiv);
    $('#Contact').append('Contacts');
    
    
    var mydiv = $('<div/>', {
        id: 'new_Btn1',
      class:  'row new_Btn' 
    });
    $('#Menu').append(mydiv);
    
    mydiv = $('<button/>', {
        id: 'CrLobby',
     class: 'btn btn-primary btn-lg btn-block'
    });
    $('#new_Btn1').append(mydiv);
    $('#CrLobby').append("Создать лобби");
    
    var mydiv = $('<div/>', {
        id: 'new_Btn2',
      class:  'row new_Btn' 
    });
    $('#Menu').append(mydiv);
    
    mydiv = $('<button/>', {
        id: 'FindLobby',
     class: 'btn btn-primary btn-lg btn-block'
    });
    $('#new_Btn2').append(mydiv);
    $('#FindLobby').append("Найти лобби");
    
    var mydiv = $('<div/>', {
        id: 'new_Btn3',
      class:  'row new_Btn' 
    });
    $('#Menu').append(mydiv);
    
    mydiv = $('<button/>', {
        id: 'QuickLobby',
     class: 'btn btn-primary btn-lg btn-block'
    });
    $('#new_Btn3').append(mydiv);
    $('#QuickLobby').append("Быстрый поиск лобби");
    
    var mydiv = $('<div/>', {
        id: 'new_Btn4',
      class:  'row new_Btn ' 
    });
    $('#Menu').append(mydiv);
    
    mydiv = $('<button/>', {
        id: 'ShowLiders',
     class: 'btn btn-primary btn-lg btn-block'
    });
    $('#new_Btn4').append(mydiv);
    $('#ShowLiders').append("Таблица лидеров");
    
}


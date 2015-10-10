/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function CreateMainPage()
{
   var mydiv = $('<div/>', {
    class:  'header row' 
    });
    $('body').append(mydiv);
    
    mydiv = $('<div/>', {
    id:  'NickName' 
    });
    $('.header').append(mydiv);
    
    mydiv = $('<div/>', {
    id:  'exit'
    });
    $('.header').append(mydiv);
    mydiv = $('<button/>', {
     class: 'btn btn-default'
    });
    $('.header #exit').append(mydiv);
    $('.header #exit .btn').append("Exit");
    
    
    mydiv = $('<div/>', {
    id:  'Profile' 
    });
    $('.header').append(mydiv);
    
    
}


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

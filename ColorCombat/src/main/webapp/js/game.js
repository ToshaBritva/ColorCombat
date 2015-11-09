/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function ShowMSGDng(MSG, timeout)
{
    $('#MsgDng p').text(MSG);
    $('#MsgDng').fadeIn();
    setTimeout(function ()
    {
        $('#MsgDng').fadeOut();
    }, timeout);
}

function ShowMSG(MSG, timeout)
{
    $('#Msg p').text(MSG);
    $('#Msg').fadeIn();
    setTimeout(function ()
    {
        $('#Msg').fadeOut();
    }, timeout);
}

function ShowEndMSG(MSG) {
    $('#EndMsg p').text(MSG);
    $('#EndMsg').fadeIn();
}

function onMainPage() {
    // similar behavior as an HTTP redirect
    window.location.replace(document.referrer);
}


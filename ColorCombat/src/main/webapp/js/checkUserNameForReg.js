/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $("#nickname").focusout(function () {
        var inputText = $("#nickname").val();
        $.ajax({
            url: 'checkname',
            data: ({
                nickname: inputText
            }),
            success: function (data) {
                if (data == 0)
                {
                    $("#button").prop('disabled', false);
                }
                else
                {
                    $("#button").prop('disabled', true);
                }
            }
        });
    });
})




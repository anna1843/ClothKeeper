//$(document).ready(function() {
//    $('#memberNickName').on('input', function() {
//        var inputLength = $(this).val().length;
//        if (inputLength > 15) {
//            $(this).val($(this).val().substr(0, 15));
//            $('#nickNameError').text('15 글자까지 입력 가능합니다.').show();
//        } else if (inputLength < 2) {
//            $('#nickNameError').text('2 글자 이상 입력해야 합니다.').show();
//        } else {
//            $('#nickNameError').hide();
//        }
//    });
//
//    $('#memberPhone').on('input', function() {
//        var input = $(this).val().replace(/[^0-9]/g, '');
//        var inputLength = input.length;
//        if (inputLength > 11) {
//            $(this).val(input.substr(0, 11));
//            $('#phoneError').text('11 글자까지 입력 가능합니다.').show();
//        } else if (inputLength < 10) {
//            $('#phoneError').text('10 글자 이상 입력해야 합니다.').show();
//        } else {
//            $('#phoneError').hide();
//        }
//        $(this).val(input);
//    });

    $('form').submit(function(e) {
        e.preventDefault();

        $.ajax({
            type: 'POST',
            url: $(this).attr('action'),
            data: $(this).serialize(),
            success: function(response) {
                $('#foundEmail').text('찾은 이메일: ' + response);
                $('#foundEmail').show();
            },
            error: function(xhr, textStatus, errorThrown) {
                if (xhr.status === 404 && xhr.responseText === 'EmailNotFound') {
                    $('#foundEmail').text('일치하는 이메일을 찾을 수 없습니다.');
                    $('#foundEmail').show();
                }
            }
        });
    });
//});
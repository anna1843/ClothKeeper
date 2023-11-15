$(function(){
    $('#newPassword').keyup(function(){
        $('#checkMessage').html('');
    });

    $('#confirmNewPassword').keyup(function(){
        if ($('#newPassword').val() != $('#confirmNewPassword').val()) {
            $('#checkMessage').html('비밀번호 일치하지 않음<br><br>');
            $('#checkMessage').css('color', 'red');
        } else {
            $('#checkMessage').html('비밀번호 일치함<br><br>');
            $('#checkMessage').css('color', 'green');
        }
    });

    var currentPasswordChanged = false;
    $('#currentPassword').keyup(function(){
        currentPasswordChanged = true;
    });

    $('#checkCurrentPasswordBtn').click(function() {
        var currentPassword = $('#currentPassword').val();
        var memberId = $('#memberId').val();

        $.ajax({
            url: '/member/checkCurrentPassword',
            method: 'POST',
            data: { memberId: memberId, currentPassword: currentPassword },
            success: function(response) {
                if (response.valid) {
                    $('#currentPasswordMessage').css('color', 'green').text('현재 비밀번호가 일치합니다.');
                } else {
                    $('#currentPasswordMessage').css('color', 'red').text('현재 비밀번호가 일치하지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                $('#currentPasswordMessage').css('color', 'red').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
                console.error(error);
            }
        });
    });

    $('#changePasswordBtn').click(function(e) {
        if ($('#currentPasswordMessage').text() === '') {
            e.preventDefault();
            alert('현재 비밀번호 확인을 먼저 해주세요.');
        } else if ($('#newPassword').val() !== $('#confirmNewPassword').val()) {
            e.preventDefault();
            alert('변경할 비밀번호가 일치하지 않습니다. 다시 확인해주세요.');
        } else if (currentPasswordChanged && $('#currentPasswordMessage').text() !== '현재 비밀번호가 일치합니다.') {
            e.preventDefault();
            alert('현재 비밀번호를 확인해주세요.');
        } else if ($('#currentPassword').val() === $('#newPassword').val()) {
            e.preventDefault();
            alert('현재 비밀번호와 신규 비밀번호가 같습니다.');
        } else if (!/^(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{8,}$/.test($('#newPassword').val())) {
            e.preventDefault();
            alert('신규 비밀번호는 알파벳, 특수문자, 숫자를 최소 하나씩 포함하고 최소 8자 이상 입력해야 합니다.');
        }
    });
});

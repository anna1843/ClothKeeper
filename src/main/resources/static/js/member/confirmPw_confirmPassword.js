$(function() {
    // 비밀번호 확인 버튼 클릭 이벤트 핸들러
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
                    // 비밀번호 확인이 성공하면 비밀번호 변경 버튼 활성화
//                    $('#changePasswordBtn').prop('disabled', false);
                } else {
                    $('#currentPasswordMessage').css('color', 'red').text('현재 비밀번호가 일치하지 않습니다.');
                    // 비밀번호 확인이 실패하면 비밀번호 변경 버튼 비활성화
//                    $('#changePasswordBtn').prop('disabled', true);
                }
            },
            error: function(xhr, status, error) {
                $('#currentPasswordMessage').css('color', 'red').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
                console.error(error);
            }
        });
    });

    var currentPasswordChanged = false; // Flag to track if current password has changed
        $('#currentPassword').keyup(function(){
            currentPasswordChanged = true;
        });

    $('#changePasswordBtn').click(function(e) {
            if ($('#currentPasswordMessage').text() === '') {
                e.preventDefault();
                alert('현재 비밀번호 확인을 먼저 해주세요.');
            } else if (currentPasswordChanged && $('#currentPasswordMessage').text() !== '현재 비밀번호가 일치합니다.') {
                e.preventDefault();
                alert('현재 비밀번호를 확인해주세요.');
            }
        });
});
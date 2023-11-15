$(document).ready(function () {
    // 중복 확인 결과
    let isEmailAvailable = false;
    let isNickNameAvailable = false;

    // 이메일 중복 확인 버튼 클릭 시 이벤트 처리
    $('#emailCheckButton').click(function () {
        $.ajax({
            url: '/api/member/memberEmail/check',
            type: 'GET',
            contentType: 'application/json',
            data: {
                memberEmail: $('#memberEmail').val()
            },
            success: function (result) {
                $('#emailNotAvailable').hide();
                $('#emailAvailable').show().text(result).append($('<br />'));
                isEmailAvailable = true;
//                alert('사용 가능한 이메일입니다.');

            },
            error: function (error) {
                $('#emailAvailable').hide();
                $('#emailNotAvailable').show().text(error.responseJSON['message']).append($('<br />'));
                isEmailAvailable = false;
//                alert('이미 사용 중인 이메일입니다.');
            }
        });
    });
    // 닉네임 중복 확인 버튼 클릭 시 이벤트 처리
    $('#nickNameCheckButton').click(function () {
        $.ajax({
            url: '/api/member/memberNickName/check',
            type: 'GET',
            contentType: 'application/json',
            data: {
                memberNickName: $('#memberNickName').val()
            },
            success: function (result) {
                $('#nickNameNotAvailable').hide();
                $('#nickNameAvailable').show().text(result).append($('<br />'));
                isNickNameAvailable = true;
//                alert('사용 가능한 닉네임입니다.');

            },
            error: function (error) {
                $('#nickNameAvailable').hide();
                $('#nickNameNotAvailable').show().text(error.responseJSON['message']).append($('<br />'));
                isNickNameAvailable = false;
//                alert('이미 사용 중인 닉네임입니다.');
            }
        });
    });

    // 회원가입 버튼 클릭 시 이벤트 처리
    $('form').submit(function (event) {
        if (!isEmailAvailable && !isNickNameAvailable) {
            event.preventDefault(); // 이벤트 중단
            alert('이메일과 닉네임 중복 확인을 해주세요.');
        } else if (!isEmailAvailable) {
            event.preventDefault(); // 이벤트 중단
            alert('이메일 중복 확인을 해주세요.');
        } else if (!isNickNameAvailable) {
            event.preventDefault(); // 이벤트 중단
            alert('닉네임 중복 확인을 해주세요.');
        }
    });
});
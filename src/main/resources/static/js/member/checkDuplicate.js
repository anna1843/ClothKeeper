$(document).ready(function () {
    // 중복 확인 결과
    let isEmailAvailable = false;
    let isNickNameAvailable = false;
    let isPhoneAvailable = false;

    // 이메일 중복 확인 버튼 클릭 시 이벤트 처리
    $('#emailCheckButton').click(function () {

    // 현재 입력된 이메일 값 가져오기
        let memberEmail = $('#memberEmail').val();

        $.ajax({
            url: '/api/member/memberEmail/check',
            type: 'GET',
            contentType: 'application/json',
            data: {
                memberEmail: memberEmail
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

    // 휴대전화번호 중복 확인 버튼 클릭 시 이벤트 처리
    $('#phoneCheckButton').click(function () {
        $.ajax({
            url: '/api/member/memberPhone/check',
            type: 'GET',
            contentType: 'application/json',
            data: {
                memberPhone: $('#memberPhone').val()
            },
            success: function (result) {
                $('#phoneNotAvailable').hide();
                $('#phoneAvailable').show().text(result).append($('<br />'));
                isPhoneAvailable = true;
//                alert('사용 가능한 닉네임입니다.');

            },
            error: function (error) {
                $('#phoneAvailable').hide();
                $('#phoneNotAvailable').show().text(error.responseJSON['message']).append($('<br />'));
                isPhoneAvailable = false;
//                alert('이미 사용 중인 닉네임입니다.');
            }
        });
    });

    // 회원가입 버튼 클릭 시 이벤트 처리
    $('form').submit(function (event) {
        if (!isEmailAvailable && !isNickNameAvailable && !isPhoneAvailable) {
            event.preventDefault(); // 이벤트 중단
            alert('이메일과 닉네임, 휴대전화번호 중복 확인을 해주세요.');
        } else if (!isNickNameAvailable) {
            event.preventDefault(); // 이벤트 중단
            alert('닉네임 중복 확인을 해주세요.');
        } else if (!isPhoneAvailable) {
            event.preventDefault(); // 이벤트 중단
            alert('휴대전화번호 중복 확인을 해주세요.');
        }
    });
});
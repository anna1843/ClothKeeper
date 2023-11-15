$(document).ready(function() {
    var sentVerificationCode; // 전송된 인증번호를 저장할 변수
    var isVerified = false; // 인증번호 확인 여부를 나타내는 변수
    var isEmailAvailable = false; // 이메일 중복 확인 여부를 나타내는 변수

// 이메일 중복 확인 버튼 클릭 시 이벤트 처리
    $('#emailCheckButton').click(function () {
        $.ajax({
            url: '/api/member/memberEmail/check',
            type: 'GET',
            contentType: 'application/json',
            data: {
                memberEmail: $('#semiMemberEmail').val()
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
    // 인증번호 전송 버튼 클릭 시 이벤트 처리
    $("#certifyEmailButton").click(function() {
            // 이메일 중복 확인 버튼을 눌렀는지 확인
            if (!isEmailAvailable) {
                alert("이메일 중복 확인을 먼저 해주세요.");
                return;
            }
            var email = $("input[name='semiMemberEmail']").val();

        // 서버로 이메일 주소 전송 및 인증 요청
        $.ajax({
            type: "POST",
            url: "/send-mail/email",
            data: JSON.stringify({ email: email }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {
                console.log(response); // 이 줄을 추가하여 response 내용 확인
                sentVerificationCode = response.code; // 서버에서 전송한 인증번호 저장
                $("#certifyEmailMessage").text("이메일로 인증번호가 전송되었습니다.").show();
                isVerified = false; // 인증번호 확인 여부 초기화
            },
            error: function() {
                $("#certifyEmailMessage").text("이메일 인증번호 전송에 실패했습니다.").show();
            }
        });
    });

    $("#verifyEmailButton").click(function() {
        var enteredCode = $("#emailVerificationCode").val();
        if (enteredCode === "") {
            alert("인증번호를 입력해주세요."); // 인증번호란이 비어있을 때 알림 표시
            return;
        }

        if (enteredCode === sentVerificationCode) {
            $("#certifyEmailMessage").text("인증번호 일치").show();
            isVerified = true; // 인증번호 확인됨
        } else {
            $("#certifyEmailMessage").text("인증번호 불일치").show();
            isVerified = false; // 인증번호 확인 여부 초기화
        }
    });

    // 이메일 입력란에 변동 생기면 이메일 중복 확인 여부 초기화 됨
    $('#semiMemberEmail').on('input', function() {
            isEmailAvailable = false;
            isVerified = false;
            $('#emailAvailable').hide();
            $('#emailNotAvailable').hide();
            $('#certifyEmailMessage').hide();
    });

    // select 엘리먼트 변경 시 이메일 중복 확인 및 인증번호 확인 여부 초기화
        $('#domainList').on('change', function() {
                isEmailAvailable = false;
                isVerified = false;
                $('#emailAvailable').hide();
                $('#emailNotAvailable').hide();
                $('#certifyEmailMessage').hide();
        });

    $("#signupForm").submit(function(event) {
            if ($("#emailVerificationCode").val() !== sentVerificationCode) {
                event.preventDefault(); // 인증번호 확인이 되지 않으면 제출을 막음
                $("#certifyEmailMessage").hide();
                alert("인증번호를 확인해주세요.");
            } else if (!isEmailAvailable || !isVerified) {
               event.preventDefault(); // 인증 및 중복 확인이 되지 않으면 제출을 막음
               if (!isEmailAvailable) {
                   alert("이메일 중복 확인해주세요.");
               } else {
                   alert("이메일 인증을 완료해주세요.");
               }
           }
    });
});
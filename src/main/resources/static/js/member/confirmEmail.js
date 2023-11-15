$(document).ready(function() {
    var sentVerificationCode; // 전송된 인증번호를 저장할 변수
    var isVerified = false; // 인증번호 확인 여부를 나타내는 변수
    var isEmailAvailable = false; // 이메일 중복 확인 여부를 나타내는 변수

    // 인증번호 '전송' 버튼 클릭 시 이벤트 처리
    $("#certifyEmailButton").click(function() {

        var memberEmail = $('#memberEmail').val();

        // 서버로 이메일 주소 전송 및 인증 요청
        $.ajax({
            type: "POST",
            url: "/send-mail/email",
            data: JSON.stringify({ email: memberEmail }),
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
    // 인증번호 '확인' 버튼 클릭 시 이벤트 처리
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

    $("#confirmEmailForm").submit(function(event) {
            if ($("#emailVerificationCode").val() !== sentVerificationCode) {
                event.preventDefault(); // 인증번호 확인이 되지 않으면 제출을 막음
                $("#certifyEmailMessage").hide();
                alert("인증번호를 확인해주세요."); // 팝업창 띄우기
            }else {
                 // 인증번호가 확인되면 확인/취소 팝업창 띄우기
                if (confirm("삭제 후 복구할 수 없습니다. 정말 삭제하시겠습니까?")) {
                    // '확인'을 선택한 경우에만 폼을 제출
                } else {
                    // '취소'를 선택한 경우
                    event.preventDefault(); // 제출 방지
                }
            }
    });
});
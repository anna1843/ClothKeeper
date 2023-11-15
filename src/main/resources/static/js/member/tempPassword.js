$(document).ready(function() {
    $("#tempPasswordForm").submit(function(event) {
        event.preventDefault();

        var email = $("#memberEmail").val();
        var phone = $("#memberPhone").val();

        // 서버로 이메일과 휴대전화번호 일치 여부를 확인 요청
        $.ajax({
            type: "GET",
            url: "/check-emailPhoneMatching",
            data: {
                memberEmail: email,
                memberPhone: phone
            },
            success: function(response) {
                if (response.matching) {
                    // 이메일과 휴대전화번호가 모두 일치할 경우 임시비밀번호 발급 요청
                    $.ajax({
                        type: "POST",
                        url: "/send-mail/password",
                        data: JSON.stringify({ email: email, phone: phone }),
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        success: function(response) {
                            console.log(response);
                            $("#tempPasswordMessage").text("해당 이메일로 임시비밀번호가 전송되었습니다.").show();
                        },
                        error: function(xhr, textStatus, errorThrown) {
                            alert("이메일 임시비밀번호 전송에 실패했습니다.");
                        }
                    });
                } else {
                    $("#tempPasswordMessage").text("이메일과 휴대전화번호가 일치하지 않습니다.").show();
                }
            },
            error: function(xhr, textStatus, errorThrown) {
                alert("일치 여부 확인에 실패했습니다.");
            }
        });
    });
});
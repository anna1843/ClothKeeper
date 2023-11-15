$(document).ready(function() {
    $("#loginForm").submit(function(event) {
        event.preventDefault();

        var email = $("#memberEmail").val();
        var password = $("#memberPassword").val();

        $.ajax({
            type: "POST",
            url: "/member/login",  // 실제 경로에 맞게 수정해야 함
            data: {
                memberEmail: email,
                memberPassword: password
            },
            success: function(response) {
                if (response.isTemporaryPassword) {
                    alert("임시비밀번호로 로그인 중입니다. 비밀번호를 변경해주세요.");
                } else {
                    alert("로그인 성공");
                    // 로그인 성공 시 원하는 페이지로 이동
                }
            },
            error: function(xhr, textStatus, errorThrown) {
                alert("로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.");
            }
        });
    });
});
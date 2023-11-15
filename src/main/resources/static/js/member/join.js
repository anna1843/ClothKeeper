$(document).ready(function() {
    // 비밀번호 검증 함수
    function validatePassword(password) {
        // 최소 8자리 이상, 알파벳, 특수문자, 숫자가 각각 하나 이상 포함되어야 함
        var regex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{8,}$/;
        return regex.test(password);
    }

    // 회원가입 폼 전송(submit) 이벤트 처리
    $('#signupForm').submit(function(event) {
        var password = $('#memberPassword').val();
        var confirmPassword = $('#confirmPassword').val();
        var phoneInput = $('#memberPhone').val();
        var postCode = $('#memberPostCode').val();
        var detailAddress = $('#memberDetailAddress').val();

        if (!validatePassword(password)) {
            alert("비밀번호는 알파벳, 특수문자, 숫자가 최소 하나씩 포함되어야하고 8자리 이상이어야합니다");
            // 폼 전송 중단
            event.preventDefault();
        } else if (password !== confirmPassword) {
            alert("비밀번호와 비밀번호 확인 값이 일치하지 않습니다.");
            // 폼 전송 중단
            event.preventDefault();
        } else if (!/^[0-9]{10,11}$/.test(phoneInput)) {
            alert("휴대전화번호는 10~11자리의 숫자만 입력 가능합니다.");
            // 폼 전송 중단
            event.preventDefault();
        } else if (postCode === "") {
            alert("우편번호를 입력해주세요.");
            // 폼 전송 중단
            event.preventDefault();
        } else if (detailAddress === "") {
            alert("상세주소를 입력해주세요.");
            // 폼 전송 중단
            event.preventDefault();
        }
    });
});
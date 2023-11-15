// semiMember
// 휴대전화 입력란 관련 요소들 가져오기
const semiMemberPhoneInput = document.querySelector('#semiMemberPhone');
const semiMemberPhoneErrorMessage = document.querySelector('#semiMemberPhoneErrorMessage');

// 휴대전화 입력란 유효성 검사 함수
function validateSemiMemberPhone() {
  const semiMemberPhone = semiMemberPhoneInput.value;
  // 정규 표현식을 사용하여 숫자 이외의 문자를 찾음
  const invalidCharacters = semiMemberPhone.match(/[^0-9]/g);

  if (invalidCharacters) {
    // 숫자 이외의 문자가 입력되면 오류 메시지 출력
    semiMemberPhoneErrorMessage.textContent = "숫자만 입력 가능합니다";
  } else {
    // 숫자만 입력된 경우 오류 메시지 제거
    semiMemberPhoneErrorMessage.textContent = "";
  }
}
// 값이 변경될 때마다 validateMemberPhone 함수 호출
semiMemberPhoneInput.addEventListener('input', validateSemiMemberPhone);
// 이름 입력란 관련 요소들 가져오기
const memberNameInput = document.querySelector('#memberName');
const memberNameErrorMessage = document.querySelector('#memberNameErrorMessage');

// 이름 입력란 유효성 검사 함수
function validateMemberName() {
  const memberName = memberNameInput.value;
  // 정규 표현식을 사용하여 한글과 영어 이외의 문자를 찾음
  const invalidCharacters = memberName.match(/[^가-힣a-zA-Z]/g);

  if (invalidCharacters) {
    // 한글과 영어 이외의 문자가 입력되면 오류 메시지 출력
    memberNameErrorMessage.textContent = "한글 또는 영어만 입력 가능합니다";
  } else {
    // 한글과 영어만 입력된 경우 오류 메시지 제거
    memberNameErrorMessage.textContent = "";
  }
}
// 값이 변경될 때마다 validateMemberName 함수 호출
memberNameInput.addEventListener('input', validateMemberName);



// 닉네임 입력란 관련 요소들 가져오기
const memberNickNameInput = document.querySelector('#memberNickName');
const memberNickNameErrorMessage = document.querySelector('#memberNickNameErrorMessage');

// 닉네임 입력란 유효성 검사 함수
function validateMemberNickName() {
  const memberNickName = memberNickNameInput.value;
  // 정규 표현식을 사용하여 한글, 영어, 숫자 이외의 문자를 찾음
  const invalidCharacters = memberNickName.match(/[^가-힣a-zA-Z0-9]/g);

  if (invalidCharacters) {
    // 한글, 영어, 숫자 이외의 문자가 입력되면 오류 메시지 출력
    memberNickNameErrorMessage.textContent = "한글, 영어, 숫자만 입력 가능합니다";
  } else {
    // 한글, 영어, 숫자만 입력된 경우 오류 메시지 제거
    memberNickNameErrorMessage.textContent = "";
  }
}
// 값이 변경될 때마다 validateMemberNickName 함수 호출
memberNickNameInput.addEventListener('input', validateMemberNickName);



// 휴대전화 입력란 관련 요소들 가져오기
const memberPhoneInput = document.querySelector('#memberPhone');
const memberPhoneErrorMessage = document.querySelector('#memberPhoneErrorMessage');

// 휴대전화 입력란 유효성 검사 함수
function validateMemberPhone() {
  const memberPhone = memberPhoneInput.value;
  // 정규 표현식을 사용하여 숫자 이외의 문자를 찾음
  const invalidCharacters = memberPhone.match(/[^0-9]/g);

  if (invalidCharacters) {
    // 숫자 이외의 문자가 입력되면 오류 메시지 출력
    memberPhoneErrorMessage.textContent = "숫자만 입력 가능합니다";
  } else {
    // 숫자만 입력된 경우 오류 메시지 제거
    memberPhoneErrorMessage.textContent = "";
  }
}
// 값이 변경될 때마다 validateMemberPhone 함수 호출
memberPhoneInput.addEventListener('input', validateMemberPhone);


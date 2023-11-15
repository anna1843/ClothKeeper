// JavaScript 함수: 드롭다운 값을 실시간으로 입력칸에 반영
function updateBirthInput() {
    // 선택한 연도, 월, 일 가져오기
    const yearSelect = document.getElementById("birthYear");
    const monthSelect = document.getElementById("birthMonth");
    const daySelect = document.getElementById("birthDay");

    const selectedYear = yearSelect.value;
    const selectedMonth = monthSelect.value;
    const selectedDay = daySelect.value;

    // 두 자리 숫자로 만들기 위해 '0'을 추가
    const formattedMonth = selectedMonth.padStart(2, '0');
    const formattedDay = selectedDay.padStart(2, '0');

    // 생년월일 입력칸에 반영
    const birthInput = document.getElementById("memberBirth");
    birthInput.value = selectedYear + formattedMonth + formattedDay;

    // 입력값이 변경되면 유효성 검사를 다시 수행
    validateBirth(birthInput);
    console.log("입력된 생년월일:", birthInput.value); // 콘솔에 입력된 생년월일 출력
}

// 드롭다운 변경 이벤트 리스너 추가
const yearSelect = document.getElementById("birthYear");
const monthSelect = document.getElementById("birthMonth");
const daySelect = document.getElementById("birthDay");

yearSelect.addEventListener("change", updateBirthInput);
monthSelect.addEventListener("change", updateBirthInput);
daySelect.addEventListener("change", updateBirthInput);

// JavaScript 함수: 생년월일 입력칸 클릭 시 드롭다운 표시 및 숨기기
function toggleBirthDropdowns() {
    const birthDropdowns = document.getElementById("birthDropdowns");
    if (birthDropdowns.style.display === "none" || birthDropdowns.style.display === "") {
        birthDropdowns.style.display = "block";
    } else {
        birthDropdowns.style.display = "none";
    }
}

// 생년월일 유효성 검사 함수
function validateBirth() {
    // 생년월일 입력란의 값 가져오기
      var birth = document.getElementById('memberBirth').value;

      // 생년월일이 8자리 숫자인 경우에만 오류 메시지를 숨김
      if (birth.length === 8 && /^[0-9]{8}$/.test(birth)) {
          document.getElementById('birthErrorMessage').textContent = "";
      }
  }
  // 페이지 로드 시 유효성 검사 수행
  window.onload = validateBirth;

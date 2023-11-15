//function checkInput(inputField, fieldName, minLength, maxLength) {
//      let inputValue = inputField.value.trim();
//      let inputLength = inputValue.length;
//
//      if (inputLength < minLength) {
//        return fieldName + " : 최소 " + minLength + "글자 이상 입력하세요.";
//      } else if (inputLength > maxLength) {
//        return fieldName + " : 최대 " + maxLength + "글자까지 입력 가능합니다.";
//      }
//
//      // 이름 필드에만 한글과 영문 이외의 입력 시 팝업창 표시
//     if (fieldName === "이름" && !/^[가-힣a-zA-Z]+$/.test(inputValue)) { // 정규표현식 사용
//       return fieldName + " : 한글과 영문만 입력 가능합니다.";
//     }
//
//    return "";
//}
//
//function onSubmitForm() {
//  let memberNameField = document.getElementsByName("memberName")[0];
//  let memberNickNameField = document.getElementsByName("memberNickName")[0];
//  let memberPhoneField = document.getElementsByName("memberPhone")[0];
//  let memberBirthField = document.getElementsByName("memberBirth")[0];
//  let memberAddressField = document.getElementsByName("memberAddress")[0];
//
//  let memberNameErrorMessage = checkInput(memberNameField, "이름", 2, 50);
//  let memberNickNameErrorMessage = checkInput(memberNickNameField, "닉네임", 2, 15);
//  let memberPhoneErrorMessage = checkInput(memberPhoneField, "휴대전화번호", 10, 11);
//  let memberBirthErrorMessage = checkInput(memberBirthField, "생년월일", 8, 8);
//  let memberAddressErrorMessage = checkInput(memberAddressField, "주소", 2, 255);
//
//  let errorMessage = [memberNameErrorMessage, memberNickNameErrorMessage, memberPhoneErrorMessage,
//                      memberBirthErrorMessage, memberAddressErrorMessage].filter(Boolean).join("\n");
//
//  if (errorMessage) {
//    alert(errorMessage);
//
//    if (memberNameErrorMessage) {
//      memberNameField.focus();
//    } else if (memberNickNameErrorMessage) {
//      memberNickNameField.focus();
//    } else if (memberPhoneErrorMessage) {
//      memberPhoneField.focus();
//    } else if (memberBirthErrorMessage) {
//      memberBirthField.focus();
//    } else if (memberAddressErrorMessage) {
//      memberAddressField.focus();
//    }
//    return false; // 폼 제출 중단
//  }
//
//  let memberNickNameValue = memberNickNameField.value.trim();
//  let originalMemberNickNameValue = memberNickNameField.getAttribute("data-original-value"); // 기존 닉네임 값
//
//  if (memberNickNameValue !== originalMemberNickNameValue) { // 값이 변경되었을 때만 닉네임 중복 체크
//    $.ajax({
//      url: '/api/member/memberNickName/check',
//      type: 'GET',
//      contentType: 'application/json',
//      data: {
//        memberNickName: memberNickNameValue
//      },
//      success: function (result) {
//          if (result ===  "사용가능한 닉네임입니다") {
//          document.forms[0].submit();
//        }
//      },
//      error: function (error) {
//        console.log(error); // 에러 정보 콘솔에 출력
//        alert('닉네임 중복입니다');
//      }
//    });
//    return false; // 폼 제출 중단
//  } else {
//    console.log("닉네임 값이 변경되지 않았습니다.");
//    document.forms[0].submit();
//  }
//
//  let memberPhoneValue = memberPhoneField.value.trim();
//  let originalMemberPhoneValue = memberPhoneField.getAttribute("data-original-value"); // 기존 휴대전화번호 값
//
//  if (memberPhoneValue !== originalMemberPhoneValue) { // 값이 변경되었을 때만 휴대전화번호 중복 체크
//    $.ajax({
//      url: '/api/member/memberPhone/check',
//      type: 'GET',
//      contentType: 'application/json',
//      data: {
//        memberPhone: memberPhoneValue
//      },
//      success: function (result) {
//          if (result ===  "사용가능한 휴대전화번호입니다") {
//          document.forms[0].submit();
//          }
//      },
//      error: function (error) {
//        alert('휴대전화번호 중복입니다');
//      }
//    });
//    return false; // 폼 제출 중단
//  } else {
//    console.log("휴대전화번호 값이 변경되지 않았습니다.");
//    document.forms[0].submit();
//  }
//}

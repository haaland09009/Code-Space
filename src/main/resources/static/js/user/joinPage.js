
   /* 회원가입 가능 여부 확인 */
   let idChecked = false;
   let pwChecked = false;
   let nickChecked = false;


   /* 이메일 중복 체크, 정규식 검사 */
   const existsUserId = () => {
       const userIdValue = document.querySelector("#userEmail").value;

       const existsIdBox = document.getElementById("existsIdBox");
       const possibleIdBox = document.getElementById("possibleIdBox");

       const incorrectIdBox = document.querySelector("#incorrectIdBox");

       /* 정규식 */
       const idRegEx = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;

    /*   if (!idRegEx.test(userIdValue)) {
           idChecked = false;
           incorrectIdBox.style.display = "block";
           possibleIdBox.style.display = "none";
           return;
       } else {
           idChecked = true;
           incorrectIdBox.style.display = "none";

           if (userIdValue == "") {
               existsIdBox.style.display = "none";
               possibleIdBox.style.display = "none";
               idChecked = false;
               return;
           }
       }*/
       $.ajax({
            type: "get",
            url: "/user/existsUserId?email=" + userIdValue,
            success: function(res) {
                if (res == true) {
                    idChecked = false;
                    existsIdBox.style.display = "block";
                    possibleIdBox.style.display = "none";
                } else {
                    idChecked = true;
                    existsIdBox.style.display = "none";
                    possibleIdBox.style.display = "block";

                    if (userIdValue == "") {
                        possibleIdBox.style.display = "none";
                        idChecked = false;
                    }
                }
            },
            error: function(err) {
                return;
            }
        });
   }


   /* 아이디 입력 시 중복 체크 */
   document.querySelector("#userEmail").addEventListener("blur", existsUserId);
  /* document.querySelector("#userEmail").addEventListener("keyup", existsUserId);*/



    /* 비밀번호 일치 여부 확인 */
    const checkPwValue = () => {

        const userPwBox = document.querySelector("#userPassword");
        const userPwCheckBox = document.querySelector("#userPasswordCheck");

        const pwNotMatchBox = document.querySelector("#pwNotMatchBox");
        const pwMatchBox = document.querySelector("#pwMatchBox");
        const pwRegNotMatchBox = document.querySelector("#pwRegNotMatchBox");

        const passwordRegEx = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&!*])[A-Za-z\d@#$%^&!*]{8,20}$/;


     /*  if (!passwordRegEx.test(userPwBox.value)) {
           pwChecked = false;
           pwRegNotMatchBox.style.display = "block";
           return;
       } else {
          pwRegNotMatchBox.style.display = "none";
       }*/

        if (userPwBox.value != userPwCheckBox.value) {
            pwMatchBox.style.display = "none";
            pwNotMatchBox.style.display = "block";
            pwChecked = false;
        } else {
            pwNotMatchBox.style.display = "none";
            pwMatchBox.style.display = "block";
            pwChecked = true;
        }

        if (userPwCheckBox.value == "" && pwNotMatchBox.style.display == "block") {
            pwNotMatchBox.style.display = "none";
        }
    }

    /* 비밀번호 확인 입력 시 일치 여부 확인 */
    document.querySelector("#userPassword").addEventListener("blur", checkPwValue);
    document.querySelector("#userPasswordCheck").addEventListener("blur", checkPwValue);


    /* 닉네임 중복 체크 */
    const checkNicknameValue = () => {
        const userNicknameBox = document.querySelector("#userNickname");
        const existsNicknameBox = document.querySelector("#existsNicknameBox");
        const possibleNicknameBox = document.querySelector("#possibleNicknameBox");

        $.ajax({
            type: "get",
            url: "/user/existsNickname?nickname=" + userNicknameBox.value,
            success: function(res) {
                if (res == true) {
                    nickChecked = false;
                    possibleNicknameBox.style.display = "none";
                    existsNicknameBox.style.display = "block";
                } else {
                    nickChecked = true;
                    existsNicknameBox.style.display = "none";
                    possibleNicknameBox.style.display = "block";
                }
                if (userNicknameBox.value == "") {
                    possibleNicknameBox.style.display = "none";
                    existsNicknameBox.style.display = "none";
                    nickChecked = false;
                }
            },
            error: function(err) {
                return;
            }
        });
    }

    document.querySelector("#userNickname").addEventListener("keyup", checkNicknameValue);

   /* 회원가입 프로세스 */
   const checkValueAndSubmit = () => {

       const email = document.querySelector("#userEmail");
       const nickname = document.querySelector("#userNickname");
       const job = document.querySelector("#jobBox");
       const password = document.querySelector("#userPassword");
       const passwordCheck = document.querySelector("#userPasswordCheck");

       const checkJobBox = document.querySelector("#checkJobBox");


       if (email.value.trim() == "") {
         alert("이메일을 입력해주세요.");
         email.focus();
         return;
       } else if (nickname.value.trim() == "") {
         alert("닉네임을 입력해주세요.");
         nickname.focus();
         return;
       }  else if (job.value.trim() == "") {
         alert("직군을 선택해주세요.");
         job.focus();
         return;
       } else if (password.value.trim() == "") {
         alert("비밀번호를 입력해주세요.");
         password.focus();
         return;
       } else if (passwordCheck.value.trim() == "") {
         alert("비밀번호 확인란을 입력해주세요.");
         passwordCheck.focus();
         return;
       } else if (password.value != passwordCheck.value) {
         alert("비밀번호가 일치하지 않습니다.");
         passwordCheck.value = "";
         passwordCheck.focus();
         return;
       }

       if (!idChecked) {
          alert("아이디를 다시 입력해주세요.");
          return;
       } else if (!pwChecked) {
          alert("비밀번호를 다시 확인해주세요.");
          return;
       } else if (!nickChecked) {
          alert("닉네임을 다시 확인해주세요.");
          return;
       }

       else {
         const frm = document.querySelector("#frm");
         frm.submit();

         const joinSuccessModal = bootstrap.Modal.getOrCreateInstance("#joinSuccessModal");
         joinSuccessModal.show();

         setTimeout(function() {
            joinSuccessModal.hide();
         }, 5000);

   }
 }

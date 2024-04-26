
  /* 회원수정 가능 여부 확인 */
  let idChecked = true;
  let pwChecked = true;
  let nickChecked = true;



  /* 이메일 중복 체크, 정규식 검사 */
  const existsUserId = () => {
     const userEmailBox = document.querySelector("#userEmail");
     const emailError = document.querySelector("#emailError");
     const email_duplicated_error = document.querySelector("#email_duplicated_error");
     const possibleEmailBox = document.querySelector("#possibleEmailBox");

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
          url: "/user/existsUserId?email=" + userEmailBox.value,
          success: function(res) {
              if (res == true) {
                  idChecked = false;
                  email_duplicated_error.style.display = "block";
                  possibleEmailBox.style.display = "none";
                  emailError.style.display = "none";
                  userEmailBox.classList.remove("greenFocus");
                  userEmailBox.classList.add("redFocus");
              } else {
                  if (userEmailBox.value == "") {
                      idChecked = false;
                      userEmailBox.classList.add("redFocus");
                      userEmailBox.classList.remove("greenFocus");
                      possibleEmailBox.style.display = "none";
                      email_duplicated_error.style.display = "none";
                      emailError.style.display = "block";
                  } else {
                    idChecked = true;
                    userEmailBox.classList.remove("redFocus");
                    userEmailBox.classList.add("greenFocus");
                    emailError.style.display = "none";
                    email_duplicated_error.style.display = "none";
                    possibleEmailBox.style.display = "block";
                  }
              }
          },
          error: function(err) {
              return;
          }
      });
    }

  document.querySelector("#userEmail").addEventListener("blur", existsUserId);

  /* 닉네임 중복 체크 */
  const checkNicknameValue = () => {
      const userNicknameBox = document.querySelector("#userNickname");
      const nicknameError = document.querySelector("#nicknameError");
      const nick_duplicated_error = document.querySelector("#nick_duplicated_error");
      const possibleNickBox = document.querySelector("#possibleNickBox");

      $.ajax({
          type: "get",
          url: "/user/existsNickname?nickname=" + userNicknameBox.value,
          success: function(res) {
              if (res == true) {
                  nickChecked = false;
                  possibleNickBox.style.display = "none";
                  nicknameError.style.display = "none";
                  nick_duplicated_error.style.display = "block";
                  userNicknameBox.classList.remove("greenFocus");
                  userNicknameBox.classList.add("redFocus");
              } else {
                   if (userNicknameBox.value == "") {
                       nickChecked = false;
                       userNicknameBox.classList.remove("greenFocus");
                       userNicknameBox.classList.add("redFocus");
                       possibleNickBox.style.display = "none";
                       nick_duplicated_error.style.display = "none";
                       nicknameError.style.display = "block";
                    } else {
                      nickChecked = true;
                      userNicknameBox.classList.remove("redFocus");
                      userNicknameBox.classList.add("greenFocus");
                      nick_duplicated_error.style.display = "none";
                      nicknameError.style.display = "none";
                      possibleNickBox.style.display = "block";
                  }
              }

          },
          error: function(err) {
              return;
          }
      });
  }

  document.querySelector("#userNickname").addEventListener("keyup", checkNicknameValue);






  /* 회원정보 수정 */
  const checkValueAndSubmit = () => {
      const nickname = document.querySelector("#userNickname");
      const email = document.querySelector("#userEmail");
      const job = document.querySelector("#jobBox");

      const nicknameError = document.querySelector("#nicknameError");
      const emailError = document.querySelector("#emailError");
      const jobError = document.querySelector("#jobError");

      const nick_duplicated_error = document.querySelector("#nick_duplicated_error");
      const email_duplicated_error = document.querySelector("#email_duplicated_error");

      email.addEventListener("input", () => {
          if (email.value.trim() !== "") {
              emailError.style.display = "none";
          }
      });

      nickname.addEventListener("input", () => {
          if (nickname.value.trim() !== "") {
              nicknameError.style.display = "none";
          }
      });

      job.addEventListener("input", () => {
          if (job.value.trim() !== "") {
              jobError.style.display = "none";
          }
      });

      if (nickChecked == false) {
         nickname.focus();
         nickname.classList.add("redFocus");
         return;
      }

      if (idChecked == false) {
         email.focus();
         email.classList.add("redFocus");
         return;
      }

      if (email.value.trim() == "") {
         emailError.style.display = "block";
         email.focus();
         return;
       } else if (nickname.value.trim() == "") {
         nicknameError.style.display = "block";
         nickname.focus();
         return;
       }  else if (job.value.trim() == "") {
         jobError.style.display = "block";
         job.focus();
         return;
       } else {
           const frm = document.querySelector("#frm");
           frm.submit();
         }
  }



  /* 회원 이미지 변경 모달 열기 */
  const updateUserImgModal = () => {
       const updateUserImgModal = bootstrap.Modal.getOrCreateInstance("#updateUserImgModal");
       updateUserImgModal.show();
  }

  /* 기본 이미지 변경 물어보기 */
  const deleteUserImgModal = () => {
      const updateUserImgModal = bootstrap.Modal.getOrCreateInstance("#updateUserImgModal");
      updateUserImgModal.hide();
      const deleteUserImgModal = bootstrap.Modal.getOrCreateInstance("#deleteUserImgModal");
      deleteUserImgModal.show();
  }

  /* 회원 이미지 수정 유효성 검사 */
  const checkImgValueAndSubmit = () => {
      const image = document.querySelector("#profile_img_link");
      const noImageAlert = document.querySelector("#noImageAlert");
      if (!image || !image.files || image.files.length == 0) {
         noImageAlert.style.display = "block";
      } else {
         const frm = document.querySelector("#imgFrm");
         frm.submit();
      }
  }
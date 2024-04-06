
  /* 회원정보 수정 */
  const checkValueAndSubmit = () => {
      const nickname = document.querySelector("#userNickname");
      const email = document.querySelector("#userEmail");
      const job = document.querySelector("#jobBox");

      const nicknameError = document.querySelector("#nicknameError");
      const emailError = document.querySelector("#emailError");
      const jobError = document.querySelector("#jobError");

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
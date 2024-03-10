
  // 회원정보 수정
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

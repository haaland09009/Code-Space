  // 회원가입 프로세스
   const checkValueAndSubmit = () => {

       const email = document.querySelector("#userEmail");
       const nickname = document.querySelector("#userNickname");
       const job = document.querySelector("#jobBox");
       const password = document.querySelector("#userPassword");
       const passwordCheck = document.querySelector("#userPasswordCheck");

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
       } else {
         const frm = document.querySelector("#frm");
         frm.submit();

         const joinSuccessModal = bootstrap.Modal.getOrCreateInstance("#joinSuccessModal");
         joinSuccessModal.show();

         setTimeout(function() {
            joinSuccessModal.hide();
         }, 5000);

   }
 }

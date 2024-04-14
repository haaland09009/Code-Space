  /* 로그인 enter 키 처리 */
   const checkSendLogin = (event) => {
      if (event.key == "Enter") {
           loginProcess();
         }
   }

   /* 로그인 */
   const loginProcess = () => {
       const email = document.querySelector("#userEmail");
       const password = document.querySelector("#userPassword");

       if (email.value.trim() == "") {
           alert("이메일을 입력하세요.");
           email.focus();
           return;
       }
       if (password.value.trim() == "") {
           alert("비밀번호를 입력하세요.");
           password.focus();
           return;
       }
       $.ajax({
           type: "post",
           url: "/user/loginProcess",
           data: {
               "email" : email.value,
               "password" : password.value
           },
           success: function(res) {
               if (res == "ok") {
    //               location.href = document.referrer;
                  location.href = "/";
               } else {
                   password.value = "";
                   alert("아이디 또는 비밀번호가 일치하지 않습니다.");
                   password.focus();
                   return;
               }
           },
           error: function(err) {
               console.log("실패");
           }
       });
    }

    /* 카카오 로그인 */
    const kakaoLogin = () => {
        const redirect_uri = "http://localhost:8081/kakao/login";
        const kakao_auth_url
            = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + kakaoApiKey +
               "&redirect_uri=" + redirect_uri;
        window.location.href = kakao_auth_url;
    }


    /* 네이버 로그인 */
    const naverLogin = () => {
        const redirect_uri = "http://localhost:8081/naver/login";
        const naver_auth_url
            = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + naverApiKey +
               "&state=STATE_STRING&redirect_uri=" + redirect_uri;
        window.location.href = naver_auth_url;
    }

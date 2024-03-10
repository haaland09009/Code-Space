

    // 로그인 페이지 이동
    const loginPage = () => {
      location.href = "/user/loginPage";
    }

    // 회원가입 페이지 이동
    const joinPage = () => {
      location.href = "/user/joinPage";
    }

    // 로그아웃
    const logout = () => {
        location.href = "/user/logout";
    }

    // 자등으로 높이 조절
    const resize = (obj) => {
      obj.style.height = 'auto'; // 초기화
      obj.style.height = (obj.scrollHeight + 4) + 'px'; // 더 정확한 높이 계산
    }

   const formatDateTime = (dateString) => {

       const date = new Date(dateString);
       const now = new Date();

       const timeDiff = now.getTime() - date.getTime(); // 현재 시간과 작성일과의 차이 (밀리초 단위)
       const minutesDiff = Math.floor(timeDiff / (1000 * 60)); // 분 단위로 변환

       // 1시간 이내일 경우
       if (minutesDiff < 60) {
          // 1분 미만일 경우
           if (minutesDiff < 1) {
             return '방금 전';
           }
           // 1분 이상일 경우
           else {
             return minutesDiff + '분 전';
           }
       }
      // 24시간 이내일 경우
      else if (minutesDiff < 1440) {

           const hoursDiff = Math.floor(minutesDiff / 60);
           return hoursDiff + '시간 전';
      }
   // 30일 이내일 경우
   else if (minutesDiff < 30 * 24 * 60) {
       const daysDiff = Math.floor(minutesDiff / (24 * 60));
       return daysDiff + '일 전';
   }
   // 12개월 이내일 경우
   else if (minutesDiff < 12 * 30 * 24 * 60) {
       const monthsDiff = Math.floor(minutesDiff / (30 * 24 * 60));
       return monthsDiff + '달 전';
   }
     const year = date.getFullYear();
     const month = date.getMonth() + 1;
     const day = date.getDate();
     let hours = date.getHours();
     const minutes = date.getMinutes();
     let period = '오전';

     if (hours >= 12) {
       period = '오후';
       if (hours > 12) {
         hours -= 12;
       }
     } else if (hours === 0) {
       hours = 12;
     }

     const formattedDateTime = year + '-' + ('0' + month).slice(-2) + '-' + ('0' + day).slice(-2) + ' ' + period + ' ' + ('0' + hours).slice(-2) + ':' + ('0' + minutes).slice(-2);
     return formattedDateTime;
   }


    const getNotificationList = () => {
            $.ajax({
             type: "get",
             url: "/notification/findAll/" + sessionId,
             success: function(res) {
                let output = "";
                 if (res == null) {
                    output += '<div class="text-center mt-3">알림이 존재하지 않습니다.</div>';
                } else {
                  for (let i in res) {
                    output += '<a href="' + res[i].notUrl + '">';
                    output += '<div class="row mt-2">';
                    output += '<div class="col ms-3">';

                    output += '<div class="row">';
                    output += '<div class="col-1">';
                    if (res[i].fileAttached == 0) {
                        output += '<img src="/img/user.jpg" class="rounded-circle" style="width: 30px; height: 30px; position: relative; top: 4px">';
                    } else if (res[i].fileAttached == 1) {
                        output += '<img class="rounded-circle" src="/upload/' + res[i].storedFileName + '" style="width: 30px; height: 30px; position: relative; top: 4px">';
                    }
                    output += '</div>';
                    output += '<div class="col ms-2 fw-semibold">' + res[i].sender + '</div>';
                    output += '<div class="col text-end text-secondary me-1">' + formatDateTime(res[i].occurDate) + '</div>';
                    output += '</div>';

                    output += '<div class="row">';
                    output += '<div class="col-1">';
                    output += '</div>';
                    output += '<div class="col ms-2">' + res[i].sentence + '</div>';
                    output += '</div>';

                    output += '<div class="border-bottom mt-2">' + '</div>';

                    output += '</div>';
                    output += '</div>';
                    output += '</a>';
                    }
                }
                document.getElementById('notification-list').innerHTML = output;
             },
             error: function(err) {
                 return;
             }
         });
      }


window.addEventListener("DOMContentLoaded", function(){
    if (sessionId != 0) {
        getNotificationList();
    }
    const dateElements = document.querySelectorAll('.date-element');
        dateElements.forEach(function (element) {
           const dateString = element.textContent;
           const formattedDate = formatDateTime(dateString);
           element.textContent = formattedDate;
       });


});

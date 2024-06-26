

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

    /* 년.월.일 시간으로 변환 */
    const formatDate = (dateString)  => {
       const date = new Date(dateString);
         const year = date.getFullYear();
         const month = String(date.getMonth() + 1).padStart(2, '0');
         const day = String(date.getDate()).padStart(2, '0');
         const hour = String(date.getHours() % 12 || 12).padStart(2, '0'); // 12시간 형식으로 변환하고 0시는 12시로 처리합니다.
         const minute = String(date.getMinutes()).padStart(2, '0');
         const ampm = date.getHours() < 12 ? '오전' : '오후';
         const formattedDate = `${year}-${month}-${day} ${ampm} ${hour}:${minute}`;
         return formattedDate;
    }

    /* 년.월.일로 변환 */
    const formatDateYmd = (dateString)  => {
       const date = new Date(dateString);
         const year = date.getFullYear();
         const month = String(date.getMonth() + 1).padStart(2, '0');
         const day = String(date.getDate()).padStart(2, '0');
         const hour = String(date.getHours() % 12 || 12).padStart(2, '0'); // 12시간 형식으로 변환하고 0시는 12시로 처리합니다.
         const minute = String(date.getMinutes()).padStart(2, '0');
         const ampm = date.getHours() < 12 ? '오전' : '오후';
         const formattedDate = `${year}.${month}.${day}`;
         return formattedDate;
    }

    /* 년.월.일 시간으로 변환 */
    const formatAmPmDate = (dateString)  => {
       const date = new Date(dateString);
         const year = date.getFullYear();
         const month = String(date.getMonth() + 1).padStart(2, '0');
         const day = String(date.getDate()).padStart(2, '0');
         const hour = String(date.getHours() % 12 || 12); // 12시간 형식으로 변환하고 0시는 12시로 처리합니다.
         const minute = String(date.getMinutes()).padStart(2, '0');
         const ampm = date.getHours() < 12 ? '오전' : '오후';
         const formattedDate = `${ampm} ${hour}:${minute}`;
         return formattedDate;
    }

   /* 분 단위로 변환하는 함수 */
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

   /* 어제인지의 여부 확인 */
   const isYesterday = (date, now) => {
       const yesterday = new Date(now);
       yesterday.setDate(now.getDate() - 1);

       return date.getFullYear() == yesterday.getFullYear() &&
              date.getMonth() == yesterday.getMonth() &&
              date.getDate() == yesterday.getDate();
   }

   /* 일 단위로 변환하는 함수 (메시지 전용) */
   const formatChatTime = (dateString) => {
       const date = new Date(dateString);
       const now = new Date();

       const daysDiff = Math.floor((now - date) / (1000 * 60 * 60 * 24));

       if (daysDiff == 0 && !isYesterday(date, now)) {
           return formatHourAndMinute(date);
       } else {
           return formatMonthAndDate(date);
       }
   }

   /* 오전, 오후 구분 함수 */
   const formatHourAndMinute = (date) => {
       let hour = date.getHours();
       const minute = date.getMinutes();
       const period = (hour < 12) ? "오전" : "오후";

       // 0시일 경우, 12로 변경
       if (hour == 0) {
           hour = 12;
       }
       // 오후인 경우 12를 빼서 12시간 형식으로 변경
       else if (hour > 12) {
           hour -= 12;
       }

       return `${period} ${hour}:${minute.toString().padStart(2, '0')}`;
     }

     /* 년,월,일 변환 함수 */
     const formatMonthAndDate = (date) => {
         const year = date.getFullYear();
         const month = date.getMonth() + 1;
         const day = date.getDate();

         const now = new Date();
         const nowYear = now.getFullYear();

         // 현재 연도와 비교
         if (year == nowYear) {
             return `${month}월 ${day}일`;
         } else {
             return `${year}년 ${month}월 ${day}일`;
         }
     }



    /* 알림 조회 */
    const getNotificationList = () => {
            $.ajax({
             type: "get",
             url: "/notification/findAll/" + sessionId + "?notRead='Y'",
             success: function(res) {
                let output = "";
                 if (res.length == 0) {
                    output += '<div class="text-center mt-3 mb-3 fs-5">읽지 않은 알림이 존재하지 않습니다.</div>';
                } else {
                  for (let i in res) {
                    output += '<a href="' + res[i].notUrl + '" onclick="readNotification(' + res[i].id + ')">';
                    output += '<div class="row contentDiv">';
                    output += '<div class="col ms-3 border-bottom">';

                    output += '<div class="row mt-3">';
                    output += '<div class="col-1">';
                    if (res[i].fileAttached == 0) {
                        output += '<img src="/img/user.jpg" class="rounded-circle" style="width: 30px; height: 30px; position: relative; top: 4px">';
                    } else if (res[i].fileAttached == 1) {
                        output += '<img class="rounded-circle" src="/upload/' + res[i].storedFileName + '" style="width: 30px; height: 30px; position: relative; top: 4px">';
                    }
                    output += '</div>';
                    output += '<div class="col ms-1 fw-semibold">' + res[i].nickname + '</div>';
                    output += '<div class="col text-end text-secondary me-1">' + res[i].formattedDate + '</div>';
                    output += '</div>';

                    output += '<div class="row mb-3">';
                    output += '<div class="col-1">';
                    output += '</div>';
                    output += '<div class="col ms-1">' + res[i].content + '</div>';
                    output += '</div>';


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

       /* 알림 읽기 */
       const readNotification = (id) => {
            $.ajax({
               type: "get",
               url: "/notification/asRead/" + id,
               success: function(res) {


               }, error: function(err) {
                   return;
               }
            });
       }

       /*  안 읽은 알림 개수  */
      const notReadNoticeCount = (id) => {
         const notReadNoticeBox = document.querySelector("#notReadNoticeBox");
         const notReadNoticeCount = document.querySelector("#notReadNoticeCount");
         const notReadNoticeCount1 = document.querySelector("#notReadNoticeCount1");
         $.ajax({
           url: "/notification/notReadCount/" + id,
           success: function(res) {
              if (res > 0) {
                 notReadNoticeBox.style.display = "block";
                 notReadNoticeCount.innerText = res;
                 notReadNoticeCount1.innerText = res;
               } else {
                 notReadNoticeBox.style.display = "none";
                 notReadNoticeCount1.innerText = "0";
               }

           }, error: function(err) {
               return;
           }
        });
      }


  // 현재 페이지의 URL에서 path 부분을 추출
    const path = window.location.pathname;

    // 모든 메뉴 항목에서 'active' 클래스 제거
    document.querySelectorAll('.tab-link').forEach(function (element) {
        element.classList.remove('text-black');
    });

    // 현재 페이지에 해당하는 메뉴 항목에 'active' 클래스 추가
     if (path.startsWith('/qna')) {
         document.querySelector('#qnaLink').classList.add('text-black');
     } else if (path.startsWith('/project')) {
         document.querySelector('#projectLink').classList.add('text-black');
     } else if (path.startsWith('/lounge')) {
        document.querySelector('#loungeLink').classList.add('text-black');
    }


    /*  안 읽은 채팅 개수  */
    const notReadChatCount = (id) => {
       const notReadChatBox = document.querySelector("#notReadChatBox");
       const notReadChatCount = document.querySelector("#notReadChatCount");
       $.ajax({
         url: "/chat/notReadCount/" + id,
         success: function(res) {
            if (res > 0) {
                notReadChatBox.style.display = "block";
                notReadChatCount.innerText = res;
             } else {
                notReadChatBox.style.display = "none";
             }

         }, error: function(err) {
             return;
         }
      });
    }

   /* 사용자 정보 모달 클릭 */
  const clickUserInfoModal = (id) => {
    selectedUserInfoId = id;
    getModalUserInfo(id);
  }

   /*  사용자 정보 확인  */
  const getModalUserInfo = (id) => {
     const userModalImg = document.querySelector("#userModalImg");
     const userModalNickname = document.querySelector("#userModalNickname");
     const userModalJob = document.querySelector("#userModalJob");

     const userInfoSpace = document.querySelector(".user-info-space");

     $.ajax({
       url: "/user/getUserInfo/" + id,
       success: function(res) {
          if (res.fileAttached == 0) {
              userModalImg.src = "/img/user.jpg";
          } else if (res.fileAttached == 1) {
              userModalImg.src = "/upload/" + res.storedFileName;
          }
          userModalNickname.innerText = res.nickname;
          userModalJob.innerText = res.jobName;

          if (sessionId == res.id) {
            userInfoSpace.style.display = "none";
          } else {
            userInfoSpace.style.display = "block";
          }

         /* 모달 열기 */
          userInfoModal();
       }, error: function(err) {
           return;
       }
    });
  }

  /* 사용자 정보 모달  */
  const userInfoModal = () => {
        const userInfoModal = bootstrap.Modal.getOrCreateInstance("#userInfoModal");
        userInfoModal.show();
  }





window.addEventListener("DOMContentLoaded", function(){
    if (sessionId != 0) {
        notReadChatCount(sessionId);
        notReadNoticeCount(sessionId);
        getNotificationList();
    }
    const dateElements = document.querySelectorAll('.date-element');
        dateElements.forEach(function (element) {
           const dateString = element.textContent;
           const formattedDate = formatDateTime(dateString);
           element.textContent = formattedDate;
       });


});

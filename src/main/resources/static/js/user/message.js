


    /* 사용자의 채팅방 load */
    const loadChatRoomList = (res) => {
        let output = "";
        for (let i in res) {
            if (selectedRoomId == res[i].roomId) {
                output += '<div class="row selectedContent">';
            } else {
                output += '<div class="row contentDiv">';
            }
            output += '<div class="col" onclick="getChatList('+ res[i].roomId +')">';
            output += '<div class="row mt-3">';
            output += '<div class="col-2">';

            if (res[i].fileAttached == 0) {
                output += '<img src="/img/user.jpg" class="rounded-circle mt-1" style="width: 55px; height: 55px;">';
            } else if (res[i].fileAttached == 1) {
                output += '<img src="/upload/' + res[i].storedFileName + '" class="rounded-circle mt-1" style="width: 55px; height: 55px;">';
            }
            output += '</div>';
            output += '<div class="col px-2">';
            output += '<div class="row">';
            output += '<div class="col">';
            output += '<span class="fw-semibold me-1">' + res[i].nickname +  '</span>';
            output += '<span class="ms-2 text-secondary date-element fs-18">' + formatDateTime(res[i].regDate)  + '</span>';
            output += '</div>';
            if (res[i].isNotReadCount != 0) {
                output += '<div class="col-1 me-3 text-end">';
                output += '<div class="redCircle">';
                output += '<span>' + res[i].isNotReadCount  +'</span>';
                output += '</div>';
                output += '</div>';
            }
            output += '</div>';
            output += '<div class="row mt-1">'
            output += '<div class="col overflow-list fs-18">' + res[i].content + '</div>';
            output += '</div>';
            output += '</div>';
            output += '</div>';

            output += '</div>';
            output += '<div class="border-bottom mt-3">' + '</div>';
            output += '</div>';
        }
        document.querySelector("#chatRoomList").innerHTML = output;
    }


    /* 최근 채팅방 모두 조회 */
    const getRecentChatList = (sessionId) => {
        $.ajax({
         url: "/chat/recentChatList/" + sessionId,
         success: function(res) {
            loadChatRoomList(res);
         }, error: function(err) {
             return;
         }
      });
    }

   /* 안 읽은 채팅 기록이 있는 채팅방만 모두 조회 */
   const getUnreadChatList = (sessionId) => {
        $.ajax({
            url: "/chat/unreadChatList/" + sessionId,
            success: function(res) {
               loadChatRoomList(res);
            }, error: function(err) {
                return;
            }
     });
   }

   /* 안 읽은 메시지 버튼 클릭 */
   const toggleUnreadChatList = (sessionId) => {
       if (sessionId == 0) {
            location.href = "/user/loginPage";
            return;
       }
       const unreadChatButton = document.querySelector(".unreadChatButton");
       if (unreadChatButton.classList.contains("btn-outline-dark")) {
            unreadChatButton.classList.remove("btn-outline-dark");
            unreadChatButton.classList.add("btn-dark");
            /* 안 읽은 채팅 기록이 있는 채팅방 조회 */
            getUnreadChatList(sessionId);
       } else {
            unreadChatButton.classList.remove("btn-dark");
            unreadChatButton.classList.add("btn-outline-dark");
            /* 모든 채팅방 조회*/
            getRecentChatList(sessionId);
       }
   }


    /* 채팅방 화면 이전 */
    const goBeforeChatScreen = () => {
        const chatScreen = document.querySelector("#chatScreen");
        chatScreen.style.display = "none";

        const beforeChatScreen = document.querySelector("#beforeChatScreen");
        beforeChatScreen.style.display = "block";
    }

    /* 채팅방 접속하기 */
    const getChatList = (roomId) => {
        if (sessionId == 0) {
            location.href = "/user/loginPage";
            return;
        }
        const chatScreen = document.querySelector("#chatScreen");
        chatScreen.style.display = "block";

        const beforeChatScreen = document.querySelector("#beforeChatScreen");
        beforeChatScreen.style.display = "none";

        $.ajax({
             type: "get",
             url: "/chat/readChat/" + roomId,
             success: function(res) {
                selectedRoomId = roomId;
                getUserInfo(roomId);
                getRecentChatList(sessionId);
                loadChatList(roomId);
                notReadChatCount(sessionId);

             }, error: function(err) {
                return;
             }
          });
       }


    /* 채팅 상대방 정보 조회 */
    const getUserInfo = (roomId) => {
        const nameCol = document.querySelector("#nameCol");
        const jobCol = document.querySelector("#jobCol");
        const imgCol = document.querySelector("#imgCol");
        $.ajax({
         type: "get",
         url: "/chat/getUserInfo/" + roomId,
         success: function(res) {
            nameCol.innerText = res.nickname;
            jobCol.innerText = res.jobName;
            if (res.fileAttached == 0) {
                const imgElement = '<img src="/img/user.jpg" class="rounded-circle" style="width: 55px; height: 55px;">';
                imgCol.innerHTML = imgElement;
            } else if (res.fileAttached == 1) {
                const imgElement = '<img src="/upload/' + res.storedFileName + '" class="rounded-circle" style="width: 55px; height: 55px;">';
                imgCol.innerHTML = imgElement;
            }
         }, error: function(err) {
             return;
         }
      });
    }


    /* 채팅방에 있는 채팅 기록 조회 */
    const loadChatList = (roomId) => {
        const chatBox = document.querySelector("#chatList");
        $.ajax({
         type: "get",
         url: "/chat/list/" + roomId,
         success: function(res) {
            const shouldAutoScroll = isScrolledToBottom(chatBox);
            let output = "";
            let yearMonthDay = null;
            for (let i in res) {
                 const entireRegDate = new Date(res[i].regDate);
                 const year = entireRegDate.getFullYear();
                 const month = entireRegDate.getMonth() + 1;
                 const day = entireRegDate.getDate();
                 const formattedDateHappen = year + '년 ' + month + '월 ' + day + '일';

                 if (yearMonthDay != formattedDateHappen) {
                   output += '<div class="row mt-3 mb-3 text-center">';
                   output += '<div class="col fs-18">' + formattedDateHappen + '</div>';
                   output += '</div>';
                   yearMonthDay = formattedDateHappen;
                }

                /* 상대방이 보낸 메시지일 경우 */
                if (sessionId != res[i].senderId) {
                    output += '<div class="row mt-2">';
                    output += '<div class="col-6 text-start">';
                    output += '<button class="btn bg-gray py-2 rounded rounded-4 text-start text-break fs-18 yourChatButton">' + res[i].content + '</button>';
                    output += '</div>';
                    output += '</div>';
                    output += '<div class="row mt-1">';
                    output += '<div class="col ms-2 date-element text-secondary chatDate">' + formatAmPmDate(res[i].regDate) + '</div>';
                    output += '</div>';
                } else if (sessionId == res[i].senderId) {
                    output += '<div class="row mt-2">';
                    output += '<div class="col">' + '</div>';
                    output += '<div class="col justify-content-end text-end">';
                    output += '<button class="btn btn-outline-dark py-2 rounded rounded-4 text-start fs-18 myChatButton" onclick="checkReadYn(' + res[i].id + ')">' + res[i].content + '</button>';
                    output += '</div>';
                    output += '</div>';
                    output += '<div class="row">';
                    output += '<div class="col text-end">';
                    if (res[i].readDate == null) {
                        output += '<span class="me-1 fs-15 unRead_ChatFont">' + "1" + '</span>';
                    }
                    output += '<span class="date-element ms-1 text-secondary chatDate">' + formatAmPmDate(res[i].regDate) + '</span>';
                    output += '</div>';
                    output += '</div>';
                }
            }
            document.querySelector("#chatList").innerHTML = output;

            if (shouldAutoScroll) {
                chatBox.scrollTop = chatBox.scrollHeight;
            }

            /*
            // 3초마다 채팅 업로드
                if(intervalHandler != null){
                    clearInterval(intervalHandler);
                    intervalHandler = null;
                }

                intervalHandler = setInterval(() => {
                    loadChatList(roomId);
                }, 1000);
                */

         }, error: function(err) {
             return;
         }
      });
    }






   /* 채팅 전송 enter 키 처리 */
   const checkSendMessage = (event) => {
      if (event.key === "Enter") {
           sendMessage();
         }
    }

    /* 채팅방이 이미 있는 상황에서 메시지 전송하기 */
    const sendMessage = () => {
        if (sessionId == 0) {
            location.href = "/user/loginPage";
            return;
        }
        const chatContent = document.querySelector("#chatContent");
        if (chatContent.value.trim() == "") {
            alert("내용을 입력하세요.");
            return;
        }
        $.ajax({
             type: "post",
             url: "/chat/sendMessage",
             data: {
               "content" : chatContent.value,
               "roomId" : selectedRoomId
             },
             success: function(res) {
                if (res == "ok") {
                    chatContent.value = "";
                    chatContent.focus();
                    loadChatList(selectedRoomId);
                    getRecentChatList(sessionId);
                } else {
                    return;
                }
             }, error: function(err) {
                 return;
             }
          });
    }

    /* 메시지 삭제 이전 읽음 여부 확인 */
    const checkReadYn = (id) => {
        /* id 설정 */
        selectedChatId = id;

        $.ajax({
            type: "get",
            url: "/chat/checkReadYn/" + selectedChatId,
            success: function(res) {
               if (res) {
                   const noDeleteChatModal = bootstrap.Modal.getOrCreateInstance("#noDeleteChatModal");
                   noDeleteChatModal.show();
                   setTimeout(function() {
                       noDeleteChatModal.hide();
                   }, 4000);
               } else {
                   /* 상대방이 읽지 않았다면 삭제 모달 띄우기 */
                   const deleteChatModal = bootstrap.Modal.getOrCreateInstance("#deleteChatModal");
                   deleteChatModal.show();
               }
            },
            error: function(err) {
                return;
            }
        });
    }


    /* 메시지 삭제 */
    const deleteChat = () => {
        $.ajax({
             type: "get",
             url: "/chat/delete/" + selectedChatId,
             success: function(res) {
                loadChatList(selectedRoomId);
                getRecentChatList(sessionId);
                selectedChatId = null;
                const deleteChatModal = bootstrap.Modal.getOrCreateInstance("#deleteChatModal");
                deleteChatModal.hide();

             }, error: function(err) {
                 return;
             }
          });
        }



    /* 스크롤이 맨 아래에 있는지 확인하는 함수 */
    const isScrolledToBottom = (element) => {
        return element.scrollHeight - element.clientHeight <= element.scrollTop + 1;
    }

    /* 사용자가 스크롤을 조작할 때 자동 스크롤을 비활성화하는 이벤트 핸들러 */
    document.querySelector("#chatList").addEventListener("scroll", function (event) {
      const chatListBox = event.target;
      const shouldAutoScroll = isScrolledToBottom(chatListBox);

      /* 스크롤이 맨 아래에 있을 경우에만 자동 스크롤 활성화 */
      if (shouldAutoScroll) {
        chatListBox.scrollTop = chatListBox.scrollHeight;
      }
    });

   window.addEventListener("DOMContentLoaded", function(){
       if (selectedRoomId == 0) {
            const beforeChatScreen = document.querySelector("#beforeChatScreen");
            beforeChatScreen.style.display = "block";
        }

  });

 /* 글자수 감지 1 */
 const checkTextareaLength = (textarea) => {
    const maxLength = 500;
    if (textarea.value.length > maxLength) {
     textarea.value = textarea.value.substring(0, maxLength); // 500자를 넘어가면 초과된 부분을 잘라냅니다.
    }
    const writeButton = document.querySelector('#writeButton');
    if (textarea.value.length >= 10) {
        writeButton.disabled = false;
    } else {
        writeButton.disabled = true;
    }
 }

 /* 글자수 감지 2 */
 const updateTextLength = (textarea) => {
     const textLength = document.querySelector('#textLength');
     textLength.textContent = textarea.value.length;
 }



 /* 라운지 게시글 작성 */
 const writeLounge = () => {
    const content = document.querySelector("#loungeContent");
    if (sessionId == 0) {
        location.href = "/user/loginPage";
        return;
    }

    if (content.value.trim() == "") {
        alert("글자를 입력해주세요.");
        return;
    }

    $.ajax({
         type: "post",
         url: "/lounge/save",
         data: {
             "content": content.value,
             "userId": sessionId
         },
         success: function(res) {
            loadLoungeList();
            content.value = "";
            updateTextLength(content); // 글자수 감지 함수 호출하여 글자 수 초기
            checkTextareaLength(content);
         },
         error: function(err) {
             return;
         }
      });
 }

 /* 라운지 게시글 목록 조회 */
 const loadLoungeList = () => {
      $.ajax({
           type: "get",
           url: "/lounge/list",
           success: function(res) {
               let output = "";
               for (let i in res) {
                    output += '<div class="row mt-4 mb-2" id="board_' + res[i].id + '">';
                    output += '<div class="col ms-1 border rounded rounded-1 px-4">';
                    output += '<div class="row mt-4">';
                    output += '<div class="col-auto ms-1 mt-1">';
                    if (res[i].fileAttached == 0) {
                        output += '<img src="/img/user.jpg" class="rounded-circle" style="width: 70px; height: 70px;">';
                    } else if (res[i].fileAttached == 1) {
                        output += '<img src="/upload/' + res[i].storedFileName + '" class="rounded-circle" style="width: 70px; height: 70px;">'
                    }
                    output += '</div>';
                    output += '<div class="col px-2">';
                    output += '<div class="row">';
                    output += '<div class="col fw-semibold">' + res[i].writer + '</div>';
                    output += '</div>';
                    output += '<div class="row">';
                    output += '<div class="col fs-18 text-secondary">' + res[i].jobName + '</div>';
                    output += '</div>';
                    output += '<div class="row">';
                    output += '<div class="col fs-18 text-secondary">' + res[i].formattedDate + '</div>';
                    output += '</div>';
                    output += '</div>';
                    if (sessionId != 0 && sessionId == res[i].userId) {
                        output += '<div class="col text-end dropdown fs-5" style="cursor: pointer">';
                        output += '<i class="bi bi-three-dots-vertical" id="dropButton" data-bs-toggle="dropdown" aria-expanded="false">' + '</i>';
                        output += '<ul class="dropdown-menu" aria-labelledby="dropButton">';
                        output += '<li>';
                        output += '<a class="dropdown-item fs-5" onclick="deleteLoungePage(' + res[i].id + ')">';
                        output += '<i class="bi bi-trash3">' + '</i>';
                        output += '<span class="ms-2">' +  "삭제하기" + '</span>';
                        output += '</a>';
                        output += '</li>';
                        output += '</ul>';
                        output += '</div>';
                    }
                    output += '</div>';
                    output += '<div class="mt-4">' + '</div>';
                    output += '<div class="row">';
                    output += '<div class="col px-4 boardContent">' + res[i].content + '</div>';
                    output += '</div>';
                    output += '<div class="mt-5">' + '</div>';
                    output += '<div class="row mt-2">';

                    if (res[i].likeYn == 0) {
                        output += '<div class="col ms-2 text-secondary" id="boardLike_' + res[i].id + '" onclick="isYourBoardForLike(' + res[i].id + ')" style="cursor: pointer;">';
                        output += '<img src="/img/thumbs-up.png" style="width: 30px; height: 30px;">';
                        output += '<span class="ms-2">' + "좋아요";
                        output += '<span class="ms-1" id="boardLikeCount_' + res[i].id + '">' + res[i].likeCount + '</span>';
                        output += '</span>';
                        output += '</div>';
                    } else if (res[i].likeYn == 1) {
                        output += '<div class="col ms-2 text-secondary" id="boardLike_' + res[i].id + '"  onclick="isYourBoardForLike(' + res[i].id + ')" style="cursor: pointer;">';
                        output += '<img src="/img/thumbs-up-fill.png" style="width: 30px; height: 30px;">';
                        output += '<span class="ms-2 text-black">' + "좋아요";
                        output += '<span class="ms-1" id="boardLikeCount_' + res[i].id + '">' + res[i].likeCount + '</span>';
                        output += '</span>';
                        output += '</div>';
                    }

                    output += '<div class="col text-end">';
                    /*output += '<i class="bi bi-bookmark me-1">' + '</i>';*/
                    output += '<i class="ms-3 bi bi-share fs-4" onclick="clip(' + res[i].id +')">' + '</i>';
                    output += '</div>';
                    output += '</div>';
                    output += '<div class="mt-3">' + '</div>';
                    output += '</div>';
                    output += '</div>';
               }
               document.getElementById('lounge-list').innerHTML = output;
            },
            error: function(err) {
                 return;
            }
        });
    }

  /* 게시글 삭제 모달 */
  const deleteLoungePage = (id) => {
        selectedLoungeId = id;
        const deleteLoungeModal = bootstrap.Modal.getOrCreateInstance("#deleteLoungeModal");
        deleteLoungeModal.show();
  }

  /* 게시글 삭제 */
  const deleteLounge = () => {
      $.ajax({
           type: "post",
           url: "/lounge/delete/" + selectedLoungeId,
           success: function(res) {
              selectedCommentId = null;
              const deleteLoungeModal = bootstrap.Modal.getOrCreateInstance("#deleteLoungeModal");
              deleteLoungeModal.hide();
              loadLoungeList();
           },
           error: function(err) {
               return;
           }
        });

  }



 /* 채팅하기 모달 */
 const sendMessageModal = () => {
    if (sessionId == 0) {
        location.href = "/user/loginPage";
        return;
    }
    const sendMessageModal = bootstrap.Modal.getOrCreateInstance("#sendMessageModal");
    sendMessageModal.show();
 }


    /* 메시지 전송 */
    const sendMessage = () => {

        if (sessionId == 0) {
            location.href = "/user/loginPage";
            return;
        }
        const content = document.querySelector("#messageContent");
        const alertBox = document.querySelector("#alertMessageBox");

        content.addEventListener("input", () => {
            if (content.value.trim() !== "") {
                alertBox.style.display = "none"; // 내용이 입력되면 alertBox를 숨깁니다.
            }
        });

        if (content.value.trim() == "") {
            alertBox.style.display = "block";
            content.focus();
            return;
        }

        $.ajax({
           type: "post",
           url: "/chat/save",
           data: {
               "content" : content.value,
               "receiverId" : selectedUserInfoId
           },
            success: function(res) {
                if (res == "ok") {
                    // 성공시

                     content.value = "";

                    const sendSuccessModal = bootstrap.Modal.getOrCreateInstance("#sendSuccessModal");
                    sendSuccessModal.show();

                     setTimeout(function() {
                        sendSuccessModal.hide();
                    }, 2000);

                    const sendMessageModal = bootstrap.Modal.getOrCreateInstance("#sendMessageModal");
                    sendMessageModal.hide();
               } else {
                  // 코드 추가
                   return;
               }
           },
           error: function(err) {
               console.log("실패");
           }
       });
    }

        // 메시지 모달 창을 닫을 때 이벤트 처리
        const sendMessageModal1 = bootstrap.Modal.getOrCreateInstance("#sendMessageModal");
        sendMessageModal1.hide(); // 모달을 닫기 전에 값 초기화
        sendMessageModal1._element.addEventListener("hidden.bs.modal", function () {
            resetModalInputs(); // 모달이 숨겨진 후에 초기화 함수 호출
        });

        const resetModalInputs = () => {
            const alertBox = document.querySelector("#alertMessageBox");
            const content = document.querySelector("#messageContent");
            alertBox.style.display = "none";
            content.value = "";
        }

 /* 주소 복사 */
 const clip = (id) => {
    var url = '';
    var textarea = document.createElement("textarea");
    document.body.appendChild(textarea);
    url = window.location.href;
    const boardIndex = url.indexOf("#");
    if (boardIndex != -1) {
        url = url.substring(0, boardIndex); // # 이전의 문자열만 가져옵니다.
    }
    textarea.value = url + "#board_" + id;
    textarea.select();
    document.execCommand("copy");
    document.body.removeChild(textarea);


    // 요소를 생성합니다.
       var alertDiv = document.createElement("div");
       alertDiv.style.position = "fixed";
       alertDiv.style.top = "70px";
       alertDiv.style.left = "50%";
       alertDiv.style.transform = "translate(-50%, -50%)";
        alertDiv.style.animation = "fadeInAndOut 2s ease-in";
       alertDiv.innerHTML = `
           <div class="rounded-4 p-3 text-center" style="background-color: #334155; width: 280px;">
               <span class="alertCircle me-1">
                   <i class="ms-1 bi bi-check-lg fw-bold"></i>
               </span>
               <span class="ms-2 text-white">주소가 복사되었습니다.</span>
           </div>
       `;
       document.body.appendChild(alertDiv);

       // 2초 후에 요소를 제거합니다.
       setTimeout(() => {
           document.body.removeChild(alertDiv);
       }, 2000);
 };

 /* 본인이 작성한 게시글인지의 여부 확인 (좋어요) */
 const isYourBoardForLike = (id) => {
      if (sessionId == 0) {
          location.href = "/user/loginPage";
          return;
      }
      $.ajax({
        type: "get",
        url: "/lounge/isYourBoard",
        data: {
           "id": id,
           "userId": sessionId
        },
        success: function(res) {
           if (res) {
               /* 반환값 true */
               alertNoLikeModal();
               return;
           } else {
               /* 반환값 false */
               toggleBoardLike(id);
           }
        },
        error: function(err) {
            return;
        }
    });
 }



  /* 본인 게시글에 좋아요 클릭 시 */
  const alertNoLikeModal = () => {
     const alertNoLikeModal = bootstrap.Modal.getOrCreateInstance("#alertNoLikeModal");
     alertNoLikeModal.show();

     setTimeout(function() {
         alertNoLikeModal.hide();
         }, 2000);
     }


  /* 게시글 좋아요 */
  const toggleBoardLike = (id) => {
     const boardLikeElement = document.querySelector('#boardLike_' + id);
     const spanElement = boardLikeElement.querySelector('span');
     const imgElement = boardLikeElement.querySelector('img');
     $.ajax({
       type: "post",
       url: "/lounge/like",
       data: {
          "loungeId": id,
          "userId": sessionId
       },
       success: function(res) {
          if (res) {
             imgElement.src = "/img/thumbs-up-fill.png";
             spanElement.classList.add('text-black');
          } else {
             imgElement.src = "/img/thumbs-up.png";
             spanElement.classList.remove('text-black');
          }
          updateBoardLikeCount(id);
       },
       error: function(err) {
           return;
       }
   });
 }

 /* 댓글 좋아요 수 업데이트 */
 const updateBoardLikeCount = (id) => {
     const boardLikeBox = document.querySelector("#boardLikeCount_" + id);
     $.ajax({
       type: "get",
       url: "/lounge/likeCount/" + id,
       success: function(res) {
          boardLikeBox.innerText = res;
       },
       error: function(err) {
           return;
       }
   });
 }




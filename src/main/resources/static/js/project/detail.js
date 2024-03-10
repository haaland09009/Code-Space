// 게시글 삭제 모달
 const deleteBoardReq = () => {
     const deleteModal = bootstrap.Modal.getOrCreateInstance("#deleteModal");
     deleteModal.show();
 }

 // 게시글 삭제
 const deleteBoard = () => {
     const deleteModal = bootstrap.Modal.getOrCreateInstance("#deleteModal");
     deleteModal.hide();
     location.href = "/project/delete/" + projectPk;
 }

 // 본인 댓글 좋아요/싫어요 클릭 오류 모달
const alertNoLikeModal = () => {
    const alertNoLikeModal = bootstrap.Modal.getOrCreateInstance("#alertNoLikeModal");
    alertNoLikeModal.show();

    setTimeout(function() {
        alertNoLikeModal.hide();
    }, 2000);
}



 // 본인 댓글 좋아요/싫어요 클릭 오류 모달
const alertLikeCheckModal = () => {
    const alertLikeCheckModal = bootstrap.Modal.getOrCreateInstance("#alertLikeCheckModal");
    alertLikeCheckModal.show();

    setTimeout(function() {
        alertLikeCheckModal.hide();
    }, 2000);
}

  // 댓글 작성
 const commentReq = () => {
     const projectId = projectPk;
     const content = document.querySelector("#commentContent");

     if (content.value.trim() == "") {
         alert("내용을 입력하세요.");
         content.focus();
         return;
     }

     $.ajax({
         type: "post",
         url: "/projectComment/save",
         data: {
             "projectId" : projectPk,
             "content" : content.value
         },
         success: function(res) {
             loadComments(projectId);
             updateCommentCount(projectId);
             content.value = "";
         }, error: function(err) {
             return;
         }
      });
 }

 // 날짜 변환
 const formatTime = (timeStamp) => {
   const date = new Date(timeStamp);
   const year = date.getFullYear();
   const month = String(date.getMonth() + 1).padStart(2, '0');
   const day = String(date.getDate()).padStart(2, '0');
   const hours = String(date.getHours()).padStart(2, '0');
   const minutes = String(date.getMinutes()).padStart(2, '0');
   const seconds = String(date.getSeconds()).padStart(2, '0');

   const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
   return formattedDate;
 }

 // 댓글 수 업데이트 함수
 const updateCommentCount = (id) => {
     $.ajax({
         type: "get",
         url: "/projectComment/count/" + id,
         success: function(res) {

             const commentCount = res; // 서버에서 받아온 댓글 수
             document.querySelector("#commentCount").innerText = commentCount; // 댓글 수 업데이트
         },
         error: function(err) {
             return;
         }
     });
 }


    // 댓글 조회
    const loadComments = (projectId) => {
         $.ajax({
         type: "get",
         url: "/projectComment/getCommentList/" + projectId,
         success: function(comments) {
             let output = "";
             for (let i in comments) {
                 output += '<div class="row mb-2">';
                 output += '<div class="col ms-1">';
                 output += '<div class="row">';
                 output += '<div class="col">';
                 output += '<div class="row">';
                 output += '<div class="col-2">';

                 if (comments[i].fileAttached == 0) {
                     output += '<img class="rounded-circle ms-2" style="width: 55px; height: 55px;" src="/img/user.jpg">';
                 } else if (comments[i].fileAttached == 1) {
                     output += '<img class="rounded-circle ms-2" style="width: 55px; height: 55px;" src="/upload/' + comments[i].storedFileName + '">';
                 }

                 output += '</div>';
                 output += '<div class="col">';
                 output += '<div class="row">';
                 output += '<div class="col fw-semibold px-0 fs-5">' + comments[i].writer + '</div>';
                 output += '</div>';
                 output += '<div class="row">';
                 output += '<div class="col px-0 text-secondary fs-18">';
                 output += '<span>'  + comments[i].jobName + '</span>';
                 output += '<span class="ms-1">' + "·" + '</span>';
                 output += '<span class="date-element ms-1">' +  formatDateTime(comments[i].regDate) + '</span>';
                 output += '</div>';
                 output += '</div>';
                 output += '</div>';
                 output += '</div>';
                 output += '</div>';
                 output += '<div class="col text-end">';

                 output += '<span>';
                 output += '<img src="/img/like.png" style="width: 30px; height: 30px; cursor: pointer;" class="me-1" ' +
                      'onclick="isYourCommentForLike(' + comments[i].id + ')" ' +
                      'id="cmtLike_' + comments[i].id + '">';

                 output += '<span class="me-1">' + comments[i].likeCount + '</span>';
                 output += '</span>';


                 output += '<span class="ms-2">';
                 output += '<img src="/img/dislike.png" style="width: 30px; height: 30px; cursor: pointer;" class="me-1" ' +
                      'onclick="isYourCommentForDisLike(' + comments[i].id + ')" ' +
                      'id="cmtDisLike_' + comments[i].id + '">';
                 output += '<span class="me-1">' + comments[i].disLikeCount + '</span>';
                 output += '</span>';

                 if (sessionId == comments[i].userId) {
                 output += '<span class="text-end ps-0 dropdown" style="cursor: pointer;" id="dropdownComment_' + comments[i].id + '">';
                 output += '<i class="bi bi-three-dots-vertical" style="height: 10px" id="dropButtonComment" data-bs-toggle="dropdown" aria-expanded="false"></i>';
                 output += '<ul class="dropdown-menu" aria-labelledby="dropButtonComment">';
                 output += '<li>';
                 output += '<a class="dropdown-item" href="#">';
                 output += '<i class="bi bi-pencil-square"></i>';
                 output += '<span class="ms-2">' + "수정하기" + '</span>';
                 output += '</a>';
                 output += '</li>';
                 output += '<li>';
                 output += '<a class="dropdown-item" onclick="deleteCommentPage(' + comments[i].id + ')">';
                 output += '<i class="bi bi-trash3"></i>';
                 output += '<span class="ms-2">' +  "삭제하기" + '</span>';

                 output += '</a>';
                 output += '</li>';
                 output += '</ul>';
                 output += '</span>';
                 }

                 output += '</div>';
                 output += '</div>';
                 output += '<div class="row mt-3">';
                 output += '<div class="col ms-1 boardContent">' + comments[i].content + '</div>';
                 output += '</div>';

                 <!-- 답글 작성 -->

                 output += '<div class="row mt-2 mb-2">' + '</div>';

                 <!-- 댓글 사이 경계선 -->
                 output += '<div class="border-bottom mt-1 mb-3"></div>';
                 <!-- 댓글 사이 경계선 -->
                 output += '</div>';
                 output += '</div>';
             }
             document.getElementById('comment-list').innerHTML = output;

         },
         error: function(err) {
             return;
         }
     });
    }




    // 댓글 삭제 모달
    const deleteCommentPage = (id) => {
        selectedCommentId = id;
        const deleteCommentModal = bootstrap.Modal.getOrCreateInstance("#deleteCommentModal");
	    deleteCommentModal.show();
    }

    // 댓글 삭제
    const deleteComment = () => {
        const id = selectedCommentId;
        const projectId = projectPk;

        $.ajax({
         type: "post",
         url: "/projectComment/delete/" + id,
         success: function(res) {
            selectedCommentId = null;
          	const deleteCommentModal = bootstrap.Modal.getOrCreateInstance("#deleteCommentModal");
			deleteCommentModal.hide();
            loadComments(projectId);
            updateCommentCount(projectId);
         },
         error: function(err) {
             return;
         }
     });

    }



  // 본인이 작성한 댓글인지의 여부 확인 (좋아요)
  const isYourCommentForLike = (id) => {
       if (sessionId == 0) {
           location.href = "/user/loginPage";
           return;
       }

       $.ajax({
         type: "get",
         url: "/projectComment/isYourComment",
         data: {
            "id": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res) {
            // 반환값 true
                alertNoLikeModal();
                return;
            } else {
                // 반환값 false
                checkCommentDisLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
  }

  // 댓글 좋아요를 눌렀을 때 싫어요 여부 확인
   const checkCommentDisLike = (id) => {
         $.ajax({
         type: "get",
         url: "/projectComment/checkCommentDisLike",
         data: {
            "id": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res > 0) {
            // 반환값 true
                alertLikeCheckModal();
                return;
            } else {
                // 반환값 false
                toggleCommentLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
   }


    // 댓글 좋아요
  const toggleCommentLike = (id) => {

       $.ajax({
         type: "post",
         url: "/projectComment/commentLike",
         data: {
            "commentId": id,
            "userId": sessionId
         },
         success: function(res) {
             updateCommentLikeCount(id);
         },
         error: function(err) {
             return;
         }
     });
  }

  // 댓글 좋아요 수 업데이트
  const updateCommentLikeCount = (id) => {
       const cmtLikeBox = document.getElementById("cmtLike_" + id);
       $.ajax({
         type: "get",
         url: "/projectComment/likeCount/" + id,
         success: function(res) {
            cmtLikeBox.innerText = res;
         },
         error: function(err) {
             return;
         }
     });
  }


   // 본인이 작성한 댓글인지의 여부 확인 (싫어요)
  const isYourCommentForDisLike = (id) => {
       if (sessionId == 0) {
           location.href = "/user/loginPage";
           return;
       }

       $.ajax({
         type: "get",
         url: "/projectComment/isYourComment",
         data: {
            "id": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res) {
            // 반환값 true
                alertNoLikeModal();
                return;
            } else {
                // 반환값 false
                checkCommentLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
  }

   // 댓글 싫어요를 눌렀을 때 좋아요 여부 확인
   const checkCommentLike = (id) => {
         $.ajax({
         type: "get",
         url: "/projectComment/checkCommentLike",
         data: {
            "id": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res > 0) {
            // 반환값 true
                alertLikeCheckModal();
                return;
            } else {
                // 반환값 false
                toggleCommentDisLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
   }

  // 댓글 싫어요
  const toggleCommentDisLike = (id) => {

       $.ajax({
         type: "post",
         url: "/projectComment/commentDisLike",
         data: {
            "commentId": id,
            "userId": sessionId
         },
         success: function(res) {
             updateCommentDisLikeCount(id);
         },
         error: function(err) {
             return;
         }
     });
  }

// 댓글 싫어요 수 업데이트
  const updateCommentDisLikeCount = (id) => {
       const cmtDisLikeBox = document.getElementById("cmtDisLike_" + id);
       $.ajax({
         type: "get",
         url: "/projectComment/disLikeCount/" + id,
         success: function(res) {
            cmtDisLikeBox.innerText = res;
         },
         error: function(err) {
             return;
         }
     });
  }


    // 채팅하기 모달
    const sendMessageModal = () => {
        const sendMessageModal = bootstrap.Modal.getOrCreateInstance("#sendMessageModal");
	    sendMessageModal.show();
    }



    // 메시지 전송
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
               "receiverId" : projectUserId
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


    // 게시물 스크랩 여부 확인
    const checkProjectClip = (id) => {
       const clipBox = document.querySelector(".clipBox");
       $.ajax({
         type: "get",
         url: "/project/checkClipYn",
         data: {
            "projectId": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res) {
               clipBox.classList.remove("bi-bookmark");
               clipBox.classList.add("bi-bookmark-fill");

            } else {
               clipBox.classList.remove("bi-bookmark-fill");
               clipBox.classList.add("bi-bookmark");
            }
         },
         error: function(err) {
             return;
         }
     });
  }


    // 게시물 스크랩
    const toggleProjectClip = (id) => {
        if (sessionId == 0) {
            location.href = "/user/loginPage";
            return;
        }
        $.ajax({
             type: "post",
             url: "/project/clip",
             data: {
                "projectId": id,
                "userId": sessionId
             },
             success: function(res) {
               checkProjectClip(id);
             },
             error: function(err) {
                 return;
             }
         });
    }
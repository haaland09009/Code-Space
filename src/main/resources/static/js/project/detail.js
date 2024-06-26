 /* 게시글 삭제 모달 */
 const deleteBoardReq = () => {
    $.ajax({
      type: "get",
      url: "/project/checkCommentCount/" + projectPk,
      success: function(res) {
         if (res > 0)  {
             const noDeleteModal = bootstrap.Modal.getOrCreateInstance("#noDeleteModal");
             noDeleteModal.show();
             setTimeout(function() {
                 noDeleteModal.hide();
             }, 4000);
         } else {
             /* 작성된 댓글이 없다면 삭제 모달 띄우기 */
              const deleteModal = bootstrap.Modal.getOrCreateInstance("#deleteModal");
              deleteModal.show();
         }
      },
      error: function(err) {
          return;
      }
  });
 }



 /* 게시글 삭제 */
 const deleteBoard = () => {
     const deleteModal = bootstrap.Modal.getOrCreateInstance("#deleteModal");
     deleteModal.hide();
     location.href = "/project/delete/" + projectPk;
 }

 /* 본인 댓글에 좋아요 / 싫어요 클릭 시 */
 const alertNoLikeModal = () => {
    const alertNoLikeModal = bootstrap.Modal.getOrCreateInstance("#alertNoLikeModal");
    alertNoLikeModal.show();

    setTimeout(function() {
        alertNoLikeModal.hide();
        }, 2000);
    }



 /* 본인 댓글 좋아요/싫어요 클릭 오류 모달 */
 const alertLikeCheckModal = () => {
    const alertLikeCheckModal = bootstrap.Modal.getOrCreateInstance("#alertLikeCheckModal");
    alertLikeCheckModal.show();

    setTimeout(function() {
        alertLikeCheckModal.hide();
    }, 2000);
 }

 /* 댓글 작성 */
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

 /* 날짜 변환 */
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

 /* 댓글 수 업데이트 */
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


 /* 댓글 목록 조회 */
 const loadComments = (projectId) => {
     $.ajax({
         type: "get",
         url: "/projectComment/getCommentList/" + projectId,
         success: function(comments) {
             let output = "";
             for (let i in comments) {
                 output += '<div class="row mb-2" id="comment_' + comments[i].id + '">';
                 output += '<div class="col ms-1">';
                 output += '<div class="row">';
                 output += '<div class="col">';
                 output += '<div class="row">';
                 output += '<div class="col-auto user-info-image" id="commentUserImage_' + comments[i].id + '" onclick="clickUserInfoModal(' + comments[i].userId + ')">';

                 if (comments[i].fileAttached == 0) {
                     output += '<img class="rounded-circle ms-2" style="width: 55px; height: 55px;" src="/img/user.jpg">';
                 } else if (comments[i].fileAttached == 1) {
                     output += '<img class="rounded-circle ms-2" style="width: 55px; height: 55px;" src="/upload/' + comments[i].storedFileName + '">';
                 }
                 output += '</div>';

                 if (sessionId != 0 && sessionId == comments[i].userId) {
                    output += '<div class="col ps-2" id="commentUpdateForm_' + comments[i].id + '" style="display: none;">';
                    output += '<div class="row">';
                    output += '<div class="col me-3">';
                    output += '<textarea name="" id="commentUpdateContent_' + comments[i].id + '" cols="10" rows="3" class="form-control fs-5"  style="resize:none;" placeholder="댓글을 작성하여 프로젝트와 스터디에 참여해보세요 !" onkeydown="resize(this)" onkeyup="resize(this)">' + comments[i].content +  '</textarea>';
                    output += '</div>';
                    output += '</div>';

                    output += '<div class="row mt-3">';
                    output += '<div class="col text-danger" style="display: none;" id="noContentAlert_' + comments[i].id + '">';
                    output += '<i class="bi bi-exclamation-circle">' + '</i>';
                    output += '<span class="ms-2">' + "내용을 최소 1자 이상 입력해주세요." + '</span>';
                    output += '</div>';
                    output += '<div class="col text-end me-3">';
                    output += '<button class="btn btn-outline-dark fs-18 rounded-1" onclick="toggleUpdateCommentPage(' + comments[i].id + ')">' + "취소" + '</button>';
                    output += '<button class="btn mainButton ms-2 fs-18 rounded-1" onclick="updateComment(' + comments[i].id + ')">' + "댓글 수정하기" + '</button>';
                    output += '</div>';
                    output += '</div>';
                    output += '</div>';
                 }
                 output += '<div class="col ps-2" id="commentUserInfo_' + comments[i].id + '">';
                 output += '<div class="row">';
                 output += '<div class="col-auto fw-semibold fs-5">' + comments[i].writer + '</div>';
                 output += '</div>';
                 output += '<div class="row">';
                 output += '<div class="col-auto text-secondary fs-18">';
                 output += '<span>'  + comments[i].jobName + '</span>';
                 output += '<span class="ms-1">' + "·" + '</span>';
                 output += '<span class="date-element ms-1">' +  formatDateTime(comments[i].regDate) + '</span>';
                 if (comments[i].updDate != null) {
                    output += '<span style="position: relative; white-space: nowrap;">';
                    output += '<span class="ms-2 updatedFont updated-project">' + "수정됨" + '</span>';
                    output += '<span class="updated-text" style="position: absolute; left: 12px">' + "수정일시 : ";
                    output += '<span>' + formatDate(comments[i].updDate) + '</span>'
                    output += '</span>';
                    output += '</span>';
                 }
                 output += '</div>';
                 output += '</div>';
                 output += '</div>';
                 output += '</div>';
                 output += '</div>';
                 output += '<div class="col text-end" id="commentUserLikeInfo_' + comments[i].id + '">';
                 output += '<span id="commentLike_' + comments[i].id + '">';
                 if (comments[i].likeYn == 0) {
                    output += '<img src="/img/thumbs-up.png" style="width: 30px; height: 30px; cursor: pointer;" class="me-1" onclick="isYourCommentForLike(' + comments[i].id + ')">';
                 } else if (comments[i].likeYn == 1) {
                    output += '<img src="/img/thumbs-up-fill.png" style="width: 30px; height: 30px; cursor: pointer;" class="me-1" onclick="isYourCommentForLike(' + comments[i].id + ')">';
                 }
                 output += '<span class="me-1" id="cmtLike_' + comments[i].id + '">' + comments[i].likeCount + '</span>';
                 output += '</span>';
                 output += '<span id="commentDisLike_' + comments[i].id + '" class="ms-2">';
                 if (comments[i].disLikeYn == 0) {
                    output += '<img src="/img/thumbs-down.png" style="width: 30px; height: 30px; cursor: pointer;" class="me-1" onclick="isYourCommentForDisLike(' + comments[i].id + ')" >';
                 } else if (comments[i].disLikeYn == 1) {
                    output += '<img src="/img/thumbs-down-fill.png" style="width: 30px; height: 30px; cursor: pointer;" class="me-1" onclick="isYourCommentForDisLike(' + comments[i].id + ')" >';
                 }
                 output += '<span class="me-1" id="cmtDisLike_' + comments[i].id + '">' + comments[i].disLikeCount + '</span>';
                 output += '</span>';

                 if (sessionId == comments[i].userId) {
                     output += '<span class="text-end ps-0 dropdown" style="cursor: pointer;" id="dropdownComment_' + comments[i].id + '">';
                     output += '<i class="bi bi-three-dots-vertical" style="height: 10px" id="dropButtonComment" data-bs-toggle="dropdown" aria-expanded="false"></i>';
                     output += '<ul class="dropdown-menu" aria-labelledby="dropButtonComment">';
                     output += '<li>';
                     output += '<a class="dropdown-item fs-5" onclick="toggleUpdateCommentPage(' + comments[i].id + ')">';
                     output += '<i class="bi bi-pencil-square"></i>';
                     output += '<span class="ms-2">' + "수정하기" + '</span>';
                     output += '</a>';
                     output += '</li>';
                     output += '<li>';
                     output += '<a class="dropdown-item fs-5" onclick="deleteCommentPage(' + comments[i].id + ')">';
                     output += '<i class="bi bi-trash3"></i>';
                     output += '<span class="ms-2">' +  "삭제하기" + '</span>';

                     output += '</a>';
                     output += '</li>';
                     output += '</ul>';
                     output += '</span>';
                 }
                 output += '</div>';
                 output += '</div>';
                 output += '<div class="row mt-3" id="commentContent_' + comments[i].id + '">';
                 output += '<div class="col ms-2">';
                 output += '<span class="boardContent" id="commentSpan_' + comments[i].id + '">' + comments[i].content + '</span>';
                 output += '</div>';
                 output += '</div>';
                 output += '<div class="row mt-2 mb-2">' + '</div>';
                 output += '<div class="border-bottom mt-1 mb-3"></div>';
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




 /* 댓글 삭제 모달 */
 const deleteCommentPage = (id) => {
    selectedCommentId = id;
    const deleteCommentModal = bootstrap.Modal.getOrCreateInstance("#deleteCommentModal");
    deleteCommentModal.show();
 }

 /* 댓글 삭제 */
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

  /* 본인이 작성한 댓글인지의 여부 확인 (좋아요) */
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
                    /* 반환값 true */
                    alertNoLikeModal();
                    return;
                } else {
                    /* 반환값 false */
                    checkCommentDisLike(id);
                }
             },
             error: function(err) {
                 return;
             }
         });
      }

   /* 댓글 좋아요를 눌렀을 때 싫어요 여부 확인 */
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
                /* 반환값 true */
                alertLikeCheckModal();
                return;
            } else {
                /* 반환값 false */
                toggleCommentLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
   }


  /* 댓글 좋아요 */
  const toggleCommentLike = (id) => {
       const commentLikeElement = document.querySelector('#commentLike_' + id);
       const imgElement = commentLikeElement.querySelector('img');
       $.ajax({
         type: "post",
         url: "/projectComment/commentLike",
         data: {
            "commentId": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res) {
               imgElement.src = "/img/thumbs-up-fill.png";
            } else {
               imgElement.src = "/img/thumbs-up.png"
            }
            updateCommentLikeCount(id);
         },
         error: function(err) {
             return;
         }
     });
  }

  /* 댓글 좋아요 수 업데이트 */
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


  /* 본인이 작성한 댓글인지의 여부 확인 (싫어요) */
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
                /* 반환값 true */
                alertNoLikeModal();
                return;
            } else {
                /* 반환값 false */
                checkCommentLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
  }

   /* 댓글 싫어요를 눌렀을 때 좋아요 여부 확인 */
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
                alertLikeCheckModal();
                return;
            } else {
                toggleCommentDisLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
   }

  /* 댓글 싫어요 */
  const toggleCommentDisLike = (id) => {
       const commentDisLikeElement = document.querySelector('#commentDisLike_' + id);
       const imgElement = commentDisLikeElement.querySelector('img');
       $.ajax({
         type: "post",
         url: "/projectComment/commentDisLike",
         data: {
            "commentId": id,
            "userId": sessionId
         },
         success: function(res) {
              if (res) {
                  imgElement.src = "/img/thumbs-down-fill.png";
              } else {
                  imgElement.src = "/img/thumbs-down.png"
              }
             updateCommentDisLikeCount(id);
         },
         error: function(err) {
             return;
         }
     });
  }

  /* 댓글 싫어요 수 업데이트*/
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


    /* 채팅하기 모달 */
    const sendMessageToWriterModal = () => {
       if (sessionId == 0) {
            location.href = "/user/loginPage";
            return;
        }
        const sendMessageToWriterModal = bootstrap.Modal.getOrCreateInstance("#sendMessageToWriterModal");
	    sendMessageToWriterModal.show();
    }

    /* 메시지 모달 창을 닫을 때 이벤트 처리 */
    const sendMessageModalToWriter = bootstrap.Modal.getOrCreateInstance("#sendMessageToWriterModal");
    sendMessageModalToWriter.hide(); // 모달을 닫기 전에 값 초기화
    sendMessageModalToWriter._element.addEventListener("hidden.bs.modal", function () {
        resetModalWriterInputs(); // 모달이 숨겨진 후에 초기화 함수 호출
    });

    const resetModalWriterInputs = () => {
        const alertBox = document.querySelector("#alertMessageToWriterBox");
        const content = document.querySelector("#messageToWriterContent");
        alertBox.style.display = "none";
        content.value = "";
    }


    /* 게시글 작성자에게 메시지 전송하기 */
    const sendMessageToWriter = () => {

        if (sessionId == 0) {
            location.href = "/user/loginPage";
            return;
        }
        const content = document.querySelector("#messageToWriterContent");
        const alertBox = document.querySelector("#alertMessageToWriterBox");

        /* 내용이 입력되면 경고창 숨기기 */
        content.addEventListener("input", () => {
            if (content.value.trim() !== "") {
                alertBox.style.display = "none";
            }
        });

        /* 내용을 입력하지 않으면 경고창 표시 */
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
                    /* 전송 성공 */
                    content.value = "";

                    const sendSuccessModal = bootstrap.Modal.getOrCreateInstance("#sendSuccessModal");
	                sendSuccessModal.show();

                     setTimeout(function() {
                        sendSuccessModal.hide();
                    }, 2000);

                    const sendMessageModal = bootstrap.Modal.getOrCreateInstance("#sendMessageModal");
                    sendMessageModal.hide();

               } else {
                  /* 만약 메시지를 보냈을 때 회원 탈퇴된 경우를 생각해야함 */
                   return;
               }
           },
           error: function(err) {
               console.log("실패");
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
                    /* 성공시 */
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

    /* 메시지 모달 창을 닫을 때 이벤트 처리 */
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

    /* 게시물 스크랩 여부 확인 */
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


    /* 게시물 스크랩 */
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

    /* 댓글 수정 모달 열기 */
    const toggleUpdateCommentPage = (id) => {
        const commentUserImage = document.querySelector("#commentUserImage_" + id);
        const commentUserInfo = document.querySelector("#commentUserInfo_" + id);
        const commentUserLikeInfo = document.querySelector("#commentUserLikeInfo_" + id);
        const commentContent = document.querySelector("#commentContent_" + id);

        const commentUpdateForm = document.querySelector("#commentUpdateForm_" + id);
        const commentUpdateContent = document.querySelector("#commentUpdateContent_" + id);

       /* 모달 열고 닫을때마다 경고창 초기화 */
        const noContentAlert = document.querySelector("#noContentAlert_" + id);
        noContentAlert.style.display = "none";

        if (commentUpdateForm.style.display == 'none') {
            commentUserImage.classList.remove("col-2");
            commentUserImage.classList.add("col-1");

            commentUserInfo.style.display = "none";
            commentUserLikeInfo.style.display = "none";
            commentContent.style.display = "none";
            commentUpdateForm.style.display = "block";

            /* 내용의 끝에 커서 설정 */
            commentUpdateContent.selectionStart = commentUpdateContent.value.length;
            commentUpdateContent.selectionEnd = commentUpdateContent.value.length;
            commentUpdateContent.focus();
        } else {
            commentUserImage.classList.remove("col-1");
            commentUserImage.classList.add("col-2");

            commentUserInfo.style.display = "block";
            commentUserLikeInfo.style.display = "block";
            commentContent.style.display = "block";
            commentUpdateForm.style.display = "none";
        }
    }

   /* 댓글 수정하기 */
   const updateComment = (id) => {
       const commentUpdateContent = document.querySelector("#commentUpdateContent_" + id);
       const noContentAlert = document.querySelector("#noContentAlert_" + id);

       if (sessionId == 0) {
            location.href = "/user/loginPage";
            return;
       }

       commentUpdateContent.addEventListener("input", () => {
           if (commentUpdateContent.value.trim() !== "") {
               noContentAlert.style.display = "none"; // 내용이 입력되면 alertBox를 숨깁니다.
           }
       });

       if (commentUpdateContent.value.trim() == "") {
            noContentAlert.style.display = "block";
            commentUpdateContent.value = "";
            commentUpdateContent.focus();
            return;
       }
       $.ajax({
            type: "post",
            url: "/projectComment/update",
            data: {
                "id" : id,
                "content" : commentUpdateContent.value
            },
            success: function(res) {
                /* 댓글 목록 다시 조회 */
                loadComments(projectPk);
            }, error: function(err) {
                return;
            }
         });
   }


   /* 댓글 위치 스크롤 이동 */
   window.onload = function() {
       /* URL에서 댓글 위치를 가져온다. (예: http://localhost:8081/project/149#comment_342) */
       const url = window.location.href;
       const commentIndex = url.indexOf("#");
       if (commentIndex != -1) { // URL에 #이 포함되어 있는 경우
          const location = url.substring(commentIndex + 1); // # 이후의 문자열을 가져온다.

          /* 댓글 위치로 스크롤 이동 */
          const commentElement = document.getElementById(location);
          if (commentElement) {
              commentElement.scrollIntoView({ behavior: "smooth", block: "center" }); // 댓글 위치로 스크롤 이동
          }
          /* comment_342에서 "_"를 기준으로 분리하여 두 번째 요소인 342를 가져온다.*/
           const commentId = location.split("_")[1];
          /* 효과를 주고 싶은 요소에 highlight 클래스 추가 */
          const commentSpan = document.querySelector("#commentSpan_" + commentId);
          if (commentSpan) {
              commentSpan.classList.add("highlight");
          }
          /* 2초 후에 highlight 클래스 제거하고 fade-out 클래스 추가 */
          setTimeout(function() {
                commentSpan.classList.remove("highlight");
            }, 4000);
      }
   };
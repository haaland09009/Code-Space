   // 게시글 삭제 모달
   const deleteQnaReq = () => {
     const deleteModal = bootstrap.Modal.getOrCreateInstance("#deleteQnaModal");
     deleteModal.show();
   }

     // 게시글 삭제
   const deleteQna = () => {
     const deleteModal = bootstrap.Modal.getOrCreateInstance("#deleteQnaModal");
     deleteModal.hide();
     location.href = "/qna/delete/" + boardId;
  }


  // 답변 작성
  const replyReq = () => {
    const qnaId = boardId;
    const content = document.querySelector("#replyContent");
    const userId = sessionId;

    if (content.value.trim() == "") {
         alert("내용을 입력하세요.");
         content.focus();
         return;
     }
       $.ajax({
         type: "post",
         url: "/qnaReply/save",
         data: {
             "qnaId" : qnaId,
             "userId" : userId,
             "content" : content.value
         },
         success: function(res) {
             loadReplies(qnaId);
             updateReplyCount(qnaId);
             content.value = "";
         }, error: function(err) {
             return;
         }
      });
  }

  // 답변 load
  const loadReplies = (qnaId) => {
    $.ajax({
     type: "get",
     url: "/qnaReply/getReplyList/" + qnaId,
     success: function(replies) {
         let output = "";
         if (replies.length == 0) {
            output += '<div class="row mt-4">';
            output += '<div class="col text-center">';
            output += '<div class="row mt-3">';
            output += '<div class="col">';
            output += '<img src="/img/comment.png">';
            output += '</div>';
            output += '</div>';
            output += '<div class="row">';
            output += '<div class="col text-secondary">' + "아직 작성된 답변이 없습니다." + '</div>';
            output += '</div>';
            output += '</div>';
            output += '</div>';
         } else {
          for (let i in replies) {
            output += '<div class="row mt-4 mb-5">';
            output += '<div class="col ms-3 border border-1">';

            output += '<div class="row mt-4 ms-1">';

            output += '<div class="col-1 mt-2">';
            if (replies[i].fileAttached == 0) {
                output += '<img class="rounded-circle" style="width: 65px; height: 65px;" src="/img/user.jpg">';
            } else if (replies[i].fileAttached == 1) {
                output += '<img class="rounded-circle" style="width: 65px; height: 65px;" src="/upload/' + replies[i].storedFileName + '">';
            }
            output += '</div>';

            output += '<div class="col ps-1">';
            output += '<div class="row">';
            output += '<div class="col fw-semibold fs-22">' + replies[i].writer + '</div>';
            output += '</div>';
            output += '<div class="row">';
            output += '<div class="col text-secondary fs-18">' + replies[i].jobName + '</div>';
            output += '</div>';

            output += '<div class="row">';
            output += '<div class="col text-secondary fs-18">';
            output += '<span class="date-element">' + formatDateTime(replies[i].regDate) + '</span>';
            output += '</div>';
            output += '</div>';
            output += '</div>';

            if (sessionId != 0 && sessionId == replies[i].userId) {
                output += '<div class="col text-end mt-1 ps-0 dropdown" style="cursor: pointer;">';
                output += '<i class="bi bi-three-dots-vertical" style="height: 10px" id="dropReplyButton" data-bs-toggle="dropdown" aria-expanded="false">' + '</i>';
                output += '<ul class="dropdown-menu" aria-labelledby="dropReplyButton">';
                output += '<li>';
                output += '<a class="dropdown-item">';
                output += '<i class="bi bi-pencil-square">' + '</i>';
                output += '<span class="ms-2">' + "수정하기" + '</span>';
                output += '</a>';
                output += '</li>';
                output += '<li>';
                output += '<a class="dropdown-item"  onclick="deleteReplyPage(' + replies[i].id + ')">';
                output += '<i class="bi bi-trash3">' + '</i>';
                output += '<span class="ms-2">' + "삭제하기" + '</span>';
                output += '</a>';
                output += '</li>';
                output += '</ul>';
                output += '</div>';
            }
            output += '</div>';

            output += '<div class="row mt-4 ms-2">';
            output += '<div class="col-11 boardContent pe-0">' + replies[i].content +  '</div>';
            output += '</div>';

            output += '<div class="row mt-4 ms-2 text-secondary fw-semibold">';
            output += '<div class="col fs-18">';

            if (replies[i].isLike == 0) {
                output += '<span style="cursor: pointer;" onclick="isYourReplyForLike(' + replies[i].id + ')" id="replyLikeSpan_' + replies[i].id + '">';
            } else {
                output += '<span style="cursor: pointer;" onclick="isYourReplyForLike(' + replies[i].id + ')" class="text-black" id="replyLikeSpan_' + replies[i].id + '">';
            }
            output += '<img src="/img/like.png" style="width: 23px; height: 23px; cursor: pointer;">';
            output += '<span class="ms-1">' + "좋아요" + '</span>';
            output += '<span class="ms-1 me-1" id="replyLike_' + replies[i].id + '">' + replies[i].likeCount + '</span>';
            output += '</span>';

            output += '<span style="cursor: pointer;" onclick="toggleReplyInputBox(' + replies[i].id + ')">';
            output += '<i class="bi bi-chat-left-dots ms-3 me-1" style="position:relative; top:2px">' + '</i>';
            output += '<span class="ms-1">' + "댓글" + '</span>';
            output += '<span class="ms-1" id="replyComment_' + replies[i].id + '">' + replies[i].commentCount + '</span>';
            output += '</span>';
            output += '</div>';
            output += '</div>';

            <!-- 댓글 입력 폼 -->
            output += '<div class="row mt-3" style="display: none;" id="toggleReplyInputBox_' + replies[i].id  + '">';
            output += '<div class="col-11 ms-4 border border-1" style="position: relative; left: 7px">';
            output += '<div class="row">';
            output += '<div class="col">';
            if (sessionId != 0) {
                output += '<textarea name=""  cols="10" rows="3" id="commentContent_' + replies[i].id  + '" class="form-control fs-18 form-control-no-border ps-0" style="resize:none;" placeholder="여러분의 소중한 댓글을 남겨주세요." onkeydown="resize(this)" onkeyup="resize(this)">' + '</textarea>';
            } else if (sessionId == 0) {
                output += '<textarea name=""  cols="10" rows="3" id="" class="form-control fs-18 disabled form-control-no-border ps-0" style="resize:none; pointer-events: none;" placeholder="로그인 후 댓글을 작성하실 수 있습니다." onkeydown="resize(this)" onkeyup="resize(this)">' + '</textarea>';
            }
            output += '</div>';
            output += '</div>';
            output += '<div class="row mb-2">';
            output += '<div class="col text-end">';
            if (sessionId != 0) {
                output += '<button class="btn mainButton text-secondary fs-18" onclick="writeComment(' + replies[i].id + ')" style="background-color: #e9ecef;">' +  "등록" +'</button>';
            } else if (sessionId == 0) {
                output += '<button class="btn mainButton disabled text-secondary fs-18" onclick="" style="background-color: #e9ecef;">' +  "등록" +'</button>';
            }
            output += '</div>';
            output += '</div>';
            output += '</div>';
            output += '</div>';
            <!-- 댓글 입력 폼 -->

            output += '<div id="comment-list_' + replies[i].id + '">';
            // 여기 사이에 코드 넣어야함
            for (let c in replies[i].commentList) {
                output += '<div class="row mt-4 border-top">';
                output += '<div class="col ms-5">';
                output += '<div class="row mt-3">';
                output += '<div class="col-1">';
                if (replies[i].commentList[c].fileAttached == 0) {
                    output += '<img src="/img/user.jpg" class="rounded-circle" style="width: 55px; height: 55px; position: relative; top: 2px;">';
                } else {
                    output += '<img class="rounded-circle" style="width: 55px; height: 55px;" src="/upload/' + replies[i].commentList[c].storedFileName + '">';
                }


                output += '</div>';
                output += '<div class="col px-0">';
                output += '<div class="row">';
                output += '<div class="col fw-semibold">' + replies[i].commentList[c].writer + '</div>';
                output += '</div>';
                output += '<div class="row">';
                output += '<div class="col text-secondary fs-16">';
                output += '<span>' + replies[i].commentList[c].jobName + '</span>';
                output += '<span class="ms-1">' + "·" + '</span>';
                output += '<span class="date-element ms-1">' +  formatDateTime(replies[i].commentList[c].regDate) + '</span>';
                output += '</div>';
                output += '</div>';
                output += '</div>';


                if (sessionId == replies[i].commentList[c].userId) {
                    output += '<div class="col-2 text-end dropdown" style="cursor: pointer;">';
                    output += '<i class="bi bi-three-dots-vertical" style="height: 10px" id="dropCommentButton" data-bs-toggle="dropdown" aria-expanded="false">' + '</i>';
                    output += '<ul class="dropdown-menu" aria-labelledby="dropCommentButton">';
                    output += '<li>';
                    output += '<a class="dropdown-item">';
                    output += '<i class="bi bi-pencil-square">' + '</i>';
                    output += '<span class="ms-2">' +  "수정하기" + '</span>';
                    output += '</a>';
                    output += '</li>';
                    output += '<li>';
                    output += '<a class="dropdown-item" onclick="deleteCommentPage(' + replies[i].commentList[c].id + ')">';
                    output += '<i class="bi bi-trash3">' + '</i>';
                    output += '<span class="ms-2">' +  "삭제하기" + '</span>';
                    output += '</a>';
                    output += '</li>';
                    output += '</ul>';
                    output += '</div>';
                }
                output += '</div>';
                output += '<div class="row mt-3">';
                output += '<div class="col-11 ms-1 boardContent">' + replies[i].commentList[c].content + '</div>';
                output += '</div>';
                output += '</div>';
                output += '</div>';
            }
            output += '</div>';
            // 여기 사이에 코드 넣어야함


            <!-- 댓글 사이 경계선 -->
            output += '<div class="row mt-3 mb-1">' + '</div>';
            <!-- 댓글 사이 경계선 -->

            output += '</div>';
            output += '</div>';
            }
        }
        document.getElementById('reply-list').innerHTML = output;
     },
     error: function(err) {
         return;
     }
     });
  }

  // 답글 수 업데이트 함수
 const updateReplyCount = (id) => {
     $.ajax({
         type: "get",
         url: "/qnaReply/count/" + id,
         success: function(res) {

             const replyCount = res; // 서버에서 받아온 답글 수
             document.querySelector("#replyCount").innerText = replyCount; // 답글 수 업데이트
         },
         error: function(err) {
             return;
         }
     });
 }


    // 답변 삭제 모달
    const deleteReplyPage = (id) => {
        selectedReplyId = id;
        const deleteReplyModal = bootstrap.Modal.getOrCreateInstance("#deleteReplyModal");
	    deleteReplyModal.show();
    }

    // 답변 삭제
    const deleteReply = () => {
        const id = selectedReplyId;
        const qnaId = boardId;

        $.ajax({
         type: "post",
         url: "/qnaReply/delete/" + id,
         success: function(res) {
            selectedReplyId = null;
          	const deleteReplyModal = bootstrap.Modal.getOrCreateInstance("#deleteReplyModal");
			deleteReplyModal.hide();
            loadReplies(qnaId);
            updateReplyCount(qnaId);
         },
         error: function(err) {
             return;
         }
     });

    }


 // 본인 게시글 좋아요/싫어요 클릭 오류 모달
    const alertQnaNoLikeModal = () => {
        const alertQnaNoLikeModal = bootstrap.Modal.getOrCreateInstance("#alertQnaNoLikeModal");
        alertQnaNoLikeModal.show();

        setTimeout(function() {
            alertQnaNoLikeModal.hide();
        }, 2000);
    }

     //  좋아요/싫어요 클릭 오류 모달
    const alertQnaLikeCheckModal = () => {
        const alertQnaLikeCheckModal = bootstrap.Modal.getOrCreateInstance("#alertQnaLikeCheckModal");
        alertQnaLikeCheckModal.show();

        setTimeout(function() {
            alertQnaLikeCheckModal.hide();
        }, 2000);
    }



  // 본인이 작성한 게시글인지의 여부 확인 (좋아요)
  const isYourQnaForLike = (id) => {
       if (sessionId == 0) {
           location.href = "/user/loginPage";
           return;
       }
       $.ajax({
         type: "get",
         url: "/qna/isYourQna",
         data: {
            "id": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res) {
            // 반환값 true
                alertQnaNoLikeModal();
                return;
            } else {
                // 반환값 false
                checkQnaDisLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
  }


    // 게시글 좋아요를 눌렀을 때 싫어요 여부 확인
   const checkQnaDisLike = (id) => {
         $.ajax({
         type: "get",
         url: "/qna/checkQnaDisLike",
         data: {
            "id": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res > 0) {
            // 반환값 true
                alertQnaLikeCheckModal();
                return;
            } else {
                // 반환값 false
                toggleQnaLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
   }

    // 게시글 좋아요
  const toggleQnaLike = (id) => {
       $.ajax({
         type: "post",
         url: "/qna/like",
         data: {
            "qnaId": id,
            "userId": sessionId
         },
         success: function(res) {
             updateQnaLikeCount(id);
             // 함수 추가
             checkQnaLikeForColor(id);
         },
         error: function(err) {
             return;
         }
     });
  }


  // 게시글 좋아요 수 업데이트
  const updateQnaLikeCount = (id) => {
       const qnaLikeBox = document.getElementById("qnaLike_" + id);
       $.ajax({
         type: "get",
         url: "/qna/likeCount/" + id,
         success: function(res) {
            qnaLikeBox.innerText = res;
         },
         error: function(err) {
             return;
         }
     });
  }


   // 본인이 작성한 게시글인지의 여부 확인 (싫어요)
  const isYourQnaForDisLike = (id) => {
       if (sessionId == 0) {
           location.href = "/user/loginPage";
           return;
       }
       $.ajax({
         type: "get",
         url: "/qna/isYourQna",
         data: {
            "id": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res) {
            // 반환값 true
                alertQnaNoLikeModal();
                return;
            } else {
                // 반환값 false
                checkQnaLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
  }

   // 게시글 싫어요를 눌렀을 때 좋아요 여부 확인
   const checkQnaLike = (id) => {
         $.ajax({
         type: "get",
         url: "/qna/checkQnaLike",
         data: {
            "id": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res > 0) {
            // 반환값 true
                alertQnaLikeCheckModal();
                return;
            } else {
                // 반환값 false
                toggleQnaDisLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
   }

  // 게시글 싫어요 클릭
  const toggleQnaDisLike = (id) => {

       $.ajax({
         type: "post",
         url: "/qna/dislike",
         data: {
            "qnaId": id,
            "userId": sessionId
         },
         success: function(res) {
             updateQnaDisLikeCount(id);
             checkQnaDisLikeForColor(id);
         },
         error: function(err) {
             return;
         }
     });
  }

// 게시글 싫어요 수 업데이트
  const updateQnaDisLikeCount = (id) => {
       const qnaDisLikeBox = document.getElementById("qnaDisLike_" + id);
       $.ajax({
         type: "get",
         url: "/qna/disLikeCount/" + id,
         success: function(res) {
            qnaDisLikeBox.innerText = res;
         },
         error: function(err) {
             return;
         }
     });
  }

  // 사용자가 게시글에 좋아요를 눌렀는지 확인 (색깔 변경 목적)
  const checkQnaLikeForColor = (id) => {
    $.ajax({
         type: "get",
         url: "/qna/checkQnaLikeForColor/" + id,
         success: function(res) {
            if (res) {
                $(".likeStatus").addClass("text-black");
            } else {
                $(".likeStatus").removeClass("text-black");
            }
         },
         error: function(err) {
             return;
         }
     });
  }


  // 사용자가 게시글에 싫어요를 눌렀는지 확인 (색깔 변경 목적)
  const checkQnaDisLikeForColor = (id) => {

    $.ajax({
         type: "get",
         url: "/qna/checkQnaDisLikeForColor/" + id,
         success: function(res) {
            if (res) {
                $(".disLikeStatus").addClass("text-black");
            } else {
                $(".disLikeStatus").removeClass("text-black");
            }
         },
         error: function(err) {
             return;
         }
     });
  }

    // 본인 작성 답변 좋아요 x
    const alertReplyNoLikeModal = () => {
        const alertReplyNoLikeModal = bootstrap.Modal.getOrCreateInstance("#alertReplyNoLikeModal");
        alertReplyNoLikeModal.show();

        setTimeout(function() {
            alertReplyNoLikeModal.hide();
        }, 2000);
    }

    // 본인이 작성한 답변인지의 여부 확인 (좋아요)
    const isYourReplyForLike = (id) => {
       if (sessionId == 0) {
           location.href = "/user/loginPage";
           return;
       }

       $.ajax({
         type: "get",
         url: "/qnaReply/isYourReply",
         data: {
            "id": id,
            "userId": sessionId
         },
         success: function(res) {
            if (res) {
            // 반환값 true
                alertReplyNoLikeModal();
                return;
            } else {
                // 반환값 false
                toggleReplyLike(id);
            }
         },
         error: function(err) {
             return;
         }
     });
  }

  // 답변 좋아요
  const toggleReplyLike = (id) => {

       $.ajax({
         type: "post",
         url: "/qnaReply/like",
         data: {
            "replyId": id,
            "userId": sessionId
         },
         success: function(res) {
             updateReplyLikeCount(id);
             checkReplyLikeForColor(id);
         },
         error: function(err) {
             return;
         }
     });
  }

  // 답변 좋아요 수 업데이트
  const updateReplyLikeCount = (id) => {
       const replyLikeBox = document.getElementById("replyLike_" + id);
       $.ajax({
         type: "get",
         url: "/qnaReply/likeCount/" + id,
         success: function(res) {
            replyLikeBox.innerText = res;
         },
         error: function(err) {
             return;
         }
     });
  }


 // 사용자가 답변에 좋아요를 눌렀는지 확인 (색깔 변경 목적)
  const checkReplyLikeForColor = (id) => {
    const replyLikeSpan = document.getElementById("replyLikeSpan_" + id);
    $.ajax({
         type: "get",
         url: "/qnaReply/checkReplyLikeForColor/" + id,
         success: function(res) {
            if (res) {
                replyLikeSpan.classList.add("text-black");
            } else {
-                replyLikeSpan.classList.remove("text-black");
            }
         },
         error: function(err) {
             return;
         }
     });
  }


   // 답글 작성 폼 열고 닫기
 const toggleReplyInputBox = (id) => {
    const replyBox = document.getElementById("toggleReplyInputBox_" + id);
    const replyButton = document.getElementById("toggleReplyButton_" + id);
    if (replyBox.style.display == 'none') {
        replyBox.style.display = 'block';
    } else {
       replyBox.style.display = 'none';
    }
 }


    // 댓글 작성
    const writeComment = (id) => {
        const content = document.querySelector("#commentContent_" + id);

        if (sessionId == 0) {
            location.href = "/user/loginPage";
            return;
        }

        if (content.value.trim() == "") {
            alert("내용을 입력하세요.");
            content.focus();
            return;
        }
        $.ajax({
             type: "post",
             url: "/qnaReplyComment/save",
             data: {
                 "replyId" : id,
                 "userId" : sessionId,
                 "content" : content.value
             },
             success: function(res) {
                // 댓글 load 함수
                loadComments(id);
                // 댓글 수 업데이트 함수
                updateCommentCount(id);
                 content.value = "";
             }, error: function(err) {
                 return;
             }
          });
    }

    // 답변의 댓글 load
    const loadComments = (id) => {
      $.ajax({
         type: "get",
         url: "/qnaReplyComment/getCommentList/" + id,
         success: function(comments) {
          let output = "";
          for (let i in comments) {
            output += '<div class="row mt-4 border-top">';
            output += '<div class="col ms-5">';
            output += '<div class="row mt-3">';
            output += '<div class="col-1">';
            if (comments[i].fileAttached == 0) {
                output += '<img src="/img/user.jpg" class="rounded-circle" style="width: 55px; height: 55px; position: relative; top: 2px;">';
            } else {
                output += '<img class="rounded-circle" style="width: 55px; height: 55px;" src="/upload/' + comments[i].storedFileName + '">';
            }

            output += '</div>';
            output += '<div class="col px-0">';
            output += '<div class="row">';
            output += '<div class="col fw-semibold">' + comments[i].writer + '</div>';
            output += '</div>';
            output += '<div class="row">';
            output += '<div class="col text-secondary fs-16">';
            output += '<span>' + comments[i].jobName + '</span>';
            output += '<span class="ms-1">' + "·" + '</span>';
            output += '<span class="date-element ms-1">' +  formatDateTime(comments[i].regDate) + '</span>';
            output += '</div>';
            output += '</div>';
            output += '</div>';
            if (sessionId == comments[i].userId) {
                output += '<div class="col-2 text-end dropdown" style="cursor: pointer;">';
                output += '<i class="bi bi-three-dots-vertical" style="height: 10px" id="dropCommentButton" data-bs-toggle="dropdown" aria-expanded="false">' + '</i>';
                output += '<ul class="dropdown-menu" aria-labelledby="dropCommentButton">';
                output += '<li>';
                output += '<a class="dropdown-item">';
                output += '<i class="bi bi-pencil-square">' + '</i>';
                output += '<span class="ms-2">' +  "수정하기" + '</span>';
                output += '</a>';
                output += '</li>';
                output += '<li>';
                output += '<a class="dropdown-item" onclick="deleteCommentPage(' + comments[i].id + ')">';
                output += '<i class="bi bi-trash3">' + '</i>';
                output += '<span class="ms-2">' +  "삭제하기" + '</span>';
                output += '</a>';
                output += '</li>';
                output += '</ul>';
                output += '</div>';
            }
            output += '</div>';
            output += '<div class="row mt-3">';
            output += '<div class="col-11 ms-1 boardContent">' + comments[i].content + '</div>';
            output += '</div>';
            output += '</div>';
            output += '</div>';
          }
          document.getElementById("comment-list_" + id).innerHTML = output;
         },
         error: function(err) {
             return;
         }
      });
    }




    // 댓글 삭제 모달
    const deleteCommentPage = (id) => {
        selectedCommentId = id;
        getReplyPk(selectedCommentId);

        const deleteCommentModal = bootstrap.Modal.getOrCreateInstance("#deleteCommentModal");
	    deleteCommentModal.show();

    }

   // 답변 pk 조회
    const getReplyPk = (id) => {
         $.ajax({
         type: "get",
         url: "/qnaReplyComment/getReplyPk/" + id,
         success: function(res) {
            selectedReplyIdForComment = res;
         },
         error: function(err) {
             return;
         }
     });
    }

     // 댓글 삭제
    const deleteComment = () => {

        $.ajax({
         type: "post",
         url: "/qnaReplyComment/delete/" + selectedCommentId,
         success: function(res) {

          	const deleteCommentModal = bootstrap.Modal.getOrCreateInstance("#deleteCommentModal");
			deleteCommentModal.hide();

            updateCommentCount(selectedReplyIdForComment);
            loadComments(selectedReplyIdForComment);


            selectedCommentId = null;
            selectedReplyIdForComment = null;

         },
         error: function(err) {
             return;
         }
     });
    }

    // 답변에 달린 댓글 수
    const updateCommentCount = (id) => {
        const commentCount = document.querySelector("#replyComment_" + id);
        $.ajax({
         type: "get",
         url: "/qnaReplyComment/count/" + id,
         success: function(res) {
            commentCount.innerText = res;
         },
         error: function(err) {
             return;
         }
     });
    }


    // 게시물 스크랩 여부 확인
    const checkQnaClip = (id) => {
       const clipBox = document.querySelector(".clipBox");
       $.ajax({
         type: "get",
         url: "/qna/checkClipYn",
         data: {
            "qnaId": id,
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
    const toggleQnaClip = (id) => {
        $.ajax({
             type: "post",
             url: "/qna/clip",
             data: {
                "qnaId": id,
                "userId": sessionId
             },
             success: function(res) {
               checkQnaClip(id);
             },
             error: function(err) {
                 return;
             }
         });
    }

window.addEventListener("DOMContentLoaded", function(){

    if (sessionId != 0) {
         checkQnaLikeForColor(boardId);
     }

});
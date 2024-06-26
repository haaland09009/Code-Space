package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qnaReply")
public class QnaReplyController {
    private final QnaReplyService qnaReplyService;
    private final QnaReplyCommentService qnaReplyCommentService;
    private final QnaService qnaService;
    private final UserService userService;
    private final NotificationService notificationService;


    /* 답변 작성 */
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute QnaReplyDTO qnaReplyDTO) {
        /* enter 처리 */
        String content = qnaReplyDTO.getContent();
        content = content.replaceAll("\r\n", "<br>");
        qnaReplyDTO.setContent(content);


        Long replyId = qnaReplyService.save(qnaReplyDTO);
        if (replyId != null) {
            List<QnaReplyDTO> qnaReplyDTOList = qnaReplyService.findAll(qnaReplyDTO.getQnaId());

            for (QnaReplyDTO dto : qnaReplyDTOList) {
                /* enter 처리 */
                String replyContent = dto.getContent();
                replyContent = replyContent.replaceAll("<br>", "\n");
                dto.setContent(replyContent);

                JobDTO replyJob = userService.findJob(dto.getUserId());
                dto.setJobName(replyJob.getName());
            }

            /* 알림 넣기 */
            QnaDTO qnaDTO = qnaService.findById(qnaReplyDTO.getQnaId());
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setReceiver(qnaDTO.getUserId()); // 수신자 pk
            notificationDTO.setSender(qnaReplyDTO.getUserId()); // 발신자 pk
            notificationDTO.setEntityId(replyId); // 답변 pk
            notificationDTO.setNotUrl("/qna/" + qnaDTO.getId() + "#reply_" + replyId);
            notificationService.saveQnaNotice(notificationDTO, "qna_reply");

            return new ResponseEntity<>(qnaReplyDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }


    /* 답변 목록 조회 */
    @GetMapping("/getReplyList/{qnaId}")
    public ResponseEntity getReplyList(@PathVariable("qnaId") Long qnaId, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (qnaId != null) {
            List<QnaReplyDTO> qnaReplyDTOList = qnaReplyService.findAll(qnaId);
            for (QnaReplyDTO dto : qnaReplyDTOList) {
                String replyContent = dto.getContent();
                replyContent = replyContent.replaceAll("<br>", "\n");
                dto.setContent(replyContent);

                JobDTO replyJob = userService.findJob(dto.getUserId());
                dto.setJobName(replyJob.getName());

                /* 좋아요 수 */
                int likeCount = qnaReplyService.likeCount(dto.getId());
                dto.setLikeCount(likeCount);

                /* 답변에 달린 댓글 가져오기 */
                List<QnaReplyCommentDTO> qnaReplyCommentDTOList = qnaReplyCommentService.findAll(dto.getId());
                List<QnaReplyCommentDTO> commentList = new ArrayList<>();
                for (QnaReplyCommentDTO qnaReplyCommentDTO : qnaReplyCommentDTOList) {
                    String commentContent = qnaReplyCommentDTO.getContent();
                    commentContent = commentContent.replaceAll("<br>", "\n");
                    qnaReplyCommentDTO.setContent(commentContent);

                    JobDTO commentJob = userService.findJob(qnaReplyCommentDTO.getUserId());
                    qnaReplyCommentDTO.setJobName(commentJob.getName());

                    commentList.add(qnaReplyCommentDTO);
                }
                dto.setCommentList(commentList);

                /* 댓글 수 */
                int commentCount = qnaReplyCommentService.commentCount(dto.getId());
                dto.setCommentCount(commentCount);

                if (sessionUser == null) {
                    dto.setIsLike(0);
                } else {
                    int isLike = qnaReplyService.checkReplyLikeForColor(dto.getId(), sessionUser.getId());
                    dto.setIsLike(isLike);
                }

            }
            return new ResponseEntity<>(qnaReplyDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    /* 답변에 작성된 댓글 수 확인 */
    @GetMapping("/checkCommentCount/{id}")
    public @ResponseBody int checkCommentCount(@PathVariable("id") Long id) {
        return qnaReplyCommentService.commentCount(id);
    }

    /* 답변 삭제 */
    @PostMapping("/delete/{id}")
    public @ResponseBody void delete(@PathVariable("id") Long id) {
        qnaReplyService.deleteById(id);
    }

    /* 답글 수 조회 */
    @GetMapping("/count/{id}")
    public @ResponseBody int replyCount(@PathVariable("id") Long id) {
        return qnaReplyService.count(id);
    }

    /* 본인이 작성한 답변인지의 여부 확인 (좋아요 클릭 방지) */
    @GetMapping("isYourReply")
    public @ResponseBody boolean isYourReply(@ModelAttribute QnaReplyDTO qnaReplyDTO) {
        Long userId = qnaReplyDTO.getUserId(); // sessionId
        Long writerId = qnaReplyService.isYourReply(qnaReplyDTO.getId());
        if (userId.equals(writerId)) {
            return true;
        } else {
            return false;
        }
    }

    /* 답변 좋아요 클릭 */
    @PostMapping("like")
    public @ResponseBody void like(@ModelAttribute QnaReplyLikeDTO qnaReplyLikeDTO) {
        qnaReplyService.like(qnaReplyLikeDTO);
    }

    /* 답변 좋아요 수 업데이트 */
    @GetMapping("likeCount/{id}")
    public @ResponseBody int likeCount(@PathVariable("id") Long id) {
        return qnaReplyService.likeCount(id);
    }

    /* 사용자가 답변에 좋아요를 눌렀는지 확인 (색깔 변경 목적) */
    @GetMapping("/checkReplyLikeForColor/{id}")
    public @ResponseBody boolean checkReplyLikeForColor(@PathVariable("id") Long id,
                                                      HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        int count = qnaReplyService.checkReplyLikeForColor(id, sessionUser.getId());
        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    /* 답변 수정하기 */
    @PostMapping("/update")
    public @ResponseBody String updateReply(@ModelAttribute QnaReplyDTO qnaReplyDTO) {
        QnaReplyDTO updatedReplyDTO = qnaReplyService.updateReply(qnaReplyDTO);
        if (updatedReplyDTO == null) {
            return "no";
        } else {
            return "ok";
        }
    }









}

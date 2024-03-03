package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final ProjectCommentService projectCommentService;
    private final UserService userService;
    private final QnaReplyService qnaReplyService;
    private final QnaReplyCommentService qnaReplyCommentService;

    // 알림 조회
    @GetMapping("/findAll/{id}")
    public ResponseEntity findAll(@PathVariable("id") Long userId) {
        List<NotificationDTO> notificationDTOList = notificationService.findAll(userId);
        if (notificationDTOList != null) {
            for (NotificationDTO notificationDTO : notificationDTOList) {
                // 댓글이 있으면
                if (projectCommentService.findById(notificationDTO.getNotContentId()) != null
                        && notificationDTO.getNotType().equals("projectComment")) {
                    Long contentId = notificationDTO.getNotContentId();
                    ProjectCommentDTO projectCommentDTO = projectCommentService.findById(contentId);
                    notificationDTO.setSender(projectCommentDTO.getWriter());

                    UserDTO senderDTO = userService.findById(notificationDTO.getSenderId());
                    notificationDTO.setSender(senderDTO.getNickname());
                    notificationDTO.setOccurDate(projectCommentDTO.getRegDate());
                    notificationDTO.setSentence("님이 회원님이 작성하신 글에 댓글을 작성했습니다.");
                    notificationDTO.setFileAttached(senderDTO.getFileAttached());
                    notificationDTO.setStoredFileName(senderDTO.getStoredFileName());
                }
                // 답변 알림 조회
                else if (qnaReplyService.findById(notificationDTO.getNotContentId()) != null
                        && notificationDTO.getNotType().equals("qnaReply")) {
                    Long replyId = notificationDTO.getNotContentId();
                    QnaReplyDTO qnaReplyDTO = qnaReplyService.findById(replyId);
                    notificationDTO.setSender(qnaReplyDTO.getWriter());

                    UserDTO senderDTO = userService.findById(notificationDTO.getSenderId());
                    notificationDTO.setSender(senderDTO.getNickname());
                    notificationDTO.setOccurDate(qnaReplyDTO.getRegDate());
                    notificationDTO.setSentence("님이 회원님이 작성하신 글에 답변을 작성했습니다.");
                    notificationDTO.setFileAttached(senderDTO.getFileAttached());
                    notificationDTO.setStoredFileName(senderDTO.getStoredFileName());
                }
                else if (qnaReplyCommentService.findById(notificationDTO.getNotContentId()) != null
                        && notificationDTO.getNotType().equals("qnaReplyComment")) {
                    Long commentId = notificationDTO.getNotContentId();
                    QnaReplyCommentDTO qnaReplyCommentDTO = qnaReplyCommentService.findById(commentId);
                    notificationDTO.setSender(qnaReplyCommentDTO.getWriter());

                    UserDTO senderDTO = userService.findById(notificationDTO.getSenderId());
                    notificationDTO.setSender(senderDTO.getNickname());
                    notificationDTO.setOccurDate(qnaReplyCommentDTO.getRegDate());
                    notificationDTO.setSentence("님이 회원님이 작성하신 답변에 댓글을 작성했습니다.");
                    notificationDTO.setFileAttached(senderDTO.getFileAttached());
                    notificationDTO.setStoredFileName(senderDTO.getStoredFileName());

                }


            }
            return new ResponseEntity<>(notificationDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("알림이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

}

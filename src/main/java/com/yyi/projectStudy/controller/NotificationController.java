package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.NotificationDTO;
import com.yyi.projectStudy.dto.ProjectCommentDTO;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.service.NotificationService;
import com.yyi.projectStudy.service.ProjectCommentService;
import com.yyi.projectStudy.service.UserService;
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

    // 알림 조회
    @GetMapping("/findAll/{id}")
    public ResponseEntity findAll(@PathVariable("id") Long userId) {
        List<NotificationDTO> notificationDTOList = notificationService.findAll(userId);
        if (notificationDTOList != null) {
            for (NotificationDTO notificationDTO : notificationDTOList) {
                // 댓글이 있으면
                if (projectCommentService.findById(notificationDTO.getNotContentId()) != null) {
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
            }
            return new ResponseEntity<>(notificationDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("알림이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

}

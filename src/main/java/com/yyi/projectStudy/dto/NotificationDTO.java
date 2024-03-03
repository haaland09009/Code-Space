package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.NotificationEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationDTO {
    private Long id;
    private Long userId; // 수신자
    private Long senderId; // 발신자
    private String notType;
    private Long notContentId;
    private String notUrl; // url
    private LocalDateTime readDate; // 읽은 날짜


    private String sender;
    private String sentence;
    private LocalDateTime occurDate;
    // 발신자
    private int fileAttached;
    private String storedFileName;

    public static NotificationDTO toNotificationDTO(NotificationEntity notificationEntity) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notificationEntity.getId());
        notificationDTO.setUserId(notificationEntity.getUserEntity().getId());
        notificationDTO.setSenderId(notificationEntity.getSenderId());
        notificationDTO.setNotType(notificationEntity.getNotType());
        if (notificationEntity.getNotType().equals("projectComment")) {
            notificationDTO.setNotContentId(notificationEntity.getProjectCommentEntity().getId());
        } else if (notificationEntity.getNotType().equals("qnaReply")) {
            notificationDTO.setNotContentId(notificationEntity.getQnaReplyEntity().getId());
        } else if (notificationEntity.getNotType().equals("qnaReplyComment")) {
            notificationDTO.setNotContentId(notificationEntity.getQnaReplyCommentEntity().getId());
        }
        notificationDTO.setNotUrl(notificationEntity.getNotUrl());
        notificationDTO.setReadDate(notificationEntity.getReadDate());


        return notificationDTO;
    }
}

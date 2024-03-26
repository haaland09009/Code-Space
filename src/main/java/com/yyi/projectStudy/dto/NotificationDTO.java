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
    private Long notId;
    private Long receiver;
    private Long sender;
    private Long entityId;
    private String notUrl;
    private LocalDateTime readDate;
    private String content;
    private LocalDateTime regDate;


    private int fileAttached;
    private String storedFileName;
    private String nickname;
    private String title;
    private String notContent;

    private String formattedDate;


    public static NotificationDTO toNotificationDTO(NotificationEntity notificationEntity) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notificationEntity.getId());
        notificationDTO.setNotId(notificationEntity.getNotTypeEntity().getId());
        notificationDTO.setReceiver(notificationEntity.getReceiver().getId());
        notificationDTO.setSender(notificationEntity.getSender().getId());
        notificationDTO.setEntityId(notificationEntity.getEntityId());
        notificationDTO.setNotUrl(notificationEntity.getNotUrl());
        notificationDTO.setContent(notificationEntity.getContent());
        notificationDTO.setRegDate(notificationEntity.getRegDate());
        notificationDTO.setReadDate(notificationEntity.getReadDate());

        /* 알림 발신 사용자 정보 */
        notificationDTO.setFileAttached(notificationEntity.getSender().getFileAttached());
        if (notificationEntity.getSender().getFileAttached() == 1) {
            notificationDTO.setStoredFileName(notificationEntity.getSender().getUserImageFileEntityList().get(0).getStoredFileName());
        }
        notificationDTO.setNickname(notificationEntity.getSender().getNickname());
        return notificationDTO;
    }
}

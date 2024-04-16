package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.NotificationDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "notification_table")
public class NotificationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "not_id")
    private NotTypeEntity notTypeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private String notUrl;

    @Column(nullable = false)
    private String content;

    @Column
    private LocalDateTime readDate;


    /* 알림 저장 */
    public static NotificationEntity toNotificationEntity(NotificationDTO notificationDTO,
                                                      NotTypeEntity notTypeEntity,
                                                      UserEntity receiver, UserEntity sender) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setNotTypeEntity(notTypeEntity);
        notificationEntity.setReceiver(receiver);
        notificationEntity.setSender(sender);
        notificationEntity.setEntityId(notificationDTO.getEntityId());
        notificationEntity.setContent(notificationDTO.getContent());
        notificationEntity.setNotUrl(notificationDTO.getNotUrl());
        return notificationEntity;
    }


}

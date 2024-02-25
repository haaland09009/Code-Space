    package com.yyi.projectStudy.entity;

    import com.yyi.projectStudy.dto.NotificationDTO;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalDateTime;

    @Entity
    @Getter
    @Setter
    @SequenceGenerator(
            name = "notification_seq_generator"
            , sequenceName = "notification_seq"
            , initialValue = 1
            , allocationSize = 1
    )
    @Table(name = "notification_table")
    public class NotificationEntity {
        @Id
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE
                , generator = "notification_seq_generator"
        )
        private Long id;

        @Column(nullable = false)
        private Long senderId;

        @Column(nullable = false)
        private String notType;

        @Column(nullable = false)
        private String notUrl;

        @Column
        private LocalDateTime readDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "not_content_id")
        private ProjectCommentEntity projectCommentEntity;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private UserEntity userEntity;

        // 알림 저장 : 댓글
        public static NotificationEntity toCommentNotificationEntity(NotificationDTO notificationDTO,
                                                              UserEntity userEntity,
                                                              ProjectCommentEntity projectCommentEntity) {
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setId(notificationDTO.getId());
            notificationEntity.setSenderId(notificationDTO.getSenderId());
            notificationEntity.setNotType(notificationDTO.getNotType());
            notificationEntity.setProjectCommentEntity(projectCommentEntity);
            notificationEntity.setNotUrl(notificationDTO.getNotUrl());
            notificationEntity.setUserEntity(userEntity);
            // 읽음 컬럼 추가??
            return notificationEntity;
        }


    }

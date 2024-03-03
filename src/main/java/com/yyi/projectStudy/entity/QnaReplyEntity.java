package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.QnaReplyDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "qna_reply_seq_generator"
        , sequenceName = "qna_reply_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "qna_reply_table")
public class QnaReplyEntity extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "qna_reply_seq_generator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private QnaEntity qnaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(nullable = false, length = 4000)
    private String content;

    @OneToMany(mappedBy = "qnaReplyEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<QnaReplyLikeEntity> qnaReplyLikeEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "qnaReplyEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<QnaReplyCommentEntity> qnaReplyCommentEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "qnaReplyEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<NotificationEntity> notificationEntityList = new ArrayList<>();

    public static QnaReplyEntity toQnaReplyEntity(QnaReplyDTO qnaReplyDTO, QnaEntity qnaEntity,
                                                  UserEntity userEntity) {
        QnaReplyEntity qnaReplyEntity = new QnaReplyEntity();
        qnaReplyEntity.setContent(qnaReplyDTO.getContent());
        qnaReplyEntity.setQnaEntity(qnaEntity);
        qnaReplyEntity.setUserEntity(userEntity);
        return qnaReplyEntity;
    }

}

package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "qna_reply_like_table")
public class QnaReplyLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private QnaReplyEntity qnaReplyEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


    public static QnaReplyLikeEntity toQnaReplyLikeEntity(QnaReplyEntity qnaReplyEntity, UserEntity userEntity) {
        QnaReplyLikeEntity qnaReplyLikeEntity = new QnaReplyLikeEntity();
        qnaReplyLikeEntity.setQnaReplyEntity(qnaReplyEntity);
        qnaReplyLikeEntity.setUserEntity(userEntity);
        return qnaReplyLikeEntity;
    }
}

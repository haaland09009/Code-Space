package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "qna_dislike_table")
public class QnaDisLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private QnaEntity qnaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public static QnaDisLikeEntity toQnaDisLikeEntity(QnaEntity qnaEntity, UserEntity userEntity) {
        QnaDisLikeEntity qnaDisLikeEntity = new QnaDisLikeEntity();
        qnaDisLikeEntity.setQnaEntity(qnaEntity);
        qnaDisLikeEntity.setUserEntity(userEntity);
        return qnaDisLikeEntity;
    }
}

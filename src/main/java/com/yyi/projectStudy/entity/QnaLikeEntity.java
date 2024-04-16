package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "qna_like_table")
public class QnaLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private QnaEntity qnaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public static QnaLikeEntity toQnaLikeEntity(QnaEntity qnaEntity, UserEntity userEntity) {
        QnaLikeEntity qnaLikeEntity = new QnaLikeEntity();
        qnaLikeEntity.setQnaEntity(qnaEntity);
        qnaLikeEntity.setUserEntity(userEntity);
        return qnaLikeEntity;
    }
}

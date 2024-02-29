package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "qna_like_seq_generator"
        , sequenceName = "qna_like_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "qna_like_table")
public class QnaLikeEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "qna_like_seq_generator"
    )
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

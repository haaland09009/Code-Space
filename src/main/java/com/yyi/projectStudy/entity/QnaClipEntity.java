package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "qna_clip_table")
public class QnaClipEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private QnaEntity qnaEntity;


    public static QnaClipEntity toQnaClipEntity(UserEntity userEntity, QnaEntity qnaEntity) {
        QnaClipEntity qnaClipEntity = new QnaClipEntity();
        qnaClipEntity.setUserEntity(userEntity);
        qnaClipEntity.setQnaEntity(qnaEntity);
        return qnaClipEntity;
    }
}

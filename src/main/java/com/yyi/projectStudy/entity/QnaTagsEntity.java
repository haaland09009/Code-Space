package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "qna_tags_table")
public class QnaTagsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private QnaEntity qnaEntity;

    @Column(nullable = false)
    private String tag;


    public static QnaTagsEntity toQnaTagsEntity(QnaEntity qnaEntity, String tag) {
        QnaTagsEntity qnaTagsEntity = new QnaTagsEntity();
        qnaTagsEntity.setQnaEntity(qnaEntity);
        qnaTagsEntity.setTag(tag);
        return qnaTagsEntity;
    }

}

package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "qna_topic_seq_generator"
        , sequenceName = "qna_topic_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "qna_topic_table")
public class QnaTopicEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "qna_topic_seq_generator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private QnaEntity qnaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private TopicEntity topicEntity;

    public static QnaTopicEntity toQnaTopicEntity(QnaEntity qnaEntity, TopicEntity topicEntity) {
        QnaTopicEntity qnaTopicEntity = new QnaTopicEntity();
        qnaTopicEntity.setQnaEntity(qnaEntity);
        qnaTopicEntity.setTopicEntity(topicEntity);
        return qnaTopicEntity;
    }

}

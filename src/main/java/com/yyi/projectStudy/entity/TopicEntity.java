package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "topic_seq_generator"
        , sequenceName = "topic_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "topic_table")
public class TopicEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "topic_seq_generator"
    )
    private Long id;

    private String name;

    @OneToMany(mappedBy = "topicEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<QnaTopicEntity> qnaTopicEntityList = new ArrayList<>();

}

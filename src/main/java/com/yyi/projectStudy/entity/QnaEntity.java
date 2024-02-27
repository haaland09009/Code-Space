package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.QnaDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "qna_seq_generator"
        , sequenceName = "qna_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "qna_table")
public class QnaEntity extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "qna_seq_generator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(nullable = false)
    private int readCount;

    @OneToMany(mappedBy = "qnaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<QnaTopicEntity> qnaTopicEntityList = new ArrayList<>();

    public static QnaEntity toQnaEntity(QnaDTO qnaDTO, UserEntity userEntity) {
        QnaEntity qnaEntity = new QnaEntity();
        qnaEntity.setTitle(qnaDTO.getTitle());
        qnaEntity.setContent(qnaDTO.getContent());
        qnaEntity.setReadCount(0);
        qnaEntity.setUserEntity(userEntity);
        return qnaEntity;
    }
}

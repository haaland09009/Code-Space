package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.QnaDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @Column
    private LocalDateTime updDate;

    @OneToMany(mappedBy = "qnaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<QnaTopicEntity> qnaTopicEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "qnaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<QnaReplyEntity> qnaReplyEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "qnaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<QnaLikeEntity> qnaLikeEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "qnaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<QnaDisLikeEntity> qnaDisLikeEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "qnaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<QnaClipEntity> qnaClipEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "qnaEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<QnaTagsEntity> qnaTagsEntityList = new ArrayList<>();




    public static QnaEntity toQnaEntity(QnaDTO qnaDTO, UserEntity userEntity) {
        QnaEntity qnaEntity = new QnaEntity();
        qnaEntity.setTitle(qnaDTO.getTitle());
        qnaEntity.setContent(qnaDTO.getContent());
        qnaEntity.setReadCount(0);
        qnaEntity.setUserEntity(userEntity);
        return qnaEntity;
    }
}

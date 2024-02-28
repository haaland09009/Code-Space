package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.QnaReplyDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "qna_reply_seq_generator"
        , sequenceName = "qna_reply_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "qna_reply_table")
public class QnaReplyEntity extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "qna_reply_seq_generator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private QnaEntity qnaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(nullable = false, length = 4000)
    private String content;

    public static QnaReplyEntity toQnaReplyEntity(QnaReplyDTO qnaReplyDTO, QnaEntity qnaEntity,
                                                  UserEntity userEntity) {
        QnaReplyEntity qnaReplyEntity = new QnaReplyEntity();
        qnaReplyEntity.setContent(qnaReplyDTO.getContent());
        qnaReplyEntity.setQnaEntity(qnaEntity);
        qnaReplyEntity.setUserEntity(userEntity);
        return qnaReplyEntity;
    }

}

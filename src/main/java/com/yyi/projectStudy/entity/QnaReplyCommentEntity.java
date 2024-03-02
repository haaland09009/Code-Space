package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.QnaReplyCommentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "qna_reply_comment_seq_generator"
        , sequenceName = "qna_reply_comment_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "qna_reply_comment_table")
public class QnaReplyCommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "qna_reply_comment_seq_generator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private QnaReplyEntity qnaReplyEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(nullable = false, length = 4000)
    private String content;


    public static QnaReplyCommentEntity toQnaReplyCommentEntity(QnaReplyCommentDTO qnaReplyCommentDTO, QnaReplyEntity qnaReplyEntity, UserEntity userEntity) {
        QnaReplyCommentEntity qnaReplyCommentEntity = new QnaReplyCommentEntity();
        qnaReplyCommentEntity.setQnaReplyEntity(qnaReplyEntity);
        qnaReplyCommentEntity.setUserEntity(userEntity);
        qnaReplyCommentEntity.setContent(qnaReplyCommentDTO.getContent());
        return qnaReplyCommentEntity;
    }
}

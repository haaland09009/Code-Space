package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "pro_cmt_dislike_seq_generator"
        , sequenceName = "pro_cmt_dislike_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "pro_cmt_dislike_table")
public class ProCmtDisLikeEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "pro_cmt_dislike_seq_generator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private ProjectCommentEntity projectCommentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public static ProCmtDisLikeEntity toProCmtDisLikeEntity(ProjectCommentEntity projectCommentEntity,
                                                      UserEntity userEntity) {
        ProCmtDisLikeEntity proCmtDisLikeEntity = new ProCmtDisLikeEntity();
        proCmtDisLikeEntity.setProjectCommentEntity(projectCommentEntity);
        proCmtDisLikeEntity.setUserEntity(userEntity);
        return proCmtDisLikeEntity;
    }
}

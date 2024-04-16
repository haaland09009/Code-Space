package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pro_cmt_dislike_table")
public class ProCmtDisLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

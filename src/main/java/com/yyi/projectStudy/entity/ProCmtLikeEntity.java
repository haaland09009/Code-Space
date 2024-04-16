package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pro_cmt_like_table")
public class ProCmtLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private ProjectCommentEntity projectCommentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public static ProCmtLikeEntity toProCmtLikeEntity(ProjectCommentEntity projectCommentEntity,
                                                      UserEntity userEntity) {
        ProCmtLikeEntity proCmtLikeEntity = new ProCmtLikeEntity();
        proCmtLikeEntity.setProjectCommentEntity(projectCommentEntity);
        proCmtLikeEntity.setUserEntity(userEntity);
        return proCmtLikeEntity;
    }

}

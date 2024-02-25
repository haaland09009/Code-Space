package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProCmtLikeEntity;
import com.yyi.projectStudy.entity.ProjectCommentEntity;
import com.yyi.projectStudy.entity.ProjectEntity;
import com.yyi.projectStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProCmtLikeRepository extends JpaRepository<ProCmtLikeEntity, Long> {

    // 댓글 좋아요 수 확인
    // select count(*) from pro_cmt_like_table where comment_id = ?;
    int countByProjectCommentEntity(ProjectCommentEntity projectCommentEntity);

    // 댓글 좋아요 pk 확인
    Optional<ProCmtLikeEntity> findByProjectCommentEntityAndUserEntity(ProjectCommentEntity projectCommentEntity,
                                                                       UserEntity userEntity);

    // 회원이 댓글에 좋아요를 눌렀는지
    // select count(*) from pro_cmt_like_table where comment_id = ? and user_id = ?;
    int countByProjectCommentEntityAndUserEntity(ProjectCommentEntity projectCommentEntity,
                                                 UserEntity userEntity);
}

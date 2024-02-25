package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProCmtDisLikeEntity;
import com.yyi.projectStudy.entity.ProCmtLikeEntity;
import com.yyi.projectStudy.entity.ProjectCommentEntity;
import com.yyi.projectStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProCmtDisLikeRepository extends JpaRepository<ProCmtDisLikeEntity, Long> {

    // 댓글 싫어요 수 확인
    // select count(*) from pro_cmt_dislike_table where comment_id = ?;
    int countByProjectCommentEntity(ProjectCommentEntity projectCommentEntity);

    // 댓글 싫어요 pk 확인
    Optional<ProCmtDisLikeEntity> findByProjectCommentEntityAndUserEntity(ProjectCommentEntity projectCommentEntity,
                                                                       UserEntity userEntity);

    // 회원이 댓글에 싫어요를 눌렀는지
    // select count(*) from pro_cmt_dislike_table where comment_id = ? and user_id = ?;
    int countByProjectCommentEntityAndUserEntity(ProjectCommentEntity projectCommentEntity,
                                                 UserEntity userEntity);
}

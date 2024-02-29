package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.QnaReplyEntity;
import com.yyi.projectStudy.entity.QnaReplyLikeEntity;
import com.yyi.projectStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QnaReplyLikeRepository extends JpaRepository<QnaReplyLikeEntity, Long> {
    // select count(*) from qna_reply_like_table where reply_id = ? and user_id = ?;
    int countByQnaReplyEntityAndUserEntity(QnaReplyEntity qnaReplyEntity, UserEntity userEntity);

    // select * from qna_reply_like_table where reply_id = ? and user_id = ?;
    Optional<QnaReplyLikeEntity> findByQnaReplyEntityAndUserEntity(QnaReplyEntity qnaReplyEntity, UserEntity userEntity);

    // select count(*) from qna_reply_like_table where reply_id = ?;
    int countByQnaReplyEntity(QnaReplyEntity qnaReplyEntity);

}

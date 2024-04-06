package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaReplyCommentEntity;
import com.yyi.projectStudy.entity.QnaReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnaReplyCommentRepository extends JpaRepository<QnaReplyCommentEntity, Long> {

    /* 답변에 달린 댓글 목록 */
    List<QnaReplyCommentEntity> findAllByQnaReplyEntityOrderByIdDesc(QnaReplyEntity qnaReplyEntity);

    /* 답변에 달린 댓글 수 */
    int countByQnaReplyEntity(QnaReplyEntity qnaReplyEntity);

    /* 댓글 수정 처리 */
    @Modifying
    @Query(value = "update QnaReplyCommentEntity set content = :content, updDate = sysdate where id = :id")
    void updateComment(@Param("content") String content, @Param("id") Long id);



}

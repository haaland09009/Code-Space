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

    // select * from qna_reply_comment_table where reply_id = ? order by id desc
    List<QnaReplyCommentEntity> findAllByQnaReplyEntityOrderByIdDesc(QnaReplyEntity qnaReplyEntity);

    // select count(*) from qna_reply_comment_table where reply_id = ?
    int countByQnaReplyEntity(QnaReplyEntity qnaReplyEntity);

    /* 댓글 수정 처리 */
    @Modifying
    @Query(value = "update QnaReplyCommentEntity set content = :content, updDate = sysdate where id = :id")
    void updateComment(@Param("content") String content, @Param("id") Long id);



}

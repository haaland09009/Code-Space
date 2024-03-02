package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaReplyCommentEntity;
import com.yyi.projectStudy.entity.QnaReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QnaReplyCommentRepository extends JpaRepository<QnaReplyCommentEntity, Long> {

    // select * from qna_reply_comment_table where reply_id = ? order by id desc
    List<QnaReplyCommentEntity> findAllByQnaReplyEntityOrderByIdDesc(QnaReplyEntity qnaReplyEntity);

    // select count(*) from qna_reply_comment_table where reply_id = ?
    int countByQnaReplyEntity(QnaReplyEntity qnaReplyEntity);

//     SELECT qt.id, COUNT(*)
//          FROM
//          qna_table qt JOIN qna_reply_table qr
//          ON qt.id = qr.qna_id
//          JOIN qna_reply_comment_table qc
//          ON qr.id = qc.reply_id
//          GROUP BY qt.id
//          ORDER BY qt.id DESC

}

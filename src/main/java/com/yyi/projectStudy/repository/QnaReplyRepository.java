package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectCommentEntity;
import com.yyi.projectStudy.entity.ProjectEntity;
import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaReplyRepository extends JpaRepository<QnaReplyEntity, Long> {
    // 게시글 당 댓글 조회
    // select * from qna_reply_table where qna_id = ? order by id desc;
    List<QnaReplyEntity> findAllByQnaEntityOrderByIdDesc(QnaEntity qnaEntity);

    // select count(*) from qna_reply_table where qna_id = ?
    int countByQnaEntity(QnaEntity qnaEntity);
}

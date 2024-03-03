package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectCommentEntity;
import com.yyi.projectStudy.entity.ProjectEntity;
import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QnaReplyRepository extends JpaRepository<QnaReplyEntity, Long> {
    // 게시글 당 댓글 조회
    // select * from qna_reply_table where qna_id = ? order by id desc;
    List<QnaReplyEntity> findAllByQnaEntityOrderByIdDesc(QnaEntity qnaEntity);

    // select count(*) from qna_reply_table where qna_id = ?
    int countByQnaEntity(QnaEntity qnaEntity);


    // SELECT id, qna_id FROM qna_reply_table WHERE id IN (SELECT reply_id FROM (SELECT reply_id, COUNT(*) likeCount FROM qna_reply_like_table GROUP BY reply_id ORDER BY likeCount DESC) WHERE ROWNUM <= 10);
    @Query(value = "SELECT * FROM (\n" +
            "    SELECT r.*\n" +
            "    FROM qna_reply_table r\n" +
            "    JOIN (\n" +
            "        SELECT reply_id, COUNT(*) likeCount\n" +
            "        FROM qna_reply_like_table\n" +
            "        GROUP BY reply_id\n" +
            "        ORDER BY likeCount DESC\n" +
            "    ) l ON r.id = l.reply_id\n" +
            "    WHERE ROWNUM <= 5\n" +
            ")", nativeQuery = true)
    List<QnaReplyEntity> findBestReply();

}

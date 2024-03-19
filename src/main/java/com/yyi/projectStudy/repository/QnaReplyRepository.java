package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectCommentEntity;
import com.yyi.projectStudy.entity.ProjectEntity;
import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QnaReplyRepository extends JpaRepository<QnaReplyEntity, Long> {
    // 게시글 당 댓글 조회
    // select * from qna_reply_table where qna_id = ? order by id desc;
    List<QnaReplyEntity> findAllByQnaEntityOrderByIdDesc(QnaEntity qnaEntity);

    // select count(*) from qna_reply_table where qna_id = ?
    int countByQnaEntity(QnaEntity qnaEntity);


    // SELECT id, qna_id FROM qna_reply_table WHERE id IN (SELECT reply_id FROM (SELECT reply_id, COUNT(*) likeCount FROM qna_reply_like_table GROUP BY reply_id ORDER BY likeCount DESC) WHERE ROWNUM <= 10);
    @Query(value = "SELECT *\n" +
            "FROM (\n" +
            "    SELECT r.*\n" +
            "    FROM qna_reply_table r\n" +
            "    JOIN (\n" +
            "        SELECT reply_id, COUNT(*) likeCount\n" +
            "        FROM qna_reply_like_table\n" +
            "        GROUP BY reply_id\n" +
            "        ORDER BY likeCount DESC\n" +
            "    ) l ON r.id = l.reply_id\n" +
            "    ORDER BY l.likeCount DESC\n" +
            ")\n" +
            "WHERE ROWNUM <= 5", nativeQuery = true)
    List<QnaReplyEntity> findBestReply();


/*    SELECT replyId as Id
    FROM (
            SELECT
                    qt.id AS qnaId,
            qr.id AS replyId,
            COUNT(ql.id) AS likeCount,
    ROW_NUMBER() OVER (PARTITION BY qt.id ORDER BY COUNT(ql.id) DESC) AS rank
    FROM
    qna_table qt
    JOIN qna_reply_table qr ON qt.id = qr.qna_id
    LEFT JOIN qna_reply_like_table ql ON qr.id = ql.reply_id
    GROUP BY
    qt.id, qr.id
    HAVING COUNT(ql.id) >= 10
            )
    WHERE rank = 1 */
    @Query(value = "SELECT replyId as Id\n" +
            "          FROM (\n" +
            "          SELECT\n" +
            "          qt.id AS qnaId,\n" +
            "          qr.id AS replyId,\n" +
            "          COUNT(ql.id) AS likeCount,\n" +
            "          ROW_NUMBER() OVER (PARTITION BY qt.id ORDER BY COUNT(ql.id) DESC) AS rank\n" +
            "          FROM\n" +
            "          qna_table qt\n" +
            "          JOIN qna_reply_table qr ON qt.id = qr.qna_id\n" +
            "          LEFT JOIN qna_reply_like_table ql ON qr.id = ql.reply_id\n" +
            "          GROUP BY\n" +
            "          qt.id, qr.id\n" +
            "          HAVING COUNT(ql.id) >= 8\n" +
            "      )\n" +
            "      WHERE rank = 1", nativeQuery = true)
    List<Long> bestReplyPkList();

    /* 답변 수정하기 */
    @Modifying
    @Query(value = "update QnaReplyEntity set content = :content, updDate = sysdate where id = :id")
    void updateReply(String content, Long id);
}

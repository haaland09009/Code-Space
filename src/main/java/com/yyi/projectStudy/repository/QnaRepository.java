package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.dto.QnaBestReplyDTO;
import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnaRepository extends JpaRepository<QnaEntity, Long> {
    // 게시글 조회수 증가
    // update qna_table set read_count = read_count + 1 where id = ?
    @Modifying
    @Query(value = "update QnaEntity q set q.readCount = q.readCount + 1 where q.id = :id")
    void updateReadCount(@Param("id") Long id);

    // 게시글 수정
    // update qna_table set title = :title, content = :content where id = ?
    @Modifying
    @Query(value = "update QnaEntity q set q.title = :title, q.content = :content where q.id = :id")
    void updateQna(@Param("title") String title, @Param("content") String content, @Param("id") Long id);

//    @Query(value ="SELECT * FROM qna_table WHERE id IN (SELECT qna_id FROM (SELECT qna_id, COUNT(*) likeCount FROM qna_like_table GROUP BY qna_id ORDER BY likeCount DESC) WHERE ROWNUM <= 5)", nativeQuery = true)
//    List<QnaEntity> findBestLikeQna();

    @Query(value= "SELECT *\n" +
            "FROM (\n" +
            "    SELECT qna.*, likeCounts.likeCount, replyCounts.replyCount\n" +
            "    FROM qna_table qna\n" +
            "    JOIN (\n" +
            "        SELECT qna_id, COUNT(*) likeCount\n" +
            "        FROM qna_like_table\n" +
            "        GROUP BY qna_id\n" +
            "        ORDER BY likeCount DESC\n" +
            "    ) likeCounts ON qna.id = likeCounts.qna_id\n" +
            "    JOIN (\n" +
            "        SELECT qna_id, COUNT(*) replyCount\n" +
            "        FROM qna_reply_table\n" +
            "        GROUP BY qna_id\n" +
            "        ORDER BY replyCount DESC\n" +
            "    ) replyCounts ON qna.id = replyCounts.qna_id\n" +
            "    ORDER BY likeCounts.likeCount DESC, replyCounts.replyCount DESC\n" +
            ")\n" +
            "WHERE ROWNUM <= 5", nativeQuery = true)
    List<QnaEntity> findBestLikeQna();


    // 랜덤 추출 4개
    @Query(value = "SELECT * FROM (\n" +
            "    SELECT * FROM qna_table \n" +
            "    WHERE id != :id\n" +
            "    ORDER BY DBMS_RANDOM.VALUE\n" +
            ") \n" +
            "WHERE ROWNUM <= 4", nativeQuery = true)
    List<QnaEntity> randomQna(@Param("id") Long id);


    // 활동 내역
    //    SELECT * FROM (
    //            SELECT id AS qna_id, NULL AS reply_id, NULL AS comment_id, title, reg_date, content FROM qna_table WHERE user_id = 1
    //            UNION ALL
    //            SELECT NULL AS qna_id, id AS reply_id, NULL AS comment_id,  NULL AS title, reg_date, content FROM qna_reply_table WHERE user_id = 1
    //                    UNION ALL
    //                    SELECT NULL AS qna_id, NULL AS reply_id, id AS comment_id, NULL AS title, reg_date, content FROM qna_reply_comment_table WHERE user_id = 1
    //    )
    //    ORDER BY reg_date DESC
        @Query(value="SELECT * FROM (\n" +
                "    SELECT id AS qna_id, NULL AS reply_id, NULL AS comment_id, title, reg_date, content FROM qna_table WHERE user_id = :userId\n" +
                "    UNION ALL\n" +
                "    SELECT NULL AS qna_id, id AS reply_id, NULL AS comment_id,  NULL AS title, reg_date, content FROM qna_reply_table WHERE user_id = :userId\n" +
                "    UNION ALL\n" +
                "    SELECT NULL AS qna_id, NULL AS reply_id, id AS comment_id, NULL AS title, reg_date, content FROM qna_reply_comment_table WHERE user_id = :userId\n" +
                ")\n" +
                "ORDER BY reg_date DESC", nativeQuery = true)
        List<Object[]> getQnaArticles(@Param("userId") Long userId);

}

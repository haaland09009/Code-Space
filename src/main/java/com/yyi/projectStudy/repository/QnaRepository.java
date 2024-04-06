package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.dto.QnaBestReplyDTO;
import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaLikeEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Primary
public interface QnaRepository extends JpaRepository<QnaEntity, Long>, QnaCustom {
    /* 게시글 조회수 증가 */
    @Modifying
    @Query(value = "update QnaEntity q set q.readCount = q.readCount + 1 where q.id = :id")
    void updateReadCount(@Param("id") Long id);

   /* 게시글 수정 */
    @Modifying
    @Query(value = "update QnaEntity q set q.title = :title, q.content = :content, q.updDate = sysdate where q.id = :id")
    void updateQna(@Param("title") String title, @Param("content") String content, @Param("id") Long id);


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


   /* 랜덤 추출 4개 */
    @Query(value = "SELECT * FROM (\n" +
            "    SELECT * FROM qna_table \n" +
            "    WHERE id != :id\n" +
            "    ORDER BY DBMS_RANDOM.VALUE\n" +
            ") \n" +
            "WHERE ROWNUM <= 4", nativeQuery = true)
    List<QnaEntity> randomQna(@Param("id") Long id);

/*
     활동 내역
        SELECT * FROM (
                SELECT id AS qna_id, NULL AS reply_id, NULL AS comment_id, title, reg_date, content FROM qna_table WHERE user_id = 1
                UNION ALL
                SELECT NULL AS qna_id, id AS reply_id, NULL AS comment_id,  NULL AS title, reg_date, content FROM qna_reply_table WHERE user_id = 1
                        UNION ALL
                        SELECT NULL AS qna_id, NULL AS reply_id, id AS comment_id, NULL AS title, reg_date, content FROM qna_reply_comment_table WHERE user_id = 1
        )
        ORDER BY reg_date DESC*/
        @Query(value="SELECT * FROM (\n" +
                "    SELECT id AS qna_id, NULL AS reply_id, NULL AS comment_id, title, reg_date, content FROM qna_table WHERE user_id = :userId\n" +
                "    UNION ALL\n" +
                "    SELECT NULL AS qna_id, id AS reply_id, NULL AS comment_id,  NULL AS title, reg_date, content FROM qna_reply_table WHERE user_id = :userId\n" +
                "    UNION ALL\n" +
                "    SELECT NULL AS qna_id, NULL AS reply_id, id AS comment_id, NULL AS title, reg_date, content FROM qna_reply_comment_table WHERE user_id = :userId\n" +
                ")\n" +
                "ORDER BY reg_date DESC", nativeQuery = true)
        List<Object[]> getQnaArticles(@Param("userId") Long userId);

/*    select id
    from
            (select qt.id, count(qrt.qna_id) replyCount
    from qna_table qt
    join qna_topic_table qtt on qt.id = qtt.qna_id
    left join qna_reply_table qrt on qrt.qna_id = qt.id
    where topic_id = 2
    group by qt.id
    order by replyCount asc, qt.id desc )*/
    @Query(value = "select id \n" +
            "  from\n" +
            " (select qt.id, count(qrt.qna_id) replyCount\n" +
            "  from qna_table qt\n" +
            "  join qna_topic_table qtt on qt.id = qtt.qna_id\n" +
            "  left join qna_reply_table qrt on qrt.qna_id = qt.id\n" +
            "  where topic_id = :topicId\n" +
            "  group by qt.id\n" +
            "  order by replyCount asc, qt.id desc )", nativeQuery = true)
    /* 답변 적은 순 정렬 (토픽 포함) */
    List<Long> getQnaListSortByReplyAscAndTopic(@Param("topicId") Long topicId);

  /*  select id
  from
 (select qt.id, count(qrt.qna_id) replyCount
  from qna_table qt
  join qna_topic_table qtt on qt.id = qtt.qna_id
  left join qna_reply_table qrt on qrt.qna_id = qt.id
  group by qt.id
  order by replyCount asc, qt.id desc ) */
    @Query(value = "select id \n" +
            "  from\n" +
            " (select qt.id, count(qrt.qna_id) replyCount\n" +
            "  from qna_table qt\n" +
            "  join qna_topic_table qtt on qt.id = qtt.qna_id\n" +
            "  left join qna_reply_table qrt on qrt.qna_id = qt.id\n" +
            "  group by qt.id\n" +
            "  order by replyCount asc, qt.id desc )", nativeQuery = true)
        /* 답변 적은 순 정렬 (토픽 미포함) */
    List<Long> getQnaListSortByReplyAsc();

    /*    select id from(
   select qt.id, count(qr.qna_id) replyCount from qna_table qt
   left join qna_reply_table qr on qt.id = qr.qna_id
   group by qt.id
   having count(qr.qna_id) = 0) */
    /*  답변 0개인 게시물 */
    @Query(value = " select id from(\n" +
            "   select qt.id, count(qr.qna_id) replyCount from qna_table qt\n" +
            "   left join qna_reply_table qr on qt.id = qr.qna_id\n" +
            "   group by qt.id\n" +
            "   having count(qr.qna_id) = 0)", nativeQuery = true)
    List<Long> getNoReplyQnaList();


    /* 검색을 통한 조회 */
    @Query(value = "select q from QnaEntity q where lower(q.title) like concat('%', :searchWord, '%')" +
            "or lower(q.content) like concat('%', :searchWord, '%') order by q.id desc")
    List<QnaEntity> findByTitleOrContent(@Param("searchWord") String keyword);


    /* 해시태그를 통한 조회 */
    @Query("select q from QnaEntity q join q.qnaTagsEntityList qt where lower(qt.tag) like concat('%', :tagName, '%')" +
            "or lower(q.title) like concat('%', :tagName, '%') order by q.id desc")
    List<QnaEntity> findByTagName(@Param("tagName") String tagName);


}

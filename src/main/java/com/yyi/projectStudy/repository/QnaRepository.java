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


    @Query(value= "select *\n" +
            "from (\n" +
            "    select qna.*, likecounts.likecount, replycounts.replycount\n" +
            "    from qna_table qna\n" +
            "    join (\n" +
            "        select qna_id, count(*) as likecount\n" +
            "        from qna_like_table\n" +
            "        group by qna_id\n" +
            "        order by likecount desc\n" +
            "    ) likecounts on qna.id = likecounts.qna_id\n" +
            "    join (\n" +
            "        select qna_id, count(*) as replycount\n" +
            "        from qna_reply_table\n" +
            "        group by qna_id\n" +
            "        order by replycount desc\n" +
            "    ) replycounts on qna.id = replycounts.qna_id\n" +
            "    order by likecounts.likecount desc, replycounts.replycount desc\n" +
            ") as qna_summary\n" +
            "limit 5", nativeQuery = true)
    List<QnaEntity> findBestLikeQna();


   /* 랜덤 추출 4개 */
    @Query(value = "select * from (\n" +
            "    select * from qna_table \n" +
            "    where id != :id\n" +
            "    order by rand()\n" +
            ") as random_qna\n" +
            "limit 4", nativeQuery = true)
    List<QnaEntity> randomQna(@Param("id") Long id);

    /* 활동 내역*/
//    @Query(value="select * from (\n" +
//            "    select id as qna_id, null as reply_id, null as comment_id, title, reg_date, content from qna_table where user_id = :userId\n" +
//            "    union all\n" +
//            "    select null as qna_id, id as reply_id, null as comment_id, null as title, reg_date, content from qna_reply_table where user_id = :userId\n" +
//            "    union all\n" +
//            "    select null as qna_id, null as reply_id, id as comment_id, null as title, reg_date, content from qna_reply_comment_table where user_id = :userId\n" +
//            ") as combined_data\n" +
//            "order by reg_date desc", nativeQuery = true)
//    List<Object[]> getQnaArticles(@Param("userId") Long userId);

    @Query(value = "SELECT id FROM ( " +
            "    SELECT qt.id, COUNT(qrt.qna_id) AS replyCount " +
            "    FROM qna_table qt " +
            "    JOIN qna_topic_table qtt ON qt.id = qtt.qna_id " +
            "    LEFT JOIN qna_reply_table qrt ON qrt.qna_id = qt.id " +
            "    WHERE qtt.topic_id = :topicId " + // topicId를 조건으로 추가
            "    GROUP BY qt.id " +
            "    ORDER BY replyCount ASC, qt.id DESC " +
            ") AS subquery", nativeQuery = true)
    /* 답변 적은 순 정렬 (토픽 포함) */
    List<Long> getQnaListSortByReplyAscAndTopic(@Param("topicId") Long topicId);


    @Query(value = "SELECT id FROM (\n" +
            "    SELECT qt.id, COUNT(qrt.qna_id) AS replyCount\n" +
            "    FROM qna_table qt\n" +
            "    JOIN qna_topic_table qtt ON qt.id = qtt.qna_id\n" +
            "    LEFT JOIN qna_reply_table qrt ON qrt.qna_id = qt.id\n" +
            "    GROUP BY qt.id\n" +
            "    ORDER BY replyCount ASC, qt.id DESC\n" +
            ") AS subquery", nativeQuery = true)
        /* 답변 적은 순 정렬 (토픽 미포함) */
    List<Long> getQnaListSortByReplyAsc();


    /*  답변 0개인 게시물 */
    @Query(value = "select id from (select qt.id, count(qr.qna_id) as replyCount " +
            "from qna_table qt left join qna_reply_table qr on qt.id = qr.qna_id " +
            "group by qt.id having count(qr.qna_id) = 0) as subquery", nativeQuery = true)
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

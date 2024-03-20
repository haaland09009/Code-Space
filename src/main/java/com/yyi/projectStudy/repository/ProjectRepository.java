package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectEntity;
import com.yyi.projectStudy.entity.ProjectStudyCategoryEntity;
import com.yyi.projectStudy.entity.ProjectStudyCategoryLinkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    // 게시글 조회수 증가
    // update project_table set read_count = read_count + 1 where id = ?
    @Modifying
    @Query(value = "update ProjectEntity p set p.readCount = p.readCount + 1 where p.id = :id")
    void updateReadCount(@Param("id") Long id);

    /* 게시글 수정 */
    @Modifying
    @Query(value = "update ProjectEntity p set p.title = :title, p.content = :content, p.startDate = :startDate, p.headCount = :headCount, p.updDate = sysdate where p.id = :id")
    void updateProject(@Param("title") String title, @Param("content") String content,
                       @Param("startDate") Date startDate, @Param("headCount") int headCount, @Param("id") Long id);


    // 랜덤 추출 3개
    @Query(value = "SELECT * FROM (SELECT * FROM project_table ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM <= 3", nativeQuery = true)
    List<ProjectEntity> findRandomProjects();

    // 메인 페이지 HOT 프로젝트 / 스터디 조회 (일단 조회수 순, 나중에 수정해야함!!!)
    @Query(value = "SELECT * FROM (SELECT * FROM project_table ORDER BY read_count DESC) WHERE ROWNUM <= 6", nativeQuery = true)
    List<ProjectEntity> findAllByOrderByReadCountDesc();


   /* 모집상태에 따른 조회 */
   /* select * from project_table where status = '모집중' order by id desc; */
   Page<ProjectEntity> findByStatusOrderByIdDesc(String status, Pageable pageable);
   /* List<ProjectEntity> findByStatusOrderByIdDesc(String status);*/


    // 프로젝트 메뉴 - 활동 내역
/*    SELECT * FROM (
            SELECT id, title, content, reg_date FROM project_table WHERE user_id = 43
            UNION ALL
            SELECT id, NULL AS title, content, reg_date FROM project_comment_table WHERE user_id = 43
    )
    ORDER BY reg_date DESC*/
    @Query(value = "SELECT * FROM (\n" +
            "    SELECT id, title, content, reg_date FROM project_table WHERE user_id = :userId\n" +
            "    UNION ALL\n" +
            "    SELECT id, NULL AS title, content, reg_date FROM project_comment_table WHERE user_id = :userId\n" +
            ")\n" +
            "ORDER BY reg_date DESC", nativeQuery = true)
    List<Object[]> getProjectArticles(@Param("userId") Long userId);


    /* Top writers */
    @Query(value = "SELECT user_id, SUM(count) AS total_count\n" +
            "FROM (\n" +
            "    SELECT user_id, COUNT(*) AS count\n" +
            "    FROM project_table\n" +
            "    GROUP BY user_id\n" +
            "    UNION ALL\n" +
            "    SELECT user_id, COUNT(*) AS count\n" +
            "    FROM qna_table\n" +
            "    GROUP BY user_id\n" +
            ")\n" +
            "GROUP BY user_id\n" +
            "ORDER BY total_count DESC", nativeQuery = true)
    List<Object[]> getTopWriters();

    /* 프로젝트, 스터디 여부 확인  */

   /* select pt.* from project_study_category_link_table psc
    join project_table pt on psc.project_id = pt.id
    where project_study_id = 1
    order by pt.id desc*/
    @Query(value="select pt.* from project_study_category_link_table psc\n" +
            "join project_table pt on psc.project_id = pt.id\n" +
            "where project_study_id = :projectStudyId order by pt.id desc",
            countQuery = "select count(pt.id) from project_study_category_link_table psc\n" +
                    "join project_table pt on psc.project_id = pt.id\n" +
                    "where project_study_id = :projectStudyId", nativeQuery = true)
    Page<ProjectEntity> getProjectListByCategory(@Param("projectStudyId") Long projectStudyId, Pageable pageable);

    /* 프로젝트, 스터디 여부 확인  + 상태 */
   /* select pt.* from project_study_category_link_table psc
    join project_table pt on psc.project_id = pt.id
    where project_study_id = 1 and status = '모집중'
    order by pt.id desc*/
    @Query(value="select pt.* from project_study_category_link_table psc\n" +
            "join project_table pt on psc.project_id = pt.id\n" +
            "where project_study_id = :projectStudyId and status = :status order by pt.id desc",
            countQuery = "select count(pt.id) from project_study_category_link_table psc\n" +
                    "    join project_table pt on psc.project_id = pt.id\n" +
                    "    where project_study_id = :projectStudyId and status = :status\n" +
                    "    order by pt.id desc", nativeQuery = true)
    Page<ProjectEntity> getProjectListByCategoryAndStatus(@Param("projectStudyId") Long projectStudyId, String status, Pageable pageable);

/*    SELECT pt.*
  FROM project_table pt
  LEFT JOIN (
    SELECT pc.project_id, COUNT(pc.id) AS comment_count
    FROM project_comment_table pc
    GROUP BY pc.project_id
    ) comment_counts ON pt.id = comment_counts.project_id
        JOIN project_study_category_link_table psc ON psc.project_id = pt.id
    ORDER BY NVL(comment_counts.comment_count, 0) DESC
    */
    @Query(value = "  SELECT pt.*\n" +
            "  FROM project_table pt\n" +
            "  LEFT JOIN (\n" +
            "    SELECT pc.project_id, COUNT(pc.id) AS comment_count\n" +
            "    FROM project_comment_table pc\n" +
            "    GROUP BY pc.project_id\n" +
            "    ) comment_counts ON pt.id = comment_counts.project_id\n" +
            "        JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
            "    ORDER BY NVL(comment_counts.comment_count, 0) DESC",
            countQuery = " SELECT count(pt.id)\n" +
                    "    FROM project_table pt\n" +
                    "    LEFT JOIN (\n" +
                    "    SELECT pc.project_id, COUNT(pc.id) AS comment_count\n" +
                    "    FROM project_comment_table pc\n" +
                    "    GROUP BY pc.project_id\n" +
                    "    ) comment_counts ON pt.id = comment_counts.project_id\n" +
                    "        JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
                    "    ORDER BY NVL(comment_counts.comment_count, 0)", nativeQuery = true)
    /* 댓글 많은 순으로 조회 (전체 보기) */
    Page<ProjectEntity> getProjectListOrderByComment(Pageable pageable);

    @Query(value = " SELECT pt.*\n" +
            "  FROM project_table pt\n" +
            "  LEFT JOIN (\n" +
            "    SELECT pc.project_id, COUNT(pc.id) AS comment_count\n" +
            "    FROM project_comment_table pc\n" +
            "    GROUP BY pc.project_id\n" +
            "    ) comment_counts ON pt.id = comment_counts.project_id\n" +
            "        JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
            "    WHERE status = :status    \n" +
            "    ORDER BY NVL(comment_counts.comment_count, 0) DESC",
            countQuery = "   SELECT count(pt.id)\n" +
                    "    FROM project_table pt\n" +
                    "    LEFT JOIN (\n" +
                    "    SELECT pc.project_id, COUNT(pc.id) AS comment_count\n" +
                    "    FROM project_comment_table pc\n" +
                    "    GROUP BY pc.project_id\n" +
                    "    ) comment_counts ON pt.id = comment_counts.project_id\n" +
                    "        JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
                    "    WHERE status = :status  \n" +
                    "    ORDER BY NVL(comment_counts.comment_count, 0)", nativeQuery = true)
        /* 댓글 많은 순으로 조회 (전체 보기 + 상태포함) */
    Page<ProjectEntity> getProjectListOrderByCommentAndStatus(@Param("status") String status, Pageable pageable);

 /*   SELECT pt.*
  FROM project_table pt
  LEFT JOIN (
    SELECT pc.project_id, COUNT(pc.id) AS comment_count
    FROM project_comment_table pc
    GROUP BY pc.project_id
    ) comment_counts ON pt.id = comment_counts.project_id
        JOIN project_study_category_link_table psc ON psc.project_id = pt.id
    WHERE psc.project_study_id = 1
    ORDER BY NVL(comment_counts.comment_count, 0) DESC
    */
    @Query(value = "  SELECT pt.*\n" +
            "  FROM project_table pt\n" +
            "  LEFT JOIN (\n" +
            "    SELECT pc.project_id, COUNT(pc.id) AS comment_count\n" +
            "    FROM project_comment_table pc\n" +
            "    GROUP BY pc.project_id\n" +
            "    ) comment_counts ON pt.id = comment_counts.project_id\n" +
            "        JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
            "    WHERE psc.project_study_id = :projectStudyId\n" +
            "    ORDER BY NVL(comment_counts.comment_count, 0) DESC",
            countQuery = "SELECT count(pt.id)\n" +
                    "  FROM project_table pt\n" +
                    "  LEFT JOIN (\n" +
                    "    SELECT pc.project_id, COUNT(pc.id) AS comment_count\n" +
                    "    FROM project_comment_table pc\n" +
                    "    GROUP BY pc.project_id\n" +
                    "    ) comment_counts ON pt.id = comment_counts.project_id\n" +
                    "        JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
                    "    WHERE psc.project_study_id = :projectStudyId\n" +
                    "    ORDER BY NVL(comment_counts.comment_count, 0) DESC", nativeQuery = true)
    /* 댓글 많은 순으로 조회 (카테고리 포함) */
    Page<ProjectEntity> getProjectListOrderByCommentAndCategory(@Param("projectStudyId") Long projectStudyId, Pageable pageable);


/*    SELECT pt.*
    FROM project_table pt
    LEFT JOIN (
            SELECT pc.project_id, COUNT(pc.id) AS comment_count
    FROM project_comment_table pc
    GROUP BY pc.project_id
    ) comment_counts ON pt.id = comment_counts.project_id
    JOIN project_study_category_link_table psc ON psc.project_id = pt.id
    WHERE psc.project_study_id = 1 AND status = '모집중'
    ORDER BY NVL(comment_counts.comment_count, 0) DESC
    */
    @Query(value = "  SELECT pt.*\n" +
            "  FROM project_table pt\n" +
            "  LEFT JOIN (\n" +
            "    SELECT pc.project_id, COUNT(pc.id) AS comment_count\n" +
            "    FROM project_comment_table pc\n" +
            "    GROUP BY pc.project_id\n" +
            "    ) comment_counts ON pt.id = comment_counts.project_id\n" +
            "        JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
            "    WHERE psc.project_study_id = :projectStudyId AND status = :status\n" +
            "    ORDER BY NVL(comment_counts.comment_count, 0) DESC ",
            countQuery = " SELECT count(pt.id)\n" +
                    "    FROM project_table pt\n" +
                    "    LEFT JOIN (\n" +
                    "            SELECT pc.project_id, COUNT(pc.id) AS comment_count\n" +
                    "    FROM project_comment_table pc\n" +
                    "    GROUP BY pc.project_id\n" +
                    "    ) comment_counts ON pt.id = comment_counts.project_id\n" +
                    "    JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
                    "    WHERE psc.project_study_id = :projectStudyId AND status = :status\n" +
                    "    ORDER BY NVL(comment_counts.comment_count, 0) DESC", nativeQuery = true)
        /* 댓글 많은 순으로 조회 (카테고리 + 상태 포함) */
    Page<ProjectEntity> getProjectListOrderByCommentAndCategoryAndStatus(
            @Param("projectStudyId") Long projectStudyId, @Param("status") String status, Pageable pageable);


   /*
   SELECT pt.*
    FROM project_table pt
    LEFT JOIN (
        SELECT pc.project_id, COUNT(pc.id) AS clip_count
        FROM project_clip_table pc
        GROUP BY pc.project_id
    ) clip_counts ON pt.id = clip_counts.project_id
    ORDER BY NVL(clip_counts.clip_count, 0) DESC
    */
    @Query(value = " SELECT pt.*\n" +
            "    FROM project_table pt\n" +
            "    LEFT JOIN (\n" +
            "        SELECT pc.project_id, COUNT(pc.id) AS clip_count\n" +
            "        FROM project_clip_table pc\n" +
            "        GROUP BY pc.project_id\n" +
            "    ) clip_counts ON pt.id = clip_counts.project_id\n" +
            "    ORDER BY NVL(clip_counts.clip_count, 0) DESC",
            countQuery = " SELECT count(pt.id)\n" +
                    "    FROM project_table pt\n" +
                    "    LEFT JOIN (\n" +
                    "        SELECT pc.project_id, COUNT(pc.id) AS clip_count\n" +
                    "        FROM project_clip_table pc\n" +
                    "        GROUP BY pc.project_id\n" +
                    "    ) clip_counts ON pt.id = clip_counts.project_id\n" +
                    "    ORDER BY NVL(clip_counts.clip_count, 0) DESC", nativeQuery = true)
   /* 스크랩 순으로 조회 (전체보기) */
    Page<ProjectEntity> getProjectListOrderByClip(Pageable pageable);

    @Query(value = "SELECT pt.*\n" +
            "    FROM project_table pt\n" +
            "    LEFT JOIN (\n" +
            "        SELECT pc.project_id, COUNT(pc.id) AS clip_count\n" +
            "        FROM project_clip_table pc\n" +
            "        GROUP BY pc.project_id\n" +
            "    ) clip_counts ON pt.id = clip_counts.project_id\n" +
            "    WHERE status = :status\n" +
            "    ORDER BY NVL(clip_counts.clip_count, 0) DESC",
            countQuery = " SELECT count(pt.id)\n" +
                    "    FROM project_table pt\n" +
                    "    LEFT JOIN (\n" +
                    "        SELECT pc.project_id, COUNT(pc.id) AS clip_count\n" +
                    "        FROM project_clip_table pc\n" +
                    "        GROUP BY pc.project_id\n" +
                    "    ) clip_counts ON pt.id = clip_counts.project_id WHERE status = :status\n" +
                    "    ORDER BY NVL(clip_counts.clip_count, 0) DESC", nativeQuery = true)
        /* 스크랩 순으로 조회 (전체보기 + 상태) */
    Page<ProjectEntity> getProjectListOrderByClipAndStatus(@Param("status") String status, Pageable pageable);


  /*
  SELECT pt.*
    FROM project_table pt
    LEFT JOIN (
        SELECT pc.project_id, COUNT(pc.id) AS clip_count
        FROM project_clip_table pc
        GROUP BY pc.project_id
    ) clip_counts ON pt.id = clip_counts.project_id
    JOIN project_study_category_link_table psc ON psc.project_id = pt.id
    WHERE psc.project_study_id = 1
    ORDER BY NVL(clip_counts.clip_count, 0) DESC
*/
    @Query(value = "SELECT pt.*\n" +
            "    FROM project_table pt\n" +
            "    LEFT JOIN (\n" +
            "        SELECT pc.project_id, COUNT(pc.id) AS clip_count\n" +
            "        FROM project_clip_table pc\n" +
            "        GROUP BY pc.project_id\n" +
            "    ) clip_counts ON pt.id = clip_counts.project_id\n" +
            "    JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
            "    WHERE psc.project_study_id = :projectStudyId\n" +
            "    ORDER BY NVL(clip_counts.clip_count, 0) DESC ",
            countQuery = "SELECT count(pt.id)\n" +
                    "    FROM project_table pt\n" +
                    "    LEFT JOIN (\n" +
                    "        SELECT pc.project_id, COUNT(pc.id) AS clip_count\n" +
                    "        FROM project_clip_table pc\n" +
                    "        GROUP BY pc.project_id\n" +
                    "    ) clip_counts ON pt.id = clip_counts.project_id\n" +
                    "    JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
                    "    WHERE psc.project_study_id = :projectStudyId\n" +
                    "    ORDER BY NVL(clip_counts.clip_count, 0) DESC", nativeQuery = true)
    /* 스크랩 순으로 조회 (카테고리 포함) */
    Page<ProjectEntity> getProjectListOrderByClipAndCategory(@Param("projectStudyId") Long projectStudyId, Pageable pageable);

 /*
 SELECT pt.*
    FROM project_table pt
    LEFT JOIN (
            SELECT pc.project_id, COUNT(pc.id) AS clip_count
    FROM project_clip_table pc
    GROUP BY pc.project_id
    ) clip_counts ON pt.id = clip_counts.project_id
    JOIN project_study_category_link_table psc ON psc.project_id = pt.id
    WHERE psc.project_study_id = 1 AND status = '모집중'
    ORDER BY NVL(clip_counts.clip_count, 0) DESC
    */
    @Query(value = " SELECT pt.*\n" +
            "    FROM project_table pt\n" +
            "    LEFT JOIN (\n" +
            "        SELECT pc.project_id, COUNT(pc.id) AS clip_count\n" +
            "        FROM project_clip_table pc\n" +
            "        GROUP BY pc.project_id\n" +
            "    ) clip_counts ON pt.id = clip_counts.project_id\n" +
            "    JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
            "    WHERE psc.project_study_id = :projectStudyId AND status = :status\n" +
            "    ORDER BY NVL(clip_counts.clip_count, 0) DESC",
            countQuery = "SELECT count(pt.id)\n" +
                    "    FROM project_table pt\n" +
                    "    LEFT JOIN (\n" +
                    "            SELECT pc.project_id, COUNT(pc.id) AS clip_count\n" +
                    "    FROM project_clip_table pc\n" +
                    "    GROUP BY pc.project_id\n" +
                    "    ) clip_counts ON pt.id = clip_counts.project_id\n" +
                    "    JOIN project_study_category_link_table psc ON psc.project_id = pt.id\n" +
                    "    WHERE psc.project_study_id = :projectStudyId AND status = :status\n" +
                    "    ORDER BY NVL(clip_counts.clip_count, 0) DESC", nativeQuery = true)
        /* 스크랩 순으로 조회 (카테고리 + 상태 포함) */
    Page<ProjectEntity> getProjectListOrderByClipAndCategoryAndStatus(
            @Param("projectStudyId") Long projectStudyId, @Param("status") String status, Pageable pageable);





}

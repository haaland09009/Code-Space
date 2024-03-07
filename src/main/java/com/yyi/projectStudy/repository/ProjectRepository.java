package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectEntity;
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

    // 게시글 수정
    @Modifying
    @Query(value = "update ProjectEntity p set p.title = :title, p.content = :content, p.endDate = :endDate, p.headCount = :headCount where p.id = :id")
    void updateProject(@Param("title") String title, @Param("content") String content,
                       @Param("endDate") Date endDate, @Param("headCount") int headCount, @Param("id") Long id);


    // 랜덤 추출 3개
    @Query(value = "SELECT * FROM (SELECT * FROM project_table ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM <= 3", nativeQuery = true)
    List<ProjectEntity> findRandomProjects();

    // 메인 페이지 HOT 프로젝트 / 스터디 조회 (일단 조회수 순, 나중에 수정해야함!!!)
    @Query(value = "SELECT * FROM (SELECT * FROM project_table ORDER BY read_count DESC) WHERE ROWNUM <= 6", nativeQuery = true)
    List<ProjectEntity> findAllByOrderByReadCountDesc();



    // 프로젝트 메뉴 - 활동 내역
//    SELECT * FROM (
//            SELECT id, title, content, reg_date FROM project_table WHERE user_id = 43
//            UNION ALL
//            SELECT id, NULL AS title, content, reg_date FROM project_comment_table WHERE user_id = 43
//    )
//    ORDER BY reg_date DESC
    @Query(value = "SELECT * FROM (\n" +
            "    SELECT id, title, content, reg_date FROM project_table WHERE user_id = :userId\n" +
            "    UNION ALL\n" +
            "    SELECT id, NULL AS title, content, reg_date FROM project_comment_table WHERE user_id = :userId\n" +
            ")\n" +
            "ORDER BY reg_date DESC", nativeQuery = true)
    List<Object[]> getProjectArticles(@Param("userId") Long userId);


}

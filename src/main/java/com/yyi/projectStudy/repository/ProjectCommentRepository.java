package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectCommentEntity;
import com.yyi.projectStudy.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectCommentRepository extends JpaRepository<ProjectCommentEntity, Long> {

    // 게시글 당 댓글 조회
    // select * from project_comment_table where project_id = ? order by id desc;
    List<ProjectCommentEntity> findAllByProjectEntityOrderByIdDesc(ProjectEntity projectEntity);

    // 게시글 당 댓글 수 조회
    // select count(*) from project_comment_table where project_id = ?
    Long countByProjectEntity(ProjectEntity projectEntity);

    /* 댓글 수정하기 */
   /* update project_comment_table set content = ? where id = ?*/
    @Modifying
    @Query(value = "update ProjectCommentEntity p set p.content = :content, p.regDate = sysdate where p.id = :id")
    void updateComment(@Param("content") String content, @Param("id") Long id);
}

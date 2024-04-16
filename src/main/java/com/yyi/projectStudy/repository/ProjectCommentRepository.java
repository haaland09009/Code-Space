package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectCommentEntity;
import com.yyi.projectStudy.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectCommentRepository extends JpaRepository<ProjectCommentEntity, Long> {

    /* 게시글 당 댓글 조회 */
    List<ProjectCommentEntity> findAllByProjectEntityOrderByIdDesc(ProjectEntity projectEntity);

    /* 게시글 당 댓글 수 조회 */
    int countByProjectEntity(ProjectEntity projectEntity);

    /* 댓글 수정하기 */
    @Modifying
    @Query(value = "update ProjectCommentEntity p set p.content = :content, p.updDate = current_timestamp where p.id = :id")
    void updateComment(@Param("content") String content, @Param("id") Long id);
}

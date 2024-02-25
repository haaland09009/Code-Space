package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectCommentEntity;
import com.yyi.projectStudy.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectCommentRepository extends JpaRepository<ProjectCommentEntity, Long> {

    // 게시글 당 댓글 조회
    // select * from project_comment_table where project_id = ? order by id desc;
    List<ProjectCommentEntity> findAllByProjectEntityOrderByIdDesc(ProjectEntity projectEntity);

    // 게시글 당 댓글 수 조회
    // select count(*) from project_comment_table where project_id = ?
    Long countByProjectEntity(ProjectEntity projectEntity);
}

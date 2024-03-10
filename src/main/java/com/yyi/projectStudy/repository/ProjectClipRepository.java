package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectClipRepository extends JpaRepository<ProjectClipEntity, Long> {
    // SELECT COUNT(*) FROM project_clip_table WHERE project_id = ? AND user_id = ?;
    int countByProjectEntityAndUserEntity(ProjectEntity projectEntity, UserEntity userEntity);

    // SELECT * FROM project_clip_table WHERE project_id = ? AND user_id = ?;
    Optional<ProjectClipEntity> findByProjectEntityAndUserEntity(ProjectEntity projectEntity, UserEntity userEntity);

    // SELECT * FROM project_clip_table WHERE user_id = 43;
    List<ProjectClipEntity> findByUserEntityOrderByIdDesc(UserEntity userEntity);

    // SELECT * FROM project_clip_table WHERE project_id = ?;
    int countByProjectEntity(ProjectEntity projectEntity);
}
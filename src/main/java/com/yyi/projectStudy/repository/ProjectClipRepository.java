package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectClipRepository extends JpaRepository<ProjectClipEntity, Long> {
    /* 사용자 - 해당 게시물의 스크랩 수 조회 */
    int countByProjectEntityAndUserEntity(ProjectEntity projectEntity, UserEntity userEntity);

    /* 사용자 - 해당 게시물의 스크랩 여부 조회 */
    Optional<ProjectClipEntity> findByProjectEntityAndUserEntity(ProjectEntity projectEntity, UserEntity userEntity);

    /* 사용자가 스크랩한 게시물 목록 조회 */
    List<ProjectClipEntity> findByUserEntityOrderByIdDesc(UserEntity userEntity);

    /* 해당 게시물의 스크랩 수 조회 */
    int countByProjectEntity(ProjectEntity projectEntity);
}
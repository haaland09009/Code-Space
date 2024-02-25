package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectStudyCategoryEntity;
import com.yyi.projectStudy.entity.ProjectStudyCategoryLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectStudyCategoryLinkRepository extends JpaRepository<ProjectStudyCategoryLinkEntity, Long> {
    Optional<ProjectStudyCategoryLinkEntity> findByProjectEntity_Id(Long id);

    // 프로젝트 / 스터디 수정
    @Modifying
    @Query("UPDATE ProjectStudyCategoryLinkEntity p SET p.projectStudyCategoryEntity.id = :projectStudyId WHERE p.projectEntity.id = :projectId")
    void updateProjectStudy(@Param("projectStudyId") Long projectStudyId, @Param("projectId") Long projectId);
}

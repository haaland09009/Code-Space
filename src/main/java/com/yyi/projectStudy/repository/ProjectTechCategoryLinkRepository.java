package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectPositionCategoryLinkEntity;
import com.yyi.projectStudy.entity.ProjectTechCategoryLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectTechCategoryLinkRepository extends JpaRepository<ProjectTechCategoryLinkEntity, Long> {
    // !!! 나중에 리스트 형태로 수정해야함.
//    Optional<ProjectTechCategoryLinkEntity> findByProjectEntity_Id(Long id);
    List<ProjectTechCategoryLinkEntity> findByProjectEntity_Id(Long id);

    // 기술스택 수정
    @Modifying
    @Query("UPDATE ProjectTechCategoryLinkEntity p SET p.techCategoryEntity.id = :techId WHERE p.projectEntity.id = :projectId")
    void updateTech(@Param("techId") Long techId, @Param("projectId") Long projectId);
}

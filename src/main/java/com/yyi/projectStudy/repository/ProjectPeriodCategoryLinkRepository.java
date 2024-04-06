package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectPeriodCategoryLinkEntity;
import com.yyi.projectStudy.entity.ProjectStudyCategoryLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectPeriodCategoryLinkRepository extends JpaRepository<ProjectPeriodCategoryLinkEntity, Long> {
    Optional<ProjectPeriodCategoryLinkEntity> findByProjectEntity_Id(Long id);

    /* 진행 기간 수정 */
    @Modifying
    @Query("update ProjectPeriodCategoryLinkEntity p set p.periodCategoryEntity.id = :periodId where p.projectEntity.id = :projectId")
    void updatePeriod(@Param("periodId") Long periodId, @Param("projectId") Long projectId);

}

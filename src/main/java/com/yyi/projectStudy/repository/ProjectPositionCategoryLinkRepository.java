package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ProjectEntity;
import com.yyi.projectStudy.entity.ProjectPositionCategoryLinkEntity;
import com.yyi.projectStudy.entity.ProjectStudyCategoryLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectPositionCategoryLinkRepository extends JpaRepository<ProjectPositionCategoryLinkEntity, Long> {
    List<ProjectPositionCategoryLinkEntity> findByProjectEntity_Id(Long id);

    /* 포지션 삭제 */
    void deleteByProjectEntity(ProjectEntity projectEntity);

    /* 포지션 수정 */
    @Modifying
    @Query("update ProjectPositionCategoryLinkEntity p set p.positionCategoryEntity.id = :positionId where p.projectEntity.id = :projectId")
    void updatePosition(@Param("positionId") Long positionId, @Param("projectId") Long projectId);
}

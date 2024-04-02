package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.TechCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TechCategoryRepository extends JpaRepository<TechCategoryEntity, Long> {

    /* 기술스택 선택 리스트 조회 */
    @Query(value = "select t from TechCategoryEntity t where t.id in :techIdList")
    List<TechCategoryEntity> selectTechList(@Param("techIdList") List<Long> techIdList);
}

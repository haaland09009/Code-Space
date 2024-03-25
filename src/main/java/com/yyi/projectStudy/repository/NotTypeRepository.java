package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.NotTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotTypeRepository extends JpaRepository<NotTypeEntity, Long> {
    /* 알림 타입 pk 찾기 */
    Optional<NotTypeEntity> findByName(String name);
}

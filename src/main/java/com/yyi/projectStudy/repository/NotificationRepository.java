package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    // 회원 당 알림 조회
    List<NotificationEntity> findByUserEntity_idOrderByIdDesc(Long id);
}

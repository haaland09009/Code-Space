package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.UserEntity;
import com.yyi.projectStudy.entity.UserJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJobRepository extends JpaRepository<UserJobEntity, Long> {
    // 회원의 직군 조회
    Optional<UserJobEntity> findByUserEntity(UserEntity userEntity);
}

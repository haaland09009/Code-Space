package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 로그인 시 이메일 확인
    Optional<UserEntity> findByEmail(String email);
}

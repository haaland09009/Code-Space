package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.UserEntity;
import com.yyi.projectStudy.entity.UserJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJobRepository extends JpaRepository<UserJobEntity, Long> {
    // 회원의 직군 조회
    Optional<UserJobEntity> findByUserEntity(UserEntity userEntity);

    // 회원의 직군 수정
    // update user_job_table set job_id = ? where user_id = ?
    @Modifying
    @Query(value = "update UserJobEntity u set u.jobEntity.id = :jobId where u.userEntity.id = :userId")
    void updateUserJob(@Param("jobId") Long jobId,
                       @Param("userId") Long userId);
}

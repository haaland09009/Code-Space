package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.UserEntity;
import com.yyi.projectStudy.entity.UserImageFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserImageFileRepository extends JpaRepository<UserImageFileEntity, Long> {
    /* 이미지 존재 여부 확인 */
    Optional<UserImageFileEntity> findByUserEntity(UserEntity userEntity);

    /* 회원 이미지 수정 */
    /* update user_image_file_table set original_file_name = :originalFileName,
        stored_file_name = :storedFileName where user_id = ? */
    @Modifying
    @Query(value = "update UserImageFileEntity u set u.originalFileName = :originalFileName," +
            "u.storedFileName = :storedFileName where u.userEntity.id = :userId")
    void updateUserImg(@Param("originalFileName") String originalFileName,
                       @Param("storedFileName") String storedFileName,
                       @Param("userId") Long userId);


}

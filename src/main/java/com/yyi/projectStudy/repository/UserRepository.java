package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /* 로그인 시 이메일 확인 */
    Optional<UserEntity> findByEmail(String email);

    /* 회원정보 수정 */
    /* update user_table set nickname = ?, email = ? where id = ?; */
    @Modifying
    @Query(value = "update UserEntity u set nickname = :nickname, email = :email where id = :id")
    void updateUser(@Param("nickname") String nickname,
                    @Param("email") String email,
                    @Param("id") Long id);

    /* 이메일 중복 체크 */
    int countByEmail(String email);

    /* 닉네임 중복 체크 */
    int countByNickname(String nickname);
}

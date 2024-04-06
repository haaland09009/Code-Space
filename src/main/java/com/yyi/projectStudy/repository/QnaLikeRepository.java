package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QnaLikeRepository extends JpaRepository<QnaLikeEntity, Long> {
    /* 회원이 게시글에 좋어요를 눌렀는지 확인 */
    int countByQnaEntityAndUserEntity(QnaEntity qnaEntity, UserEntity userEntity);

    /* 게시글 좋아요 pk 확인 */
    Optional<QnaLikeEntity> findByQnaEntityAndUserEntity(QnaEntity qnaEntity, UserEntity userEntity);

    /* 게시글 좋아요 수 확인 */
    int countByQnaEntity(QnaEntity qnaEntity);

}

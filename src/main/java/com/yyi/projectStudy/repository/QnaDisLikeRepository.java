package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.QnaDisLikeEntity;
import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QnaDisLikeRepository extends JpaRepository<QnaDisLikeEntity, Long> {
    /* 회원이 게시글에 싫어요를 눌렀는지 확인 */
    int countByQnaEntityAndUserEntity(QnaEntity qnaEntity, UserEntity userEntity);

    /* 게시글 싫어요 pk 확인 */
    Optional<QnaDisLikeEntity> findByQnaEntityAndUserEntity(QnaEntity qnaEntity, UserEntity userEntity);

    /* 게시글 싫어요 수 확인 */
    int countByQnaEntity(QnaEntity qnaEntity);

}

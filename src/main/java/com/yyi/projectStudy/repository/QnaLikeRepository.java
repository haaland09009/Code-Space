package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QnaLikeRepository extends JpaRepository<QnaLikeEntity, Long> {
    // 회원이 게시글에 좋어요를 눌렀는지
    // select count(*) from qna_like_table where qna_id = ? and user_id = ?;
    int countByQnaEntityAndUserEntity(QnaEntity qnaEntity, UserEntity userEntity);

    // 게시글 좋아요 pk 확인
    // select * from qna_like_table where qna_id = ? and user_id = ?
    Optional<QnaLikeEntity> findByQnaEntityAndUserEntity(QnaEntity qnaEntity, UserEntity userEntity);

    // 게시글 좋아요 수 확인
    // select count(*) from qna_like_table where qna_id = ?
    int countByQnaEntity(QnaEntity qnaEntity);
}

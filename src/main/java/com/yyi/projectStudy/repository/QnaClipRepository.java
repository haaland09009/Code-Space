package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.QnaClipEntity;
import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QnaClipRepository extends JpaRepository<QnaClipEntity, Long> {
    // SELECT COUNT(*) FROM qna_clip_table WHERE qna_id = ? AND user_id = ?;
    int countByQnaEntityAndUserEntity(QnaEntity qnaEntity, UserEntity userEntity);

    // SELECT * FROM qna_clip_table WHERE qna_id = ? AND user_id = ?;
    Optional<QnaClipEntity> findByQnaEntityAndUserEntity(QnaEntity qnaEntity, UserEntity userEntity);

    // SELECT * FROM qna_clip_table WHERE user_id = 43;
    List<QnaClipEntity> findByUserEntityOrderByIdDesc(UserEntity userEntity);
}

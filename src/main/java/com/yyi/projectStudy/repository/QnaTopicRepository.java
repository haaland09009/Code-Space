package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QnaTopicRepository extends JpaRepository<QnaTopicEntity, Long> {
    // 게시판번호(qnaId)로 토픽번호(topicId) 조회
    // select * from qna_topic_table where qna_id = 1;
    Optional<QnaTopicEntity> findByQnaEntity(QnaEntity qnaEntity);
}

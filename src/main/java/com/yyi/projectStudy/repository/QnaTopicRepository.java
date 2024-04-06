package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaTopicEntity;
import com.yyi.projectStudy.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QnaTopicRepository extends JpaRepository<QnaTopicEntity, Long> {
    /* 게시판번호(qnaId)로 토픽번호(topicId) 조회 */
    Optional<QnaTopicEntity> findByQnaEntity(QnaEntity qnaEntity);

    /* 토픽번호로 게시판 조회 */
    List<QnaTopicEntity> findByTopicEntityOrderByQnaEntityIdDesc(TopicEntity topicEntity);


    /* 토픽 번호 수정 */
    @Modifying
    @Query(value = "update QnaTopicEntity q set q.topicEntity.id = :topicId where q.id = :id")
    void updateQnaTopic(@Param("topicId") Long topicId, @Param("id") Long id);
}

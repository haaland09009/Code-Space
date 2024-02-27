package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.QnaDTO;
import com.yyi.projectStudy.dto.QnaTopicDTO;
import com.yyi.projectStudy.dto.TopicDTO;
import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaTopicEntity;
import com.yyi.projectStudy.entity.TopicEntity;
import com.yyi.projectStudy.entity.UserEntity;
import com.yyi.projectStudy.repository.QnaRepository;
import com.yyi.projectStudy.repository.QnaTopicRepository;
import com.yyi.projectStudy.repository.TopicRepository;
import com.yyi.projectStudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaService {
    private final TopicRepository topicRepository;
    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;
    private final QnaTopicRepository qnaTopicRepository;

    // 토픽 종류 조회
    public List<TopicDTO> findAllTopic() {
        List<TopicEntity> topicEntityList = topicRepository.findAll();
        List<TopicDTO> topicDTOList = new ArrayList<>();
        for (TopicEntity topicEntity : topicEntityList) {
            topicDTOList.add(TopicDTO.toTopicDTO(topicEntity));
        }
        return topicDTOList;
    }

    // 게시글 작성
    public Long saveQna(QnaDTO qnaDTO) {
        UserEntity userEntity = userRepository.findById(qnaDTO.getUserId()).get();
        QnaEntity qnaEntity = QnaEntity.toQnaEntity(qnaDTO, userEntity);
        return qnaRepository.save(qnaEntity).getId();
    }

    // 게시글 조회
    @Transactional
    public QnaDTO findById(Long savedId) {
        QnaEntity qnaEntity = qnaRepository.findById(savedId).get();
        return QnaDTO.toQnaDTO(qnaEntity);
    }


    // 토픽 pk 조회
    public TopicDTO findByIdForTopic(Long topicId) {
        TopicEntity topicEntity = topicRepository.findById(topicId).get();
        return TopicDTO.toTopicDTO(topicEntity);
    }

    // QNA - TOPIC 저장
    public void saveQnaTopic(QnaDTO dto, TopicDTO topicDTO) {
        QnaEntity qnaEntity = qnaRepository.findById(dto.getId()).get();
        TopicEntity topicEntity = topicRepository.findById(topicDTO.getId()).get();
        QnaTopicEntity qnaTopicEntity = QnaTopicEntity.toQnaTopicEntity(qnaEntity, topicEntity);
        qnaTopicRepository.save(qnaTopicEntity);
    }

    // 기술, 커리어, 기타 모두 조회
    @Transactional
    public List<QnaDTO> findAll() {
        List<QnaEntity> qnaEntityList = qnaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<QnaDTO> qnaDTOList = new ArrayList<>();
        for (QnaEntity qnaEntity : qnaEntityList) {
            qnaDTOList.add(QnaDTO.toQnaDTO(qnaEntity));
        }
        return qnaDTOList;
    }


    // 게시판번호(qnaId)로 토픽 카테고리 조회
    public TopicDTO findTopic(Long id) {
        QnaEntity qnaEntity = qnaRepository.findById(id).get();
        QnaTopicEntity qnaTopicEntity = qnaTopicRepository.findByQnaEntity(qnaEntity).get();
        Long topicId = qnaTopicEntity.getTopicEntity().getId();
        TopicEntity topicEntity = topicRepository.findById(topicId).get();
        return TopicDTO.toTopicDTO(topicEntity);
    }


    // 게시글 삭제
    public void deleteById(Long id) {
        qnaRepository.deleteById(id);
    }

    // 조회수 증가
    @Transactional
    public void updateReadCount(Long id) {
        qnaRepository.updateReadCount(id);
    }


    // 게시글 수정
    @Transactional
    public QnaDTO updateQna(QnaDTO qnaDTO) {
        String content = qnaDTO.getContent();
        content = content.replaceAll("\r\n", "<br>");
        qnaDTO.setContent(content);

        qnaRepository.updateQna(qnaDTO.getTitle(), qnaDTO.getContent(), qnaDTO.getId());
        QnaEntity qnaEntity = qnaRepository.findById(qnaDTO.getId()).get();
        return QnaDTO.toQnaDTO(qnaEntity);
    }

    // 게시글 수정 - 토픽 카테고리
    @Transactional
    public TopicDTO updateQnaTopic(Long id, QnaTopicDTO qnaTopicDTO) {
        QnaEntity qnaEntity = qnaRepository.findById(id).get();
        QnaTopicEntity qnaTopicEntity = qnaTopicRepository.findByQnaEntity(qnaEntity).get();
        qnaTopicRepository.updateQnaTopic(qnaTopicDTO.getTopicId(), qnaTopicEntity.getId());
        TopicEntity topicEntity = topicRepository.findById(qnaTopicDTO.getTopicId()).get();
        return TopicDTO.toTopicDTO(topicEntity);
    }
}

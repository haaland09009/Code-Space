package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.entity.*;
import com.yyi.projectStudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaService {
    private final TopicRepository topicRepository;
    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;
    private final QnaTopicRepository qnaTopicRepository;
    private final QnaDisLikeRepository qnaDisLikeRepository;
    private final QnaLikeRepository qnaLikeRepository;

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

    // 본인이 작성한 게시글인지 확인
    public Long isYourQna(Long id) {
        Optional<QnaEntity> optionalQnaEntity = qnaRepository.findById(id);
        if (optionalQnaEntity.isPresent()) {
            QnaEntity qnaEntity = optionalQnaEntity.get();
            return qnaEntity.getUserEntity().getId();
        } else {
            return null;
        }
    }

    // 게시글 좋아요를 눌렀을 때 싫어요 여부 확인
    public int checkQnaDisLike(QnaDTO qnaDTO) {
        QnaEntity qnaEntity = qnaRepository.findById(qnaDTO.getId()).get();
        UserEntity userEntity = userRepository.findById(qnaDTO.getUserId()).get();
        return qnaDisLikeRepository.countByQnaEntityAndUserEntity(qnaEntity, userEntity);
    }

    // 게시글 좋아요 클릭
    public void like(QnaLikeDTO qnaLikeDTO) {
        Optional<QnaEntity> optionalQnaEntity = qnaRepository.findById(qnaLikeDTO.getQnaId());
        if (optionalQnaEntity.isPresent()) {
            QnaEntity qnaEntity = optionalQnaEntity.get();
            UserEntity userEntity = userRepository.findById(qnaLikeDTO.getUserId()).get();
            if (qnaLikeRepository.countByQnaEntityAndUserEntity(qnaEntity, userEntity) > 0) {
                Long id = qnaLikeRepository.findByQnaEntityAndUserEntity(qnaEntity, userEntity).get().getId();
                qnaLikeRepository.deleteById(id);
            } else {
                qnaLikeRepository.save(QnaLikeEntity.toQnaLikeEntity(qnaEntity, userEntity));
            }
        }
    }

    // 게시글 좋아요 수 확인
    public int likeCount(Long id) {
        QnaEntity qnaEntity = qnaRepository.findById(id).get();
        return qnaLikeRepository.countByQnaEntity(qnaEntity);
    }


    // 게시글 싫어요를 눌렀을 때 좋어요 여부 확인
    public int checkQnaLike(QnaDTO qnaDTO) {
        QnaEntity qnaEntity = qnaRepository.findById(qnaDTO.getId()).get();
        UserEntity userEntity = userRepository.findById(qnaDTO.getUserId()).get();
        return qnaLikeRepository.countByQnaEntityAndUserEntity(qnaEntity, userEntity);
    }


    // 게시글 싫어요 클릭
    public void disLike(QnaDisLikeDTO qnaDisLikeDTO) {
        Optional<QnaEntity> optionalQnaEntity = qnaRepository.findById(qnaDisLikeDTO.getQnaId());
        if (optionalQnaEntity.isPresent()) {
            QnaEntity qnaEntity = optionalQnaEntity.get();
            UserEntity userEntity = userRepository.findById(qnaDisLikeDTO.getUserId()).get();
            if (qnaDisLikeRepository.countByQnaEntityAndUserEntity(qnaEntity, userEntity) > 0) {
                Long id = qnaDisLikeRepository.findByQnaEntityAndUserEntity(qnaEntity, userEntity).get().getId();
                qnaDisLikeRepository.deleteById(id);
            } else {
                qnaDisLikeRepository.save(QnaDisLikeEntity.toQnaDisLikeEntity(qnaEntity, userEntity));
            }
        }
    }


    // 게시글 싫어요 수 확인
    public int disLikeCount(Long id) {
        QnaEntity qnaEntity = qnaRepository.findById(id).get();
        return qnaDisLikeRepository.countByQnaEntity(qnaEntity);
    }

    // 게시글에 좋아요를 눌렀는지
    public int checkQnaLikeForColor(Long qnaId, Long userId) {
        QnaEntity qnaEntity = qnaRepository.findById(qnaId).get();
        UserEntity userEntity = userRepository.findById(userId).get();
        return qnaLikeRepository.countByQnaEntityAndUserEntity(qnaEntity, userEntity);
    }

    // 게시글에 싫어요를 눌렀는지
    public int checkQnaDisLikeForColor(Long qnaId, Long userId) {
        QnaEntity qnaEntity = qnaRepository.findById(qnaId).get();
        UserEntity userEntity = userRepository.findById(userId).get();
        return qnaDisLikeRepository.countByQnaEntityAndUserEntity(qnaEntity, userEntity);
    }



}

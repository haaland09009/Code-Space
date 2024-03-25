package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.entity.*;
import com.yyi.projectStudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final QnaReplyRepository qnaReplyRepository;
    private final QnaReplyLikeRepository qnaReplyLikeRepository;
    private final QnaReplyCommentRepository qnaReplyCommentRepository;
    private final QnaClipRepository qnaClipRepository;
    private final QnaTagsRepository qnaTagsRepository;

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

    /* 기술, 커리어, 기타 모두 조회 */
    @Transactional
    public List<QnaDTO> findAll(String searchWord) {
        List<QnaEntity> qnaEntityList;
        if (searchWord != null) {
            searchWord = searchWord.toLowerCase();
            System.out.println("검색 단어 : " + searchWord);
            qnaEntityList = qnaRepository.findByTitleOrContent(searchWord);
        } else {
            qnaEntityList = qnaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }
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


    // 베스트 답변 찾기
    public List<QnaBestReplyDTO> findBestReplyList() {
        List<QnaReplyEntity> qnaReplyEntityList = qnaReplyRepository.findBestReply();
        // 위 쿼리 qna_id, id (reply_id) 추출
        List<QnaBestReplyDTO> bestReplyDTOlist = new ArrayList<>();
        for (QnaReplyEntity qnaReplyEntity : qnaReplyEntityList) {
            QnaBestReplyDTO qnaBestReplyDTO = new QnaBestReplyDTO();

            int likeCount = qnaReplyLikeRepository.countByQnaReplyEntity(qnaReplyEntity);
            // 답변 좋아요
            qnaBestReplyDTO.setLikeCount(likeCount);

            Long qnaId = qnaReplyEntity.getQnaEntity().getId();
            QnaEntity qnaEntity = qnaRepository.findById(qnaId).get();
            // id값
            qnaBestReplyDTO.setId(qnaId);
            // 제목
            qnaBestReplyDTO.setTitle(qnaEntity.getTitle());

            int replyCount = qnaReplyRepository.countByQnaEntity(qnaEntity);
            // 답변 수
            qnaBestReplyDTO.setReplyCount(replyCount);

            // 토픽
            QnaTopicEntity qnaTopicEntity = qnaTopicRepository.findByQnaEntity(qnaEntity).get();
            TopicEntity topicEntity = topicRepository.findById(qnaTopicEntity.getTopicEntity().getId()).get();
            qnaBestReplyDTO.setTopicName(topicEntity.getName());

            bestReplyDTOlist.add(qnaBestReplyDTO);
        }
        return bestReplyDTOlist;
    }

    // 베스트 질문
    @Transactional
    public List<QnaBestDTO> findBestQnaList() {
        // 좋아요 많은 순
        List<QnaEntity> qnaEntityList = qnaRepository.findBestLikeQna();
        List<QnaBestDTO> qnaBestDTOList = new ArrayList<>();
        for (QnaEntity qnaEntity : qnaEntityList) {
            QnaBestDTO qnaBestDTO = new QnaBestDTO();
            Long qnaId = qnaEntity.getId();

            // id
            qnaBestDTO.setId(qnaId);
            // 좋아요 수
            int likeCount = qnaLikeRepository.countByQnaEntity(qnaEntity);
            qnaBestDTO.setLikeCount(likeCount);

            // 답변 수
            int replyCount = qnaReplyRepository.countByQnaEntity(qnaEntity);
            qnaBestDTO.setReplyCount(replyCount);

            // 제목
            qnaBestDTO.setTitle(qnaEntity.getTitle());

            // 작성자
            qnaBestDTO.setWriter(qnaEntity.getUserEntity().getNickname());
            qnaBestDTO.setFileAttached(qnaEntity.getUserEntity().getFileAttached());

            if (qnaEntity.getUserEntity().getFileAttached() == 1) {
                qnaBestDTO.setStoredFileName(qnaEntity.getUserEntity().getUserImageFileEntityList().get(0).getStoredFileName());
            }

            // 토픽
            QnaTopicEntity qnaTopicEntity = qnaTopicRepository.findByQnaEntity(qnaEntity).get();
            TopicEntity topicEntity = topicRepository.findById(qnaTopicEntity.getTopicEntity().getId()).get();
            qnaBestDTO.setTopicName(topicEntity.getName());

            // 등록일
            qnaBestDTO.setRegDate(qnaEntity.getRegDate());


//            // 싫어요 수
//            int disLikeCount = qnaDisLikeRepository.countByQnaEntity(qnaEntity);
            qnaBestDTOList.add(qnaBestDTO);

        }
        return qnaBestDTOList;
    }


    // 기술, 커리어 조회
    @Transactional
    public List<QnaDTO> findAllByTopic(Long id) {
        TopicEntity topicEntity = topicRepository.findById(id).get();
        List<QnaTopicEntity> qnaTopicEntityList = qnaTopicRepository.findByTopicEntityOrderByQnaEntityIdDesc(topicEntity);
        List<QnaDTO> qnaDTOList = new ArrayList<>();
        for (QnaTopicEntity qnaTopicEntity : qnaTopicEntityList) {
            Long qnaId = qnaTopicEntity.getQnaEntity().getId();
            QnaEntity qnaEntity = qnaRepository.findById(qnaId).get();
            qnaDTOList.add(QnaDTO.toQnaDTO(qnaEntity));
        }
        return qnaDTOList;
    }

    // 랜덤 추출
    @Transactional
    public List<QnaDTO> randomQnaList(Long id) {
        List<QnaEntity> qnaEntityList = qnaRepository.randomQna(id);
        List<QnaDTO> qnaDTOList = new ArrayList<>();
        for (QnaEntity qnaEntity : qnaEntityList) {
            qnaDTOList.add(QnaDTO.toQnaDTO(qnaEntity));
        }
        return qnaDTOList;
    }


    // 활동내역
    @Transactional
    public List<QnaArticleDTO> findArticleList(Long userId) {
        List<Object[]> qnaArticleList = qnaRepository.getQnaArticles(userId);
        List<QnaArticleDTO> qnaArticleDTOList = new ArrayList<>();
        for (Object[] article : qnaArticleList) {
            QnaArticleDTO qnaArticleDTO = new QnaArticleDTO();
            BigDecimal qnaIdBigDecimal = (BigDecimal) article[0];
            BigDecimal replyIdBigDecimal = (BigDecimal) article[1];
            BigDecimal commentIdBigDecimal = (BigDecimal) article[2];

            Long qnaId = (qnaIdBigDecimal != null) ? qnaIdBigDecimal.longValue() : null;
            Long replyId = (replyIdBigDecimal != null) ? replyIdBigDecimal.longValue() : null;
            Long commentId = (commentIdBigDecimal != null) ? commentIdBigDecimal.longValue() : null;
//            Long qnaId = (Long) article[0];
//            Long replyId = (Long) article[1];
//            Long commentId = (Long) article[2];
            String title = (String) article[3];
            java.sql.Timestamp timestamp = (java.sql.Timestamp) article[4];
            LocalDateTime regDate = timestamp.toLocalDateTime();
            String content = (String) article[5];
            System.out.println("qnaId : " + qnaId + ", replyId : " + replyId + ", commentId : " + commentId);
            if (qnaId != null) {
                qnaArticleDTO.setQnaId(qnaId);
                qnaArticleDTO.setTitle(title);
                qnaArticleDTO.setRegDate(regDate);
                TopicDTO topicDTO = findTopic(qnaId);
                qnaArticleDTO.setCategoryName(topicDTO.getName());

                qnaArticleDTO.setBoardId(qnaId);
            } else if (replyId != null) {
                QnaReplyEntity qnaReplyEntity = qnaReplyRepository.findById(replyId).get();
                QnaEntity qnaEntity = qnaReplyEntity.getQnaEntity();

//                qnaArticleDTO.setQnaId(qnaEntity.getId());
                qnaArticleDTO.setReplyId(replyId);
                qnaArticleDTO.setTitle(qnaEntity.getTitle());
                qnaArticleDTO.setRegDate(regDate);
                qnaArticleDTO.setReplyContent(content);
                qnaArticleDTO.setBoardId(qnaEntity.getId());

                TopicDTO topicDTO = findTopic(qnaEntity.getId());
                qnaArticleDTO.setCategoryName(topicDTO.getName());
            } else if (commentId != null) {
                QnaReplyCommentEntity qnaReplyCommentEntity = qnaReplyCommentRepository.findById(commentId).get();
                QnaReplyEntity qnaReplyEntity = qnaReplyCommentEntity.getQnaReplyEntity();
                QnaEntity qnaEntity = qnaReplyEntity.getQnaEntity();

//                qnaArticleDTO.setQnaId(qnaEntity.getId());
                qnaArticleDTO.setCommentId(commentId);
//                qnaArticleDTO.setReplyId(qnaReplyCommentEntity.getQnaReplyEntity().getId());
                qnaArticleDTO.setTitle(qnaEntity.getTitle());
                qnaArticleDTO.setRegDate(regDate);
                qnaArticleDTO.setReplyContent(qnaReplyCommentEntity.getContent());
                qnaArticleDTO.setCommentContent(content);
                qnaArticleDTO.setBoardId(qnaEntity.getId());

                TopicDTO topicDTO = findTopic(qnaEntity.getId());
                qnaArticleDTO.setCategoryName(topicDTO.getName());
            }
            qnaArticleDTOList.add(qnaArticleDTO);
        }
        return qnaArticleDTOList;
    }


    // 게시물 스크랩
    public void clip(QnaClipDTO qnaClipDTO) {
        Optional<QnaEntity> optionalQnaEntity = qnaRepository.findById(qnaClipDTO.getQnaId());
        if (optionalQnaEntity.isPresent()) {
            QnaEntity qnaEntity = optionalQnaEntity.get();
            UserEntity userEntity = userRepository.findById(qnaClipDTO.getUserId()).get();
            int clipCount = qnaClipRepository.countByQnaEntityAndUserEntity(qnaEntity, userEntity);
            if (clipCount > 0) {
                QnaClipEntity qnaClipEntity = qnaClipRepository.findByQnaEntityAndUserEntity(qnaEntity, userEntity).get();
                qnaClipRepository.deleteById(qnaClipEntity.getId());
            } else if (clipCount == 0) {
                QnaClipEntity qnaClipEntity = QnaClipEntity.toQnaClipEntity(userEntity, qnaEntity);
                qnaClipRepository.save(qnaClipEntity);
            }
        }
    }


    // 게시물 스크랩 여부 확인
    public int checkClipYn(QnaClipDTO qnaClipDTO) {
        QnaEntity qnaEntity = qnaRepository.findById(qnaClipDTO.getQnaId()).get();
        UserEntity userEntity = userRepository.findById(qnaClipDTO.getUserId()).get();
        return qnaClipRepository.countByQnaEntityAndUserEntity(qnaEntity, userEntity);
    }

    // 스크랩 목록
    @Transactional
    public List<QnaClipDTO> getClipList(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();

        List<QnaClipDTO> qnaClipDTOList = new ArrayList<>();
        List<QnaClipEntity> qnaClipEntityList = qnaClipRepository.findByUserEntityOrderByIdDesc(userEntity);
        for (QnaClipEntity qnaClipEntity : qnaClipEntityList) {
            qnaClipDTOList.add(QnaClipDTO.toQnaClipDTO(qnaClipEntity));
        }
        return qnaClipDTOList;
    }

    // 해시 태그 추가
    @Transactional
    public void saveHashTag(QnaDTO dto, String tag) {
        if (findHashTag(dto.getId()) != null) {
            Long qnaId = dto.getId();
            qnaTagsRepository.updateTags(qnaId, tag);
        } else {
            QnaEntity qnaEntity = qnaRepository.findById(dto.getId()).get();
            QnaTagsEntity qnaTagsEntity = QnaTagsEntity.toQnaTagsEntity(qnaEntity, tag);
            qnaTagsRepository.save(qnaTagsEntity);
        }
    }


    // 해시 태그 조회
    @Transactional
    public QnaTagsDTO findHashTag(Long id) {
        QnaEntity qnaEntity = qnaRepository.findById(id).get();
        Optional<QnaTagsEntity> optionalQnaTagsEntity = qnaTagsRepository.findByQnaEntity(qnaEntity);
        if (optionalQnaTagsEntity.isPresent()) {
            return QnaTagsDTO.toQnaTagsDTO(optionalQnaTagsEntity.get());
        } else {
            return null;
        }
    }


    /* 답변 순으로 정렬 */
    @Transactional
    public List<QnaDTO> getQnaDTOListByReplySort(Long topicId) {
        List<Long> qnaIdList;
        if (topicId != null) {
            qnaIdList = qnaRepository.getQnaListSortByReplyAscAndTopic(topicId);
        } else {
            qnaIdList = qnaRepository.getQnaListSortByReplyAsc();
        }
        List<QnaDTO> qnaDTOList = new ArrayList<>();
        for (Long qnaId : qnaIdList) {
            QnaEntity qnaEntity = qnaRepository.findById(qnaId).get();
            qnaDTOList.add(QnaDTO.toQnaDTO(qnaEntity));
        }
        return qnaDTOList;
    }

   /* 답변이 없는 게시물 */
    @Transactional
    public List<QnaDTO> getNoRelyQnaList() {
        List<QnaDTO> qnaDTOList = new ArrayList<>();
        List<Long> qnaIdList = qnaRepository.getNoReplyQnaList();
        if (qnaIdList != null) {
            for (Long qnaId : qnaIdList) {
                Optional<QnaEntity> optionalQnaEntity = qnaRepository.findById(qnaId);
                if (optionalQnaEntity.isPresent()) {
                    qnaDTOList.add(QnaDTO.toQnaDTO(optionalQnaEntity.get()));
                }
            }
        } else {
            return null;
        }
        return qnaDTOList;
    }


}

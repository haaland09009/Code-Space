package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.QnaReplyDTO;
import com.yyi.projectStudy.dto.QnaReplyLikeDTO;
import com.yyi.projectStudy.entity.*;
import com.yyi.projectStudy.repository.QnaReplyLikeRepository;
import com.yyi.projectStudy.repository.QnaReplyRepository;
import com.yyi.projectStudy.repository.QnaRepository;
import com.yyi.projectStudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaReplyService {
    private final QnaReplyRepository qnaReplyRepository;
    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;
    private final QnaReplyLikeRepository qnaReplyLikeRepository;

    // 답변 작성
    public Long save(QnaReplyDTO qnaReplyDTO) {
        Optional<QnaEntity> optionalQnaEntity = qnaRepository.findById(qnaReplyDTO.getQnaId());
        if (optionalQnaEntity.isPresent()) {
            QnaEntity qnaEntity = optionalQnaEntity.get();
            UserEntity userEntity = userRepository.findById(qnaReplyDTO.getUserId()).get();
            QnaReplyEntity qnaReplyEntity = QnaReplyEntity.toQnaReplyEntity(qnaReplyDTO, qnaEntity, userEntity);
            return qnaReplyRepository.save(qnaReplyEntity).getId();
        } else {
            // 나중에 게시글이 존재하지 않습니다. 모달로 띄워야함.
            return null;
        }
    }


    // 게시글 당 답변 조회
    @Transactional
    public List<QnaReplyDTO> findAll(Long qnaId) {
        QnaEntity qnaEntity = qnaRepository.findById(qnaId).get();
        List<QnaReplyEntity> qnaReplyEntityList = qnaReplyRepository.findAllByQnaEntityOrderByIdDesc(qnaEntity);
        List<QnaReplyDTO> qnaReplyDTOList = new ArrayList<>();
        for (QnaReplyEntity qnaReplyEntity : qnaReplyEntityList) {
            qnaReplyDTOList.add(QnaReplyDTO.toQnaReplyDTO(qnaReplyEntity));
        }
        return qnaReplyDTOList;
    }

    // 답변 삭제
    public void deleteById(Long id) {
        qnaReplyRepository.deleteById(id);
    }


    // 답글 수 조회
    @Transactional
    public int count(Long id) {
        QnaEntity qnaEntity = qnaRepository.findById(id).get();
        return qnaReplyRepository.countByQnaEntity(qnaEntity);
    }

    // 답변 작성한 user 조회
    public Long isYourReply(Long id) {
        return qnaReplyRepository.findById(id).get().getUserEntity().getId();
    }

    // 답변 좋아요 클릭
    public void like(QnaReplyLikeDTO qnaReplyLikeDTO) {
        Optional<QnaReplyEntity> optionalQnaReplyEntity = qnaReplyRepository.findById(qnaReplyLikeDTO.getReplyId());
        if (optionalQnaReplyEntity.isPresent()) {
            QnaReplyEntity qnaReplyEntity = optionalQnaReplyEntity.get();
            UserEntity userEntity = userRepository.findById(qnaReplyLikeDTO.getUserId()).get();
            if (qnaReplyLikeRepository.countByQnaReplyEntityAndUserEntity(qnaReplyEntity, userEntity) > 0) {
                Long id = qnaReplyLikeRepository.findByQnaReplyEntityAndUserEntity(qnaReplyEntity, userEntity).get().getId();
                qnaReplyLikeRepository.deleteById(id);
            } else {
                qnaReplyLikeRepository.save(QnaReplyLikeEntity.toQnaReplyLikeEntity(qnaReplyEntity, userEntity));
            }
        }
    }


    // 답변 좋아요 수 업데이트
    public int likeCount(Long id) {
        QnaReplyEntity qnaReplyEntity = qnaReplyRepository.findById(id).get();
        return qnaReplyLikeRepository.countByQnaReplyEntity(qnaReplyEntity);
    }

    // 사용자가 답변에 좋아요를 눌렀는지 확인 (색깔 변경 목적)
    public int checkReplyLikeForColor(Long replyId, Long userId) {
        QnaReplyEntity qnaReplyEntity = qnaReplyRepository.findById(replyId).get();
        UserEntity userEntity = userRepository.findById(userId).get();
        return qnaReplyLikeRepository.countByQnaReplyEntityAndUserEntity(qnaReplyEntity, userEntity);
    }

    // 답변 하나 조회
    @Transactional
    public QnaReplyDTO findById(Long id) {
        Optional<QnaReplyEntity> optionalQnaReplyEntity = qnaReplyRepository.findById(id);
        if (optionalQnaReplyEntity.isPresent()) {
            return QnaReplyDTO.toQnaReplyDTO(optionalQnaReplyEntity.get());
        } else {
            return null;
        }
    }

    /* 베스트 답변 pk 조회 */
    public List<Long> getBestReplyList() {
        return qnaReplyRepository.bestReplyPkList();
    }

}

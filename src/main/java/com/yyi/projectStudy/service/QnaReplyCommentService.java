package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.QnaReplyCommentDTO;
import com.yyi.projectStudy.dto.QnaReplyDTO;
import com.yyi.projectStudy.entity.*;
import com.yyi.projectStudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaReplyCommentService {
    private final QnaReplyCommentRepository qnaReplyCommentRepository;
    private final QnaRepository qnaRepository;
    private final QnaReplyRepository qnaReplyRepository;
    private final UserRepository userRepository;
    private final NotTypeRepository notTypeRepository;
    private final NotificationRepository notificationRepository;

    // 댓글 작성
    public Long save(QnaReplyCommentDTO qnaReplyCommentDTO) {
        Optional<QnaReplyEntity> optionalQnaReplyEntity = qnaReplyRepository.findById(qnaReplyCommentDTO.getReplyId());
        if (optionalQnaReplyEntity.isPresent()) {
            QnaReplyEntity qnaReplyEntity = optionalQnaReplyEntity.get();
            UserEntity userEntity = userRepository.findById(qnaReplyCommentDTO.getUserId()).get();
            QnaReplyCommentEntity qnaReplyCommentEntity = QnaReplyCommentEntity.toQnaReplyCommentEntity(qnaReplyCommentDTO, qnaReplyEntity, userEntity);
            return qnaReplyCommentRepository.save(qnaReplyCommentEntity).getId();
        } else {
            return null;
        }
    }

    // 답변 당 댓글 조회
    @Transactional
    public List<QnaReplyCommentDTO> findAll(Long replyId) {
        QnaReplyEntity qnaReplyEntity = qnaReplyRepository.findById(replyId).get();
        List<QnaReplyCommentEntity> qnaReplyCommentEntityList =
                qnaReplyCommentRepository.findAllByQnaReplyEntityOrderByIdDesc(qnaReplyEntity);
        List<QnaReplyCommentDTO> qnaReplyCommentDTOList = new ArrayList<>();
        for (QnaReplyCommentEntity qnaReplyCommentEntity : qnaReplyCommentEntityList) {
            qnaReplyCommentDTOList.add(QnaReplyCommentDTO.toQnaReplyCommentDTO(qnaReplyCommentEntity));
        }
        return qnaReplyCommentDTOList;
    }

    // 어떤 답변에 댓글이 달린건지 확인 (답변 pk 추출)
    @Transactional
    public QnaReplyCommentDTO findById(Long id) {
        Optional<QnaReplyCommentEntity> optionalQnaReplyCommentEntity = qnaReplyCommentRepository.findById(id);
        if (optionalQnaReplyCommentEntity.isPresent()) {
            return QnaReplyCommentDTO.toQnaReplyCommentDTO(optionalQnaReplyCommentEntity.get());
        } else {
            return null;
        }
    }


    /* 댓글 삭제 */
    @Transactional
    public void deleteById(Long id) {
        /* 알림 삭제 */
        NotTypeEntity notTypeEntity = notTypeRepository.findByName("qna_reply_comment").get();
        Long notId = notTypeEntity.getId();
        notificationRepository.deleteNotification(notId, id);

        qnaReplyCommentRepository.deleteById(id);
    }

    // 답변에 달린 댓글 수
    public int commentCount(Long id) {
        QnaReplyEntity qnaReplyEntity = qnaReplyRepository.findById(id).get();
        return qnaReplyCommentRepository.countByQnaReplyEntity(qnaReplyEntity);
    }


   /* 댓글 수정하기 */
    @Transactional
    public QnaReplyCommentDTO updateComment(QnaReplyCommentDTO qnaReplyCommentDTO) {
        /* 댓글에 달린 답변 조회 */
        Optional<QnaReplyEntity> optionalQnaReplyEntity = qnaReplyRepository.findById(qnaReplyCommentDTO.getReplyId());
        if (optionalQnaReplyEntity.isPresent()) {
            QnaReplyEntity qnaReplyEntity = optionalQnaReplyEntity.get();
            /* 답변에 달린 게시글 조회 */
            Optional<QnaEntity> optionalQnaEntity = qnaRepository.findById(qnaReplyEntity.getQnaEntity().getId());
            if (optionalQnaEntity.isPresent()) {
                /* 댓글 수정처리 */
                qnaReplyCommentRepository.updateComment(qnaReplyCommentDTO.getContent(), qnaReplyCommentDTO.getId());
               /* 수정된 댓글 조회 */
               QnaReplyCommentEntity qnaReplyCommentEntity = qnaReplyCommentRepository.findById(qnaReplyCommentDTO.getId()).get();
               return QnaReplyCommentDTO.toQnaReplyCommentDTO(qnaReplyCommentEntity);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


}

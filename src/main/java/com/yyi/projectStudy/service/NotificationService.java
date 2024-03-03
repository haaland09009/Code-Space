package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.NotificationDTO;
import com.yyi.projectStudy.entity.*;
import com.yyi.projectStudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ProjectCommentRepository projectCommentRepository;
    private final QnaReplyRepository qnaReplyRepository;
    private final QnaReplyCommentRepository qnaReplyCommentRepository;

    // 알림 저장 - 댓글
    public void saveComment(NotificationDTO notificationDTO, Long commentId) {
        // 수신자 저장
        UserEntity userEntity = userRepository.findById(notificationDTO.getUserId()).get();
        // 댓글 저장
        ProjectCommentEntity projectCommentEntity = projectCommentRepository.findById(commentId).get();
        NotificationEntity notificationEntity = NotificationEntity.toCommentNotificationEntity(notificationDTO, userEntity, projectCommentEntity);


        notificationRepository.save(notificationEntity);
    }

    // 알림 저장 - qna 답변
    public void saveQnaReply(NotificationDTO notificationDTO, Long replyId) {
        // 수신자 저장
        UserEntity userEntity = userRepository.findById(notificationDTO.getUserId()).get();
        // 답변 저장
        QnaReplyEntity qnaReplyEntity = qnaReplyRepository.findById(replyId).get();
        NotificationEntity qnaReplyNotificationEntity = NotificationEntity.toQnaReplyNotificationEntity(notificationDTO, userEntity, qnaReplyEntity);
        notificationRepository.save(qnaReplyNotificationEntity);
    }

    // 알림 저장 - qna 답변에 대한 댓글
    public void saveQnaReplyComment(NotificationDTO notificationDTO, Long commentId) {
        UserEntity userEntity = userRepository.findById(notificationDTO.getUserId()).get();
        QnaReplyCommentEntity qnaReplyCommentEntity = qnaReplyCommentRepository.findById(commentId).get();
        NotificationEntity notificationEntity = NotificationEntity.toQnaReplyCommentNotificationEntity(notificationDTO, userEntity, qnaReplyCommentEntity);
        notificationRepository.save(notificationEntity);
    }



    // 알림 조회
    @Transactional
    public List<NotificationDTO> findAll(Long userId) {
        List<NotificationEntity> notificationEntityList = notificationRepository.findByUserEntity_idOrderByIdDesc(userId);
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (NotificationEntity notificationEntity : notificationEntityList) {
            notificationDTOList.add(NotificationDTO.toNotificationDTO(notificationEntity));
        }
        return notificationDTOList;
    }



}

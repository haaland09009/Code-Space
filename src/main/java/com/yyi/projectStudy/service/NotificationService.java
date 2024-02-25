package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.NotificationDTO;
import com.yyi.projectStudy.entity.NotificationEntity;
import com.yyi.projectStudy.entity.ProjectCommentEntity;
import com.yyi.projectStudy.entity.UserEntity;
import com.yyi.projectStudy.repository.NotificationRepository;
import com.yyi.projectStudy.repository.ProjectCommentRepository;
import com.yyi.projectStudy.repository.UserRepository;
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

    // 알림 저장 - 댓글
    public void saveComment(NotificationDTO notificationDTO, Long commentId) {
        // 수신자 저장
        UserEntity userEntity = userRepository.findById(notificationDTO.getUserId()).get();
        // 댓글 저장
        ProjectCommentEntity projectCommentEntity = projectCommentRepository.findById(commentId).get();
        NotificationEntity notificationEntity = NotificationEntity.toCommentNotificationEntity(notificationDTO, userEntity, projectCommentEntity);

        System.out.println("type = " + notificationEntity.getNotType());
        System.out.println("userEntity = " + notificationEntity.getUserEntity().getId());
        System.out.println("commenEnTITY= " + notificationEntity.getProjectCommentEntity().getId());

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

package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.NotificationDTO;
import com.yyi.projectStudy.dto.ProjectDTO;
import com.yyi.projectStudy.dto.QnaDTO;
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
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotTypeRepository notTypeRepository;
    private final UserRepository userRepository;
    private final ProjectStudyCategoryLinkRepository projectStudyCategoryLinkRepository;


    /* 회원 당 알림 조회 */
    @Transactional
    public List<NotificationDTO> findAll(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        List<NotificationEntity> notificationEntityList =
                notificationRepository.findByReceiverOrderByIdDesc(userEntity);
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (NotificationEntity notificationEntity : notificationEntityList) {
            notificationDTOList.add(NotificationDTO.toNotificationDTO(notificationEntity));
        }
        return notificationDTOList;
    }


    /* 프로젝트, 스터디 댓글 알림 넣기 */
    @Transactional
    public void saveProjectComment(NotificationDTO notificationDTO,
                                   ProjectDTO projectDTO) {
        /* 알림 타입 pk 찾기 */
        NotTypeEntity notTypeEntity = notTypeRepository.findByName("project_comment").get();

        /* 수신자 불러오기 */
        Long receiverId = notificationDTO.getReceiver();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(receiverId);
        UserEntity receiver = optionalUserEntity.get();

        /* 발신자 불러오기 */
        UserEntity sender = userRepository.findById(notificationDTO.getSender()).get();

        /* 프로젝트, 스터디 카테고리 불러오기 */
        ProjectStudyCategoryLinkEntity projectStudyCategoryLinkEntity =
                projectStudyCategoryLinkRepository.findByProjectEntity_Id(projectDTO.getId()).get();
        ProjectStudyCategoryEntity projectStudyCategoryEntity = projectStudyCategoryLinkEntity.getProjectStudyCategoryEntity();
        String categoryName = projectStudyCategoryEntity.getName();

        /* 내용 저장 */
        notificationDTO.setContent("님이 회원님의 " + categoryName + " 모집 게시물에 댓글을 작성했습니다.");
        NotificationEntity notificationEntity =
                NotificationEntity.toNotificationEntity(notificationDTO, notTypeEntity, receiver, sender);

        notificationRepository.save(notificationEntity);
    }


    /* 회원 당 안 읽은 알림 수 조회 */
    public int notReadCount(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        return notificationRepository.notReadNoticeCount(userEntity);
    }

    /* qna 답변 알림 넣기 */
    public void saveQnaReply(NotificationDTO notificationDTO) {
        /* 알림 타입 pk 찾기 */
        NotTypeEntity notTypeEntity = notTypeRepository.findByName("qna_reply").get();

        /* 수신자 불러오기 */
        Long receiverId = notificationDTO.getReceiver();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(receiverId);
        UserEntity receiver = optionalUserEntity.get();

        /* 발신자 불러오기 */
        UserEntity sender = userRepository.findById(notificationDTO.getSender()).get();

        /* 내용 저장 */
        notificationDTO.setContent("님이 회원님의 Q&A 게시물에 답변을 작성했습니다.");
        NotificationEntity notificationEntity =
                NotificationEntity.toNotificationEntity(notificationDTO, notTypeEntity, receiver, sender);

        notificationRepository.save(notificationEntity);
    }
}

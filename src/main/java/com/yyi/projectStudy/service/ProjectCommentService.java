package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.ProCmtDisLikeDTO;
import com.yyi.projectStudy.dto.ProCmtLikeDTO;
import com.yyi.projectStudy.dto.ProjectCommentDTO;
import com.yyi.projectStudy.dto.ProjectDTO;
import com.yyi.projectStudy.entity.*;
import com.yyi.projectStudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectCommentService {
    private final ProjectCommentRepository projectCommentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProCmtLikeRepository proCmtLikeRepository;
    private final ProCmtDisLikeRepository proCmtDisLikeRepository;
    private final NotTypeRepository notTypeRepository;
    private final NotificationRepository notificationRepository;

    /* 댓글 작성하기 */
    public Long save(ProjectCommentDTO projectCommentDTO) {
        Optional<ProjectEntity> optionalProjectEntity =
                projectRepository.findById(projectCommentDTO.getProjectId());
        if (optionalProjectEntity.isPresent()) {
            ProjectEntity projectEntity = optionalProjectEntity.get();
            UserEntity userEntity = userRepository.findById(projectCommentDTO.getUserId()).get();
            return projectCommentRepository.save(ProjectCommentEntity.toProjectCommentEntity(projectCommentDTO,
                    projectEntity, userEntity)).getId();
        } else {
            return null;
        }
    }

    /* 게시글 당 댓글 조회 */
    @Transactional
    public List<ProjectCommentDTO> findAll(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId).get();
        List<ProjectCommentEntity> projectCommentEntityList =
                projectCommentRepository.findAllByProjectEntityOrderByIdDesc(projectEntity);
        List<ProjectCommentDTO> projectCommentDTOList = new ArrayList<>();
        for (ProjectCommentEntity projectCommentEntity : projectCommentEntityList) {
            projectCommentDTOList.add(ProjectCommentDTO.toProjectCommentDTO(projectCommentEntity));
        }
        return projectCommentDTOList;
    }

    /* 게시글의 댓글 수 조회 */
    @Transactional
    public int count(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId).get();
        return projectCommentRepository.countByProjectEntity(projectEntity);
    }

    /* 댓글 삭제 */
    @Transactional
    public void deleteById(Long id) {
        /* 알림 삭제 */
        NotTypeEntity notTypeEntity = notTypeRepository.findByName("project_comment").get();
        Long notId = notTypeEntity.getId();
        notificationRepository.deleteNotification(notId, id);

        projectCommentRepository.deleteById(id);
    }

    /* 댓글 하나 조회 */
    @Transactional
    public ProjectCommentDTO findById(Long id) {
        Optional<ProjectCommentEntity> optionalProjectCommentEntity = projectCommentRepository.findById(id);
        if (optionalProjectCommentEntity.isPresent()) {
            ProjectCommentEntity projectCommentEntity = optionalProjectCommentEntity.get();
            return ProjectCommentDTO.toProjectCommentDTO(projectCommentEntity);
        } else {
            return null;
        }
    }

    /* 본인이 작성한 댓글인지 확인 */
    public Long isYourComment(Long id) {
        ProjectCommentEntity projectCommentEntity = projectCommentRepository.findById(id).get();
        return projectCommentEntity.getUserEntity().getId();
    }

    /* 댓글 좋아요 */
    public void commentLike(ProCmtLikeDTO proCmtLikeDTO) {
        Optional<ProjectCommentEntity> optionalProjectCommentEntity = projectCommentRepository.findById(proCmtLikeDTO.getCommentId());
        if (optionalProjectCommentEntity.isPresent()) {
            ProjectCommentEntity projectCommentEntity = optionalProjectCommentEntity.get();
            UserEntity userEntity = userRepository.findById(proCmtLikeDTO.getUserId()).get();
            if (proCmtLikeRepository.countByProjectCommentEntityAndUserEntity(projectCommentEntity, userEntity) > 0) {
                /* 댓글에 이미 좋아요를 누른 상태이면 취소 */
                Long id = proCmtLikeRepository.findByProjectCommentEntityAndUserEntity(projectCommentEntity, userEntity).get().getId();
                proCmtLikeRepository.deleteById(id);
            } else {
                /* 좋아요를 누르지 않았다면 좋아요 처리 */
                ProCmtLikeEntity proCmtLikeEntity = ProCmtLikeEntity.toProCmtLikeEntity(projectCommentEntity, userEntity);
                proCmtLikeRepository.save(proCmtLikeEntity);
            }
        }
    }

    /* 댓글 좋아요 수 확인 */
    public int commentLikeCount(Long id) {
        ProjectCommentEntity projectCommentEntity = projectCommentRepository.findById(id).get();
        return proCmtLikeRepository.countByProjectCommentEntity(projectCommentEntity);
    }

    /* 댓글 싫어요 */
    public void commentDisLike(ProCmtDisLikeDTO proCmtDisLikeDTO) {
        Optional<ProjectCommentEntity> optionalProjectCommentEntity = projectCommentRepository.findById(proCmtDisLikeDTO.getCommentId());
        if (optionalProjectCommentEntity.isPresent()) {
            ProjectCommentEntity projectCommentEntity = optionalProjectCommentEntity.get();
            UserEntity userEntity = userRepository.findById(proCmtDisLikeDTO.getUserId()).get();
            if (proCmtDisLikeRepository.countByProjectCommentEntityAndUserEntity(projectCommentEntity, userEntity) > 0) {
                /* 댓글에 이미 싫어요를 누른 상태이면 취소 */
                Long id = proCmtDisLikeRepository.findByProjectCommentEntityAndUserEntity(projectCommentEntity, userEntity).get().getId();
                proCmtDisLikeRepository.deleteById(id);
            } else {
                /* 싫어요를 누르지 않았다면 싫어요 처리 */
                ProCmtDisLikeEntity proCmtDisLikeEntity = ProCmtDisLikeEntity.toProCmtDisLikeEntity(projectCommentEntity, userEntity);
                proCmtDisLikeRepository.save(proCmtDisLikeEntity);
            }
        }
    }

    /* 댓글 싫어요 수 확인 */
    public int commentDisLikeCount(Long id) {
        ProjectCommentEntity projectCommentEntity = projectCommentRepository.findById(id).get();
        return proCmtDisLikeRepository.countByProjectCommentEntity(projectCommentEntity);
    }



    /* 댓글 좋아요를 눌렀을 때 싫어요 여부 확인 */
    public int checkCommentDisLike(ProjectCommentDTO projectCommentDTO) {
        ProjectCommentEntity projectCommentEntity = projectCommentRepository.findById(projectCommentDTO.getId()).get();
        UserEntity userEntity = userRepository.findById(projectCommentDTO.getUserId()).get();
        return proCmtDisLikeRepository.countByProjectCommentEntityAndUserEntity(projectCommentEntity, userEntity);
    }

    /* 댓글 싫어요를 눌렀을 때 좋아요 여부 확인 */
    public int checkCommentLike(ProjectCommentDTO projectCommentDTO) {
        ProjectCommentEntity projectCommentEntity = projectCommentRepository.findById(projectCommentDTO.getId()).get();
        UserEntity userEntity = userRepository.findById(projectCommentDTO.getUserId()).get();
        return proCmtLikeRepository.countByProjectCommentEntityAndUserEntity(projectCommentEntity, userEntity);
    }

    /* 댓글 수정하기 */
    @Transactional
    public void update(ProjectCommentDTO projectCommentDTO) {
        projectCommentRepository.updateComment(projectCommentDTO.getContent(), projectCommentDTO.getId());
    }
}


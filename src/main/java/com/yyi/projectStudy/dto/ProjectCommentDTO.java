package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.ProjectCommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectCommentDTO {
    private Long id;
    private Long projectId;
    private Long userId;
    private String content;
    private LocalDateTime regDate;

    private String writer;
    private int fileAttached;
    private String storedFileName;
    private int likeCount;
    private int disLikeCount;

    // Entity -> DTO
    public static ProjectCommentDTO toProjectCommentDTO(ProjectCommentEntity projectCommentEntity) {
        ProjectCommentDTO projectCommentDTO = new ProjectCommentDTO();
        projectCommentDTO.setId(projectCommentEntity.getId());
        projectCommentDTO.setProjectId(projectCommentEntity.getProjectEntity().getId());
        projectCommentDTO.setUserId(projectCommentEntity.getUserEntity().getId());
        projectCommentDTO.setContent(projectCommentEntity.getContent());
        projectCommentDTO.setRegDate(projectCommentEntity.getRegDate());

        projectCommentDTO.setWriter(projectCommentEntity.getUserEntity().getNickname());
        projectCommentDTO.setFileAttached(projectCommentEntity.getUserEntity().getFileAttached());
        if (projectCommentEntity.getUserEntity().getFileAttached() == 1) {
            projectCommentDTO.setStoredFileName(projectCommentEntity.getUserEntity().getUserImageFileEntityList().get(0).getStoredFileName());
        }
        return projectCommentDTO;
    }
}

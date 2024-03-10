package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.ProjectClipEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectClipDTO {
    private Long id;
    private Long userId;
    private Long projectId;
    private LocalDateTime regDate;

    private String projectStudy;
    private String title;

    public static ProjectClipDTO toProjectClipDTO(ProjectClipEntity projectClipEntity) {
        ProjectClipDTO projectClipDTO = new ProjectClipDTO();
        projectClipDTO.setId(projectClipEntity.getId());
        projectClipDTO.setUserId(projectClipEntity.getUserEntity().getId());
        projectClipDTO.setProjectId(projectClipEntity.getProjectEntity().getId());
        projectClipDTO.setRegDate(projectClipEntity.getRegDate());
        return projectClipDTO;
    }
}

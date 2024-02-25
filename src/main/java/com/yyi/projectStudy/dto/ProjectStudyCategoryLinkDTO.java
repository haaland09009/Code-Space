package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.ProjectStudyCategoryLinkEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectStudyCategoryLinkDTO {
    private Long id;
    private Long projectId;
    private Long projectStudyId;

    private String name;

    public static ProjectStudyCategoryLinkDTO toProjectStudyCategoryLinkDTO
            (ProjectStudyCategoryLinkEntity projectStudyCategoryLinkEntity) {
        ProjectStudyCategoryLinkDTO projectStudyCategoryLinkDTO
                = new ProjectStudyCategoryLinkDTO();
        projectStudyCategoryLinkDTO.setId(projectStudyCategoryLinkEntity.getId());
        projectStudyCategoryLinkDTO.setProjectId(projectStudyCategoryLinkEntity.getProjectEntity().getId());
        projectStudyCategoryLinkDTO.setProjectStudyId(projectStudyCategoryLinkEntity.getProjectStudyCategoryEntity().getId());

        projectStudyCategoryLinkDTO.setName(projectStudyCategoryLinkEntity.getProjectStudyCategoryEntity().getName());
        return projectStudyCategoryLinkDTO;
    }

}

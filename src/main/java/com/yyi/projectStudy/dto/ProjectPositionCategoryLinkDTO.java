package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.ProjectPositionCategoryLinkEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectPositionCategoryLinkDTO {
    private Long id;
    private Long projectId;
    private Long positionId;

    private String name;

    public static ProjectPositionCategoryLinkDTO toProjectPositionCategoryLinkDTO
            (ProjectPositionCategoryLinkEntity projectPositionCategoryLinkEntity) {
        ProjectPositionCategoryLinkDTO projectPositionCategoryLinkDTO =
                new ProjectPositionCategoryLinkDTO();
        projectPositionCategoryLinkDTO.setId(projectPositionCategoryLinkEntity.getId());
        projectPositionCategoryLinkDTO.setProjectId(projectPositionCategoryLinkEntity.getProjectEntity().getId());
        projectPositionCategoryLinkDTO.setPositionId(projectPositionCategoryLinkEntity.getPositionCategoryEntity().getId());

        projectPositionCategoryLinkDTO.setName(projectPositionCategoryLinkEntity.getPositionCategoryEntity().getName());
        return projectPositionCategoryLinkDTO;
    }

}

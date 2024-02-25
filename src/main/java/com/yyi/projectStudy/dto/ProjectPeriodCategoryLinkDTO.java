package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.ProjectPeriodCategoryLinkEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectPeriodCategoryLinkDTO {
    private Long id;
    private Long projectId;
    private Long periodId;

    private String name;

    public static ProjectPeriodCategoryLinkDTO toProjectPeriodCategoryLinkDTO
            (ProjectPeriodCategoryLinkEntity projectPeriodCategoryLinkEntity) {
        ProjectPeriodCategoryLinkDTO projectPeriodCategoryLinkDTO
                = new ProjectPeriodCategoryLinkDTO();
        projectPeriodCategoryLinkDTO.setId(projectPeriodCategoryLinkEntity.getId());
        projectPeriodCategoryLinkDTO.setProjectId(projectPeriodCategoryLinkEntity.getProjectEntity().getId());
        projectPeriodCategoryLinkDTO.setPeriodId(projectPeriodCategoryLinkEntity.getPeriodCategoryEntity().getId());

        projectPeriodCategoryLinkDTO.setName(projectPeriodCategoryLinkEntity.getPeriodCategoryEntity().getName());
        return projectPeriodCategoryLinkDTO;

    }

}

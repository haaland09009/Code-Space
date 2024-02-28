package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.ProjectTechCategoryLinkEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectTechCategoryLinkDTO {
    private Long id;
    private Long projectId;
    private Long techId;
//    private List<Long> techId;

    private String name;

//    public static ProjectTechCategoryLinkDTO toProjectTechCategoryLinkDTO
//            (ProjectTechCategoryLinkEntity projectTechCategoryLinkEntity)
//    {
//        ProjectTechCategoryLinkDTO projectTechCategoryLinkDTO
//                = new ProjectTechCategoryLinkDTO();
//        projectTechCategoryLinkDTO.setId(projectTechCategoryLinkEntity.getId());
//        projectTechCategoryLinkDTO.setProjectId(projectTechCategoryLinkEntity.getProjectEntity().getId());
//        projectTechCategoryLinkDTO.setTechId(projectTechCategoryLinkEntity.getTechCategoryEntity().getId());
//        projectTechCategoryLinkDTO.setName(projectTechCategoryLinkEntity.getTechCategoryEntity().getName());
//        return projectTechCategoryLinkDTO;
//    }
}

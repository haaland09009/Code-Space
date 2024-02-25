package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.ProjectStudyCategoryEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectStudyCategoryDTO {
    private Long id;
    private String name;

    public static ProjectStudyCategoryDTO toProjectStudyCategoryDTO(ProjectStudyCategoryEntity projectStudyCategoryEntity) {
        ProjectStudyCategoryDTO projectStudyCategoryDTO = new ProjectStudyCategoryDTO();
        projectStudyCategoryDTO.setId(projectStudyCategoryEntity.getId());
        projectStudyCategoryDTO.setName(projectStudyCategoryEntity.getName());
        return projectStudyCategoryDTO;
    }
}

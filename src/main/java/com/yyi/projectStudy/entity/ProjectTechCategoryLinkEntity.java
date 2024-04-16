package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.ProjectTechCategoryLinkDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "project_tech_category_link_table")
public class ProjectTechCategoryLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_id")
    private TechCategoryEntity techCategoryEntity;

    public static ProjectTechCategoryLinkEntity toProjectTechCategoryLinkEntity
            (
             ProjectEntity projectEntity,
             TechCategoryEntity techCategoryEntity)
    {
        ProjectTechCategoryLinkEntity projectTechCategoryLinkEntity =
                new ProjectTechCategoryLinkEntity();
//        projectTechCategoryLinkEntity.setId(projectTechCategoryLinkDTO.getId());
        projectTechCategoryLinkEntity.setProjectEntity(projectEntity);
        projectTechCategoryLinkEntity.setTechCategoryEntity(techCategoryEntity);
        return projectTechCategoryLinkEntity;
    }


}

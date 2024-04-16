package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.ProjectStudyCategoryLinkDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "project_study_category_link_table")
public class ProjectStudyCategoryLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_study_id")
    private ProjectStudyCategoryEntity projectStudyCategoryEntity;


    public static ProjectStudyCategoryLinkEntity toProjectStudyCategoryLinkEntity
            (ProjectStudyCategoryLinkDTO projectStudyCategoryLinkDTO,
             ProjectEntity projectEntity,
             ProjectStudyCategoryEntity projectStudyCategoryEntity) {
        ProjectStudyCategoryLinkEntity projectStudyCategoryLinkEntity
                = new ProjectStudyCategoryLinkEntity();
        projectStudyCategoryLinkEntity.setId(projectStudyCategoryLinkDTO.getId());
        projectStudyCategoryLinkEntity.setProjectEntity(projectEntity);
        projectStudyCategoryLinkEntity.setProjectStudyCategoryEntity(projectStudyCategoryEntity);
        return projectStudyCategoryLinkEntity;
    }

}

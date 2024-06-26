package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.ProjectPositionCategoryLinkDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "project_position_category_link_table")
public class ProjectPositionCategoryLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private PositionCategoryEntity positionCategoryEntity;

    public static ProjectPositionCategoryLinkEntity toProjectPositionCategoryLinkEntity(
             ProjectEntity projectEntity,
             PositionCategoryEntity positionCategoryEntity) {
        ProjectPositionCategoryLinkEntity projectPositionCategoryLinkEntity
                = new ProjectPositionCategoryLinkEntity();
        projectPositionCategoryLinkEntity.setProjectEntity(projectEntity);
        projectPositionCategoryLinkEntity.setPositionCategoryEntity(positionCategoryEntity);
        return projectPositionCategoryLinkEntity;
    }

}

package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.ProjectPeriodCategoryLinkDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "project_period_category_link_seq_generator"
        , sequenceName = "project_period_category_link_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "project_period_category_link_table")
public class ProjectPeriodCategoryLinkEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "project_period_category_link_seq_generator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private PeriodCategoryEntity periodCategoryEntity;

    public static ProjectPeriodCategoryLinkEntity
                    toProjectPeriodCategoryLinkEntity(ProjectPeriodCategoryLinkDTO projectPeriodCategoryLinkDTO,
                                                      ProjectEntity projectEntity,
                                                      PeriodCategoryEntity periodCategoryEntity
                                                      ) {
        ProjectPeriodCategoryLinkEntity projectPeriodCategoryLinkEntity =
                new ProjectPeriodCategoryLinkEntity();

        projectPeriodCategoryLinkEntity.setId(projectPeriodCategoryLinkDTO.getId());
        projectPeriodCategoryLinkEntity.setProjectEntity(projectEntity);
        projectPeriodCategoryLinkEntity.setPeriodCategoryEntity(periodCategoryEntity);
        return projectPeriodCategoryLinkEntity;
    }

}

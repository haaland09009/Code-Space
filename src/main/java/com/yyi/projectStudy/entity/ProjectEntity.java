package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.ProjectDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "project_seq_generator"
        , sequenceName = "project_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "project_table")
public class ProjectEntity extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "project_seq_generator"
    )
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int readCount;

    @Column(nullable = false)
    private int headCount;

    @Column(nullable = false)
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectCommentEntity> projectCommentEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectStudyCategoryLinkEntity> projectStudyCategoryLinkEntityList
            = new ArrayList<>();

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectTechCategoryLinkEntity> projectTechCategoryLinkEntityList
            = new ArrayList<>();

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectPositionCategoryLinkEntity> projectPositionCategoryLinkEntityList
            = new ArrayList<>();

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectPeriodCategoryLinkEntity> projectPeriodCategoryLinkEntityList
            = new ArrayList<>();


    public static ProjectEntity toProjectEntity(ProjectDTO projectDTO, UserEntity userEntity) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectDTO.getId());
        projectEntity.setTitle(projectDTO.getTitle());
        projectEntity.setContent(projectDTO.getContent());
        projectEntity.setHeadCount(projectDTO.getHeadCount()); //
        projectEntity.setEndDate(projectDTO.getEndDate()); //
        projectEntity.setReadCount(0);

        projectEntity.setUserEntity(userEntity);
        return projectEntity;
    }


}

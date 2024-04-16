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
@Table(name = "project_table")
public class ProjectEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(nullable = false)
    private int readCount;

    @Column(nullable = false)
    private int headCount;

    @Column(nullable = false)
    private Date startDate;

    @Column
    private String status;

    @Column
    private LocalDateTime updDate;

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

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<ProjectClipEntity> projectClipEntityList = new ArrayList<>();


    public static ProjectEntity toProjectEntity(ProjectDTO projectDTO, UserEntity userEntity) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectDTO.getId());
        projectEntity.setTitle(projectDTO.getTitle());
        projectEntity.setContent(projectDTO.getContent());
        projectEntity.setHeadCount(projectDTO.getHeadCount());
        projectEntity.setStartDate(projectDTO.getStartDate());
        projectEntity.setStatus("모집중");
        projectEntity.setReadCount(0);

        projectEntity.setUserEntity(userEntity);
        return projectEntity;
    }


}

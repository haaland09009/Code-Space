package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.ProjectCommentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "project_comment_seq_generator"
        , sequenceName = "project_comment_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "project_comment_table")
public class ProjectCommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "project_comment_seq_generator"
    )
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column
    private LocalDateTime updDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "projectCommentEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProCmtLikeEntity> proCmtLikeEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "projectCommentEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProCmtDisLikeEntity> proCmtDisLikeEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "projectCommentEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<NotificationEntity> notificationEntityList = new ArrayList<>();

    // DTO -> Entity
    public static ProjectCommentEntity toProjectCommentEntity(ProjectCommentDTO projectCommentDTO,
                                                              ProjectEntity projectEntity,
                                                              UserEntity userEntity) {
        ProjectCommentEntity projectCommentEntity = new ProjectCommentEntity();
        projectCommentEntity.setId(projectCommentDTO.getId());
        projectCommentEntity.setContent(projectCommentDTO.getContent());
        projectCommentEntity.setProjectEntity(projectEntity);
        projectCommentEntity.setUserEntity(userEntity);
        return projectCommentEntity;
    }

}

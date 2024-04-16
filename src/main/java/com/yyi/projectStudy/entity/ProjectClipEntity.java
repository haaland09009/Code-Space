package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "project_clip_table")
public class ProjectClipEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;


    public static ProjectClipEntity toProjectClipEntity(UserEntity userEntity, ProjectEntity projectEntity) {
        ProjectClipEntity projectClipEntity = new ProjectClipEntity();
        projectClipEntity.setUserEntity(userEntity);
        projectClipEntity.setProjectEntity(projectEntity);
        return projectClipEntity;
    }
}

package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "project_study_category_seq_generator"
        , sequenceName = "project_study_category_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "project_study_category_table")
public class ProjectStudyCategoryEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "project_study_category_seq_generator"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "projectStudyCategoryEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectStudyCategoryLinkEntity> projectStudyCategoryLinkEntityList
            = new ArrayList<>();

}

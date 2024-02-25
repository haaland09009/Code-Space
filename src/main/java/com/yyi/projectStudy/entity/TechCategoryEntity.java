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
        name = "tech_category_seq_generator"
        , sequenceName = "tech_category_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "tech_category_table")
public class TechCategoryEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "tech_category_seq_generator"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "techCategoryEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectTechCategoryLinkEntity> projectTechCategoryLinkEntityList
            = new ArrayList<>();

}

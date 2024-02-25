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
        name = "position_category_seq_generator"
        , sequenceName = "position_category_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "position_category_table")
public class PositionCategoryEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "position_category_seq_generator"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "positionCategoryEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectPositionCategoryLinkEntity> projectPositionCategoryLinkEntityList
            = new ArrayList<>();
}

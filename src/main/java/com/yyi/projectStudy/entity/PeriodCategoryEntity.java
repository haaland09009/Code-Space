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
        name = "period_category_seq_generator"
        , sequenceName = "period_category_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "period_category_table")
public class PeriodCategoryEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "period_category_seq_generator"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "periodCategoryEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectPeriodCategoryLinkEntity> projectPeriodCategoryLinkEntityList
            = new ArrayList<>();
}

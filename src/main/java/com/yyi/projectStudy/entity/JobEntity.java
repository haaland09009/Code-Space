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
        name = "job_seq_generator"
        , sequenceName = "job_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "job_table")
public class JobEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "job_seq_generator"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "jobEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserJobEntity> userJobEntityList
            = new ArrayList<>();


}

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
        name = "not_type_seq_generator"
        , sequenceName = "not_type_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "not_type_table")
public class NotTypeEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "not_type_seq_generator"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "notTypeEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<NotificationEntity> notificationEntityList = new ArrayList<>();

}

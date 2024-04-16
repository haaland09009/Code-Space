package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.LoungeDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "lounge_table")
public class LoungeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(nullable = false, length = 4000)
    private String content;

    @OneToMany(mappedBy = "loungeEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LoungeLikeEntity> loungeLikeEntityList
            = new ArrayList<>();

    public static LoungeEntity toLoungeEntity(LoungeDTO loungeDTO, UserEntity userEntity) {
        LoungeEntity loungeEntity = new LoungeEntity();
        loungeEntity.setUserEntity(userEntity);
        loungeEntity.setContent(loungeDTO.getContent());
        return loungeEntity;
    }
}

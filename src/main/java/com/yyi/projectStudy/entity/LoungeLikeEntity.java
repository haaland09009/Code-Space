package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "lounge_like_seq_generator"
        , sequenceName = "lounge_like_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "lounge_like_table")
public class LoungeLikeEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "lounge_like_seq_generator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lounge_id")
    private LoungeEntity loungeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public static LoungeLikeEntity toLoungeLikeEntity(LoungeEntity loungeEntity, UserEntity userEntity) {
        LoungeLikeEntity loungeLikeEntity = new LoungeLikeEntity();
        loungeLikeEntity.setLoungeEntity(loungeEntity);
        loungeLikeEntity.setUserEntity(userEntity);
        return loungeLikeEntity;
    }
}

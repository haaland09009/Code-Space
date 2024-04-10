package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.LoungeDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "lounge_seq_generator"
        , sequenceName = "lounge_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "lounge_table")
public class LoungeEntity extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "lounge_seq_generator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(nullable = false, length = 4000)
    private String content;

    public static LoungeEntity toLoungeEntity(LoungeDTO loungeDTO, UserEntity userEntity) {
        LoungeEntity loungeEntity = new LoungeEntity();
        loungeEntity.setUserEntity(userEntity);
        loungeEntity.setContent(loungeDTO.getContent());
        return loungeEntity;
    }
}

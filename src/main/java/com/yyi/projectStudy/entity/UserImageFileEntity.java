package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "user_image_file_seq_generator"
        , sequenceName = "user_image_file_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "user_image_file_table")
public class UserImageFileEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "user_image_file_seq_generator"
    )
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public static UserImageFileEntity toUserImageFileEntity(UserEntity userEntity,
                                                            String originalFileName,
                                                            String storedFileName) {
        UserImageFileEntity userImageFileEntity = new UserImageFileEntity();
        userImageFileEntity.setOriginalFileName(originalFileName);
        userImageFileEntity.setStoredFileName(storedFileName);
        userImageFileEntity.setUserEntity(userEntity);
        return userImageFileEntity;
    }
}

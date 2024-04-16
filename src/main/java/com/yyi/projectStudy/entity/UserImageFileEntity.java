package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_image_file_table")
public class UserImageFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

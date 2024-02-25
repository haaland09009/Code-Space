package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "user_seq_generator"
        , sequenceName = "user_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "user_table")
public class UserEntity1 extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "user_seq_generator"
    )
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;


    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectEntity> projectEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectCommentEntity> projectCommentEntityList = new ArrayList<>();

    // DTO -> Entity
    public static UserEntity1 toUserEntity(UserDTO userDTO) {
        UserEntity1 userEntity = new UserEntity1();
        userEntity.setId(userDTO.getId());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setNickname(userDTO.getNickname());
        return userEntity;
    }


}

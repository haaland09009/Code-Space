package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.UserJobDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_job_table")
public class UserJobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private JobEntity jobEntity;


    public static UserJobEntity toUserJobEntity(UserEntity userEntity, JobEntity jobEntity) {
        UserJobEntity userJobEntity = new UserJobEntity();
        userJobEntity.setUserEntity(userEntity);
        userJobEntity.setJobEntity(jobEntity);
        return userJobEntity;
    }
}

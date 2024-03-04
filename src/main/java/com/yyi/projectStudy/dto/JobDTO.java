package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.JobEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobDTO {
    private Long id;
    private String name;

    public static JobDTO toJobDTO(JobEntity jobEntity) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(jobEntity.getId());
        jobDTO.setName(jobEntity.getName());
        return jobDTO;
    }


}

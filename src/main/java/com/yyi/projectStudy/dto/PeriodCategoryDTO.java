package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.PeriodCategoryEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PeriodCategoryDTO {
    private Long id;
    private String name;

    public static PeriodCategoryDTO toPeriodCategoryDTO(PeriodCategoryEntity periodCategoryEntity) {
        PeriodCategoryDTO periodCategoryDTO = new PeriodCategoryDTO();
        periodCategoryDTO.setId(periodCategoryEntity.getId());
        periodCategoryDTO.setName(periodCategoryEntity.getName());
        return periodCategoryDTO;
    }
}

package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.PositionCategoryEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PositionCategoryDTO {
    private Long id;
    private String name;

    public static PositionCategoryDTO toPositionCategoryDTO(PositionCategoryEntity positionCategoryEntity) {
        PositionCategoryDTO positionCategoryDTO = new PositionCategoryDTO();
        positionCategoryDTO.setId(positionCategoryEntity.getId());
        positionCategoryDTO.setName(positionCategoryEntity.getName());
        return positionCategoryDTO;
    }
}

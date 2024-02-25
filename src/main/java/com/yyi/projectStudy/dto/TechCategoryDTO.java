package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.TechCategoryEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TechCategoryDTO {
    private Long id;
    private String name;

    public static TechCategoryDTO toTechCategoryDTO(TechCategoryEntity techCategoryEntity) {
        TechCategoryDTO techCategoryDTO = new TechCategoryDTO();
        techCategoryDTO.setId(techCategoryEntity.getId());
        techCategoryDTO.setName(techCategoryEntity.getName());
        return techCategoryDTO;
    }
}

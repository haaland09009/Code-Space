package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.TechCategoryDTO;
import com.yyi.projectStudy.entity.TechCategoryEntity;
import com.yyi.projectStudy.repository.TechCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechService {
    private final TechCategoryRepository techCategoryRepository;

    /* 기술스택 선택 리스트 조회 */
    @Transactional
    public List<TechCategoryDTO> selectTechList(List<Long> techIdList) {
        List<TechCategoryEntity> techCategoryEntityList = techCategoryRepository.selectTechList(techIdList);
        List<TechCategoryDTO> techCategoryDTOList = new ArrayList<>();
        for (TechCategoryEntity techCategoryEntity : techCategoryEntityList) {
            techCategoryDTOList.add(TechCategoryDTO.toTechCategoryDTO(techCategoryEntity));
        }
        return techCategoryDTOList;
    }
}

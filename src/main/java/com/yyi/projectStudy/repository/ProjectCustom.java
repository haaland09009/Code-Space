package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.dto.ProjectDTO;

import java.util.List;

public interface ProjectCustom {
    List<ProjectDTO> findByCondition(List<Long> techList, Long positionId, String status);
}

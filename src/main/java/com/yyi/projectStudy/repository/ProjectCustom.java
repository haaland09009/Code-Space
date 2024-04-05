package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.dto.ProjectDTO;
import com.yyi.projectStudy.dto.ProjectSearchDTO;

import java.util.List;

public interface ProjectCustom {
    List<ProjectDTO> findByCondition(ProjectSearchDTO projectSearchDTO);
}

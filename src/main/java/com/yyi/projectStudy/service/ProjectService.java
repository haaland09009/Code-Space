package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.entity.*;
import com.yyi.projectStudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final PeriodCategoryRepository periodCategoryRepository;
    private final PositionCategoryRepository positionCategoryRepository;
    private final ProjectStudyCategoryRepository projectStudyCategoryRepository;
    private final TechCategoryRepository techCategoryRepository;

    private final ProjectPositionCategoryLinkRepository projectPositionCategoryLinkRepository;
    private final ProjectPeriodCategoryLinkRepository projectPeriodCategoryLinkRepository;
    private final ProjectStudyCategoryLinkRepository projectStudyCategoryLinkRepository;
    private final ProjectTechCategoryLinkRepository projectTechCategoryLinkRepository;


//    // 게시글 작성
//    public void save(ProjectDTO projectDTO) {
//        UserEntity userEntity = userRepository.findById(projectDTO.getUserId()).get();
//        projectRepository.save(ProjectEntity.toProjectEntity(projectDTO, userEntity));
//    }

    // 진행기간 카테고리 조회
    public List<PeriodCategoryDTO> findAllPeriodCategoryDTOList() {
        List<PeriodCategoryEntity> periodCategoryEntityList
                = periodCategoryRepository.findAll();
        List<PeriodCategoryDTO> periodCategoryDTOList = new ArrayList<>();
        for (PeriodCategoryEntity periodCategoryEntity : periodCategoryEntityList) {
            periodCategoryDTOList.add(PeriodCategoryDTO.toPeriodCategoryDTO(periodCategoryEntity));
        }
        return periodCategoryDTOList;
    }

    // 포지션 카테고리 조회
    public List<PositionCategoryDTO> findAllPositionCategoryDTOList() {
        List<PositionCategoryEntity> positionCategoryEntityList = positionCategoryRepository.findAll();
        List<PositionCategoryDTO> positionCategoryDTOList = new ArrayList<>();
        for (PositionCategoryEntity positionCategoryEntity : positionCategoryEntityList) {
            positionCategoryDTOList.add(PositionCategoryDTO.toPositionCategoryDTO(positionCategoryEntity));
        }
        return positionCategoryDTOList;
    }

    // 프로젝트 / 스터디 카테고리 조회
    public List<ProjectStudyCategoryDTO> findAllProjectStudyCategoryDTOList() {
        List<ProjectStudyCategoryEntity> projectStudyCategoryEntityList = projectStudyCategoryRepository.findAll();
        List<ProjectStudyCategoryDTO> projectStudyCategoryDTOList = new ArrayList<>();
        for (ProjectStudyCategoryEntity projectStudyCategoryEntity : projectStudyCategoryEntityList) {
            projectStudyCategoryDTOList.add(ProjectStudyCategoryDTO.toProjectStudyCategoryDTO(projectStudyCategoryEntity));
        }
        return projectStudyCategoryDTOList;
    }

    // 기술스택 카테고리 조회
    public List<TechCategoryDTO> findAllTechCategoryDTOList() {
        List<TechCategoryEntity> techCategoryEntityList = techCategoryRepository.findAll();
        List<TechCategoryDTO> techCategoryDTOList = new ArrayList<>();
        for (TechCategoryEntity techCategoryEntity : techCategoryEntityList) {
            techCategoryDTOList.add(TechCategoryDTO.toTechCategoryDTO(techCategoryEntity));
        }
        return techCategoryDTOList;
    }


    // 게시글 작성
    @Transactional
    public Long save(ProjectDTO projectDTO) {
        UserEntity userEntity = userRepository.findById(projectDTO.getUserId()).get();
        ProjectEntity projectEntity = projectRepository.save(ProjectEntity.toProjectEntity(projectDTO, userEntity));
        return ProjectDTO.toProjectDTO(projectEntity).getId();
    }

    // 프로젝트 - 포지션 카테고리 T 데이터 추가
    public void saveProjectPosition(ProjectDTO dto, ProjectPositionCategoryLinkDTO projectPositionCategoryLinkDTO) {
        ProjectEntity projectEntity = projectRepository.findById(dto.getId()).get();
        PositionCategoryEntity positionCategoryEntity = positionCategoryRepository.findById(projectPositionCategoryLinkDTO.getPositionId()).get();
        projectPositionCategoryLinkRepository.save(ProjectPositionCategoryLinkEntity.toProjectPositionCategoryLinkEntity(projectPositionCategoryLinkDTO,
                projectEntity, positionCategoryEntity));
    }

    // 프로젝트 - 진행기간 카테고리 T 데이터 추가
    public void saveProjectPeriod(ProjectDTO dto, ProjectPeriodCategoryLinkDTO projectPeriodCategoryLinkDTO) {
        ProjectEntity projectEntity = projectRepository.findById(dto.getId()).get();
        PeriodCategoryEntity periodCategoryEntity = periodCategoryRepository.findById(projectPeriodCategoryLinkDTO.getPeriodId()).get();
        projectPeriodCategoryLinkRepository.save(
                ProjectPeriodCategoryLinkEntity.toProjectPeriodCategoryLinkEntity(projectPeriodCategoryLinkDTO,
                        projectEntity, periodCategoryEntity));
    }

    // 프로젝트 - 기술스택 카테고리 T 데이터 추가
    public void saveProjectTech(ProjectDTO dto, List<Long> techIdList) {
        ProjectEntity projectEntity = projectRepository.findById(dto.getId()).get();
        for (Long techId : techIdList) {
            TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(techId).get();
            projectTechCategoryLinkRepository.save(ProjectTechCategoryLinkEntity.toProjectTechCategoryLinkEntity(
                        projectEntity, techCategoryEntity));
        }
    }

//    public void saveProjectTech(ProjectDTO dto, ProjectTechCategoryLinkDTO projectTechCategoryLinkDTO) {
//        ProjectEntity projectEntity = projectRepository.findById(dto.getId()).get();
//        TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(projectTechCategoryLinkDTO.getTechId()).get();
//        projectTechCategoryLinkRepository.save(
//                ProjectTechCategoryLinkEntity.toProjectTechCategoryLinkEntity(projectTechCategoryLinkDTO,
//                        projectEntity, techCategoryEntity));
//
//    }

    // 프로젝트 - 스터디 카테고리 T 데이터 추가
    public void saveProjectStudy(ProjectDTO dto, ProjectStudyCategoryLinkDTO projectStudyCategoryLinkDTO) {
        ProjectEntity projectEntity = projectRepository.findById(dto.getId()).get();
        ProjectStudyCategoryEntity projectStudyCategoryEntity =
                projectStudyCategoryRepository.findById(projectStudyCategoryLinkDTO.getProjectStudyId()).get();
        ProjectStudyCategoryLinkEntity projectStudyCategoryLinkEntity = ProjectStudyCategoryLinkEntity.toProjectStudyCategoryLinkEntity(projectStudyCategoryLinkDTO, projectEntity, projectStudyCategoryEntity);
        projectStudyCategoryLinkRepository.save(projectStudyCategoryLinkEntity);
    }




    // 게시글 목록 조회
    @Transactional
    public List<ProjectDTO> findAll() {
        List<ProjectEntity> projectEntityList = projectRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntityList) {
            projectDTOList.add(ProjectDTO.toProjectDTO(projectEntity));
        }
        return projectDTOList;
    }

    // 게시글 상세보기
    @Transactional
    public ProjectDTO findById(Long id) {
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(id);
        if (optionalProjectEntity.isPresent()) {
            return ProjectDTO.toProjectDTO(optionalProjectEntity.get());
        } else {
            return null;
        }
    }

    // 게시글 - 프로젝트 / 스터디 여부 조회
    public ProjectStudyCategoryDTO findProjectStudyCategory(Long projectId) {
        ProjectStudyCategoryLinkEntity projectStudyCategoryLinkEntity =
                projectStudyCategoryLinkRepository.findByProjectEntity_Id(projectId).get();
        Long projectStudyId = projectStudyCategoryLinkEntity.getProjectStudyCategoryEntity().getId();
        ProjectStudyCategoryEntity projectStudyCategoryEntity = projectStudyCategoryRepository.findById(projectStudyId).get();
        return ProjectStudyCategoryDTO.toProjectStudyCategoryDTO(projectStudyCategoryEntity);
    }

    // 게시글 - 포지션 조회
    public PositionCategoryDTO findPositionCategory(Long projectId) {
        ProjectPositionCategoryLinkEntity projectPositionCategoryLinkEntity
                = projectPositionCategoryLinkRepository.findByProjectEntity_Id(projectId).get();
        Long positionId = projectPositionCategoryLinkEntity.getPositionCategoryEntity().getId();
        PositionCategoryEntity positionCategoryEntity = positionCategoryRepository.findById(positionId).get();
        return PositionCategoryDTO.toPositionCategoryDTO(positionCategoryEntity);
    }

    // 게시글 - 기술스택 조회
    public List<TechCategoryDTO> findTechCategory(Long projectId) {
        // 나중에 리스트 형태로 수정 !!!!!!
       List<ProjectTechCategoryLinkEntity> projectTechCategoryLinkEntityList
                = projectTechCategoryLinkRepository.findByProjectEntity_Id(projectId);
       List<TechCategoryDTO> techCategoryDTOList = new ArrayList<>();
       for (ProjectTechCategoryLinkEntity projectTechCategoryLinkEntity : projectTechCategoryLinkEntityList) {
           Long techId = projectTechCategoryLinkEntity.getTechCategoryEntity().getId();
           TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(techId).get();
           techCategoryDTOList.add(TechCategoryDTO.toTechCategoryDTO(techCategoryEntity));
       }
        return techCategoryDTOList;
    }

//    public TechCategoryDTO findTechCategory(Long projectId) {
//        // 나중에 리스트 형태로 수정 !!!!!!
//        ProjectTechCategoryLinkEntity projectTechCategoryLinkEntity
//                = projectTechCategoryLinkRepository.findByProjectEntity_Id(projectId).get();
//        Long techId = projectTechCategoryLinkEntity.getTechCategoryEntity().getId();
//        TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(techId).get();
//        return TechCategoryDTO.toTechCategoryDTO(techCategoryEntity);
//    }

    // 게시글 - 진행기간 조회
    public PeriodCategoryDTO findPeriodCategory(Long projectId) {
        ProjectPeriodCategoryLinkEntity projectPeriodCategoryLinkEntity
                = projectPeriodCategoryLinkRepository.findByProjectEntity_Id(projectId).get();
        Long periodId = projectPeriodCategoryLinkEntity.getPeriodCategoryEntity().getId();
        PeriodCategoryEntity periodCategoryEntity = periodCategoryRepository.findById(periodId).get();
        return PeriodCategoryDTO.toPeriodCategoryDTO(periodCategoryEntity);
    }

    // 게시글 조회수 증가
    @Transactional
    public void updateReadCount(Long id) {
        projectRepository.updateReadCount(id);
    }

    // 게시글 삭제
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }


    // 게시글 수정
    @Transactional
    public ProjectDTO update(ProjectDTO projectDTO) {
        projectRepository.updateProject(projectDTO.getTitle(), projectDTO.getContent(), projectDTO.getEndDate(), projectDTO.getHeadCount(), projectDTO.getId());
        ProjectEntity projectEntity = projectRepository.findById(projectDTO.getId()).get();
        return ProjectDTO.toProjectDTO(projectEntity);
    }

    // 포지션 수정
    @Transactional
    public PositionCategoryDTO updatePosition(ProjectDTO projectDTO,
                                   ProjectPositionCategoryLinkDTO projectPositionCategoryLinkDTO) {
        projectPositionCategoryLinkRepository.updatePosition(projectPositionCategoryLinkDTO.getPositionId(),
                projectDTO.getId());

        PositionCategoryEntity positionCategoryEntity = projectPositionCategoryLinkRepository.findByProjectEntity_Id(projectDTO.getId()).get().getPositionCategoryEntity();
        return PositionCategoryDTO.toPositionCategoryDTO(positionCategoryEntity);
    }

    // 진행기간 수정
    @Transactional
    public PeriodCategoryDTO updatePeriod(ProjectDTO projectDTO,
                                          ProjectPeriodCategoryLinkDTO projectPeriodCategoryLinkDTO) {
        projectPeriodCategoryLinkRepository.updatePeriod(projectPeriodCategoryLinkDTO.getPeriodId(), projectDTO.getId());
        PeriodCategoryEntity periodCategoryEntity = projectPeriodCategoryLinkRepository.findByProjectEntity_Id(projectDTO.getId()).get().getPeriodCategoryEntity();
        return PeriodCategoryDTO.toPeriodCategoryDTO(periodCategoryEntity);
    }

    // 기술스택 수정
    @Transactional
    public List<TechCategoryDTO> updateTech(ProjectDTO projectDTO, List<Long> techIdList) {
        ProjectEntity projectEntity = projectRepository.findById(projectDTO.getId()).get();
        projectTechCategoryLinkRepository.deleteByProjectEntity(projectEntity);
        for (Long techId : techIdList) {
            TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(techId).get();
            projectTechCategoryLinkRepository.save(ProjectTechCategoryLinkEntity.toProjectTechCategoryLinkEntity(
                    projectEntity, techCategoryEntity));
//            if (projectTechCategoryLinkRepository.countByProjectEntityAndTechCategoryEntity(projectEntity, techCategoryEntity) == 0) {
//                projectTechCategoryLinkRepository.save(ProjectTechCategoryLinkEntity.toProjectTechCategoryLinkEntity(
//                        projectEntity, techCategoryEntity));
//            } else {
//                projectTechCategoryLinkRepository.updateTech(techId, projectDTO.getId());
//            }
        }
        List<TechCategoryDTO> techCategoryDTOList = new ArrayList<>();
        List<ProjectTechCategoryLinkEntity> entityList = projectTechCategoryLinkRepository.findByProjectEntity_Id(projectDTO.getId());
        for (ProjectTechCategoryLinkEntity entity : entityList) {
            Long techId = entity.getTechCategoryEntity().getId();
            TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(techId).get();
            techCategoryDTOList.add(TechCategoryDTO.toTechCategoryDTO(techCategoryEntity));
        }
        return techCategoryDTOList;
    }

//    List<ProjectTechCategoryLinkEntity> projectTechCategoryLinkEntityList
//            = projectTechCategoryLinkRepository.findByProjectEntity_Id(projectId);
//    List<TechCategoryDTO> techCategoryDTOList = new ArrayList<>();
//       for (ProjectTechCategoryLinkEntity projectTechCategoryLinkEntity : projectTechCategoryLinkEntityList) {
//        Long techId = projectTechCategoryLinkEntity.getTechCategoryEntity().getId();
//        TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(techId).get();
//        techCategoryDTOList.add(TechCategoryDTO.toTechCategoryDTO(techCategoryEntity));
//    }
//        return techCategoryDTOList;

    // 기술스택 수정 (나중에 리스트 형태로 수정해야함)
//    @Transactional
//    public TechCategoryDTO updateTech(ProjectDTO projectDTO,
//                                      ProjectTechCategoryLinkDTO projectTechCategoryLinkDTO) {
//        projectTechCategoryLinkRepository.updateTech(projectTechCategoryLinkDTO.getTechId(), projectDTO.getId());
//        TechCategoryEntity techCategoryEntity = projectTechCategoryLinkRepository.findByProjectEntity_Id(projectDTO.getId()).get().getTechCategoryEntity();
//        return TechCategoryDTO.toTechCategoryDTO(techCategoryEntity);
//    }

//    @Transactional
//    public TechCategoryDTO updateTech(ProjectDTO projectDTO,
//                                      ProjectTechCategoryLinkDTO projectTechCategoryLinkDTO) {
//        projectTechCategoryLinkRepository.updateTech(projectTechCategoryLinkDTO.getTechId(), projectDTO.getId());
//        TechCategoryEntity techCategoryEntity = projectTechCategoryLinkRepository.findByProjectEntity_Id(projectDTO.getId()).get().getTechCategoryEntity();
//        return TechCategoryDTO.toTechCategoryDTO(techCategoryEntity);
//    }

    // 프로젝트 / 스터디 여부 수정
    @Transactional
    public ProjectStudyCategoryDTO updateProjectStudy(ProjectDTO projectDTO,
                                                      ProjectStudyCategoryLinkDTO projectStudyCategoryLinkDTO) {
        projectStudyCategoryLinkRepository.updateProjectStudy(projectStudyCategoryLinkDTO.getProjectStudyId(), projectDTO.getId());
        ProjectStudyCategoryEntity projectStudyCategoryEntity = projectStudyCategoryLinkRepository.findByProjectEntity_Id(projectDTO.getId()).get().getProjectStudyCategoryEntity();
        return ProjectStudyCategoryDTO.toProjectStudyCategoryDTO(projectStudyCategoryEntity);
    }

    // 프로젝트 / 스터디 랜덤 추출 3개
    @Transactional
    public List<ProjectDTO> findRandomProjects() {
        List<ProjectEntity> projectEntityList = projectRepository.findRandomProjects();
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntityList) {
            projectDTOList.add(ProjectDTO.toProjectDTO(projectEntity));
        }
        return projectDTOList;
    }

}

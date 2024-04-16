/*
package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.entity.*;
import com.yyi.projectStudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService1 {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final PeriodCategoryRepository periodCategoryRepository;
    private final PositionCategoryRepository positionCategoryRepository;
    private final ProjectStudyCategoryRepository projectStudyCategoryRepository;
    private final TechCategoryRepository techCategoryRepository;
    private final ProjectCommentRepository projectCommentRepository;
    private final ProjectClipRepository projectClipRepository;

    private final ProjectPositionCategoryLinkRepository projectPositionCategoryLinkRepository;
    private final ProjectPeriodCategoryLinkRepository projectPeriodCategoryLinkRepository;
    private final ProjectStudyCategoryLinkRepository projectStudyCategoryLinkRepository;
    private final ProjectTechCategoryLinkRepository projectTechCategoryLinkRepository;

    private static final int PAGE_LIMIT = 6; // 한 페이지에 존재하는 게시글 수


    */
/* 진행기간 카테고리 조회 *//*

    public List<PeriodCategoryDTO> findAllPeriodCategoryDTOList() {
        List<PeriodCategoryEntity> periodCategoryEntityList
                = periodCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<PeriodCategoryDTO> periodCategoryDTOList = new ArrayList<>();
        for (PeriodCategoryEntity periodCategoryEntity : periodCategoryEntityList) {
            periodCategoryDTOList.add(PeriodCategoryDTO.toPeriodCategoryDTO(periodCategoryEntity));
        }
        return periodCategoryDTOList;
    }

    */
/* 포지션 카테고리 조회 *//*

    public List<PositionCategoryDTO> findAllPositionCategoryDTOList() {
        List<PositionCategoryEntity> positionCategoryEntityList = positionCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<PositionCategoryDTO> positionCategoryDTOList = new ArrayList<>();
        for (PositionCategoryEntity positionCategoryEntity : positionCategoryEntityList) {
            positionCategoryDTOList.add(PositionCategoryDTO.toPositionCategoryDTO(positionCategoryEntity));
        }
        return positionCategoryDTOList;
    }

    */
/* 프로젝트, 스터디 카테고리 조회 *//*

    public List<ProjectStudyCategoryDTO> findAllProjectStudyCategoryDTOList() {
        List<ProjectStudyCategoryEntity> projectStudyCategoryEntityList = projectStudyCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<ProjectStudyCategoryDTO> projectStudyCategoryDTOList = new ArrayList<>();
        for (ProjectStudyCategoryEntity projectStudyCategoryEntity : projectStudyCategoryEntityList) {
            projectStudyCategoryDTOList.add(ProjectStudyCategoryDTO.toProjectStudyCategoryDTO(projectStudyCategoryEntity));
        }
        return projectStudyCategoryDTOList;
    }

    */
/* 사용 언어 카테고리 조회 *//*

    public List<TechCategoryDTO> findAllTechCategoryDTOList() {
        List<TechCategoryEntity> techCategoryEntityList = techCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<TechCategoryDTO> techCategoryDTOList = new ArrayList<>();
        for (TechCategoryEntity techCategoryEntity : techCategoryEntityList) {
            techCategoryDTOList.add(TechCategoryDTO.toTechCategoryDTO(techCategoryEntity));
        }
        return techCategoryDTOList;
    }


    */
/* 게시글 작성 *//*

    @Transactional
    public Long save(ProjectDTO projectDTO) {
        UserEntity userEntity = userRepository.findById(projectDTO.getUserId()).get();
        ProjectEntity projectEntity = projectRepository.save(ProjectEntity.toProjectEntity(projectDTO, userEntity));
        return ProjectDTO.toProjectDTO(projectEntity).getId();
    }

    */
/* 저장될 게시글의 모집 포지션 데이터 *//*

    public void saveProjectPosition(ProjectDTO dto, List<Long> positionIdList) {
        ProjectEntity projectEntity = projectRepository.findById(dto.getId()).get();
        for (Long positionId : positionIdList) {
            PositionCategoryEntity positionCategoryEntity = positionCategoryRepository.findById(positionId).get();
            projectPositionCategoryLinkRepository.save(ProjectPositionCategoryLinkEntity.toProjectPositionCategoryLinkEntity(projectEntity, positionCategoryEntity));
        }
    }


    */
/* 저장될 게시글의 진행기간 데이터 *//*

    public void saveProjectPeriod(ProjectDTO dto, ProjectPeriodCategoryLinkDTO projectPeriodCategoryLinkDTO) {
        ProjectEntity projectEntity = projectRepository.findById(dto.getId()).get();
        PeriodCategoryEntity periodCategoryEntity = periodCategoryRepository.findById(projectPeriodCategoryLinkDTO.getPeriodId()).get();
        projectPeriodCategoryLinkRepository.save(
                ProjectPeriodCategoryLinkEntity.toProjectPeriodCategoryLinkEntity(projectPeriodCategoryLinkDTO,
                        projectEntity, periodCategoryEntity));
    }

    */
/* 저장될 게시글의 사용 언어 데이터 *//*

    public void saveProjectTech(ProjectDTO dto, List<Long> techIdList) {
        ProjectEntity projectEntity = projectRepository.findById(dto.getId()).get();
        for (Long techId : techIdList) {
            TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(techId).get();
            projectTechCategoryLinkRepository.save(ProjectTechCategoryLinkEntity.toProjectTechCategoryLinkEntity(
                        projectEntity, techCategoryEntity));
        }
    }

    */
/* 저장될 게시글의 프로젝트, 스터디 여부 데이터 *//*

    public void saveProjectStudy(ProjectDTO dto, ProjectStudyCategoryLinkDTO projectStudyCategoryLinkDTO) {
        ProjectEntity projectEntity = projectRepository.findById(dto.getId()).get();
        ProjectStudyCategoryEntity projectStudyCategoryEntity =
                projectStudyCategoryRepository.findById(projectStudyCategoryLinkDTO.getProjectStudyId()).get();
        ProjectStudyCategoryLinkEntity projectStudyCategoryLinkEntity = ProjectStudyCategoryLinkEntity.toProjectStudyCategoryLinkEntity(projectStudyCategoryLinkDTO, projectEntity, projectStudyCategoryEntity);
        projectStudyCategoryLinkRepository.save(projectStudyCategoryLinkEntity);
    }




    */
/* 게시글 목록 조회  *//*

    @Transactional
    public Page<ProjectDTO> findAll(String status, Pageable pageable) {
        Page<ProjectEntity> projectList;

        int page = pageable.getPageNumber() - 1;
        if (status != null) {
            if (status.equals("unrecruited")) {
                projectList = projectRepository.findByStatusOrderByIdDesc("모집중", PageRequest.of(page, PAGE_LIMIT));
            } else {
                projectList = projectRepository.findByStatusOrderByIdDesc("모집완료", PageRequest.of(page, PAGE_LIMIT));
            }
        } else {
            projectList = projectRepository.findAll(PageRequest.of(page, PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        }
        Page<ProjectDTO> projectDTOS = projectList.map(
                project -> new ProjectDTO(project));

        return projectDTOS;
    }

*/
/*    @Transactional
    public List<ProjectDTO> findAll(String status) {
        List<ProjectEntity> projectEntityList;
        if (status != null) {
            if (status.equals("unrecruited")) {
                projectEntityList = projectRepository.findByStatusOrderByIdDesc("모집중");
            } else {
                projectEntityList = projectRepository.findByStatusOrderByIdDesc("모집완료");
            }
        } else {
            projectEntityList = projectRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntityList) {
            projectDTOList.add(ProjectDTO.toProjectDTO(projectEntity));
        }
        return projectDTOList;
    }*//*


    */
/* 게시글 상세 보기 *//*

    @Transactional
    public ProjectDTO findById(Long id) {
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(id);
        if (optionalProjectEntity.isPresent()) {
            return ProjectDTO.toProjectDTO(optionalProjectEntity.get());
        } else {
            return null;
        }
    }

    */
/* 해당 게시글의 프로젝트, 스터디 여부 조회 *//*

    public ProjectStudyCategoryDTO findProjectStudyCategory(Long projectId) {
        ProjectStudyCategoryLinkEntity projectStudyCategoryLinkEntity =
                projectStudyCategoryLinkRepository.findByProjectEntity_Id(projectId).get();
        Long projectStudyId = projectStudyCategoryLinkEntity.getProjectStudyCategoryEntity().getId();
        ProjectStudyCategoryEntity projectStudyCategoryEntity = projectStudyCategoryRepository.findById(projectStudyId).get();
        return ProjectStudyCategoryDTO.toProjectStudyCategoryDTO(projectStudyCategoryEntity);
    }

    */
/* 해당 게시글의 모집 포지션 조회 *//*

    public List<PositionCategoryDTO> findPositionCategory(Long projectId) {
        List<ProjectPositionCategoryLinkEntity> projectPositionCategoryLinkEntityList =
                projectPositionCategoryLinkRepository.findByProjectEntity_Id(projectId);
        List<PositionCategoryDTO> positionCategoryDTOList = new ArrayList<>();
        for (ProjectPositionCategoryLinkEntity projectPositionCategoryLinkEntity : projectPositionCategoryLinkEntityList) {
            Long positionId = projectPositionCategoryLinkEntity.getPositionCategoryEntity().getId();
            PositionCategoryEntity positionCategoryEntity = positionCategoryRepository.findById(positionId).get();
            positionCategoryDTOList.add(PositionCategoryDTO.toPositionCategoryDTO(positionCategoryEntity));
        }
        return positionCategoryDTOList;
    }

    */
/* 해당 게시글의 프로젝트, 스터디 사용 언어 조회 *//*

    public List<TechCategoryDTO> findTechCategory(Long projectId) {
       List<ProjectTechCategoryLinkEntity> projectTechCategoryLinkEntityList
                = projectTechCategoryLinkRepository.findByProjectEntity_IdOrderByIdAsc(projectId);
       List<TechCategoryDTO> techCategoryDTOList = new ArrayList<>();
       for (ProjectTechCategoryLinkEntity projectTechCategoryLinkEntity : projectTechCategoryLinkEntityList) {
           Long techId = projectTechCategoryLinkEntity.getTechCategoryEntity().getId();
           TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(techId).get();
           techCategoryDTOList.add(TechCategoryDTO.toTechCategoryDTO(techCategoryEntity));
       }
        return techCategoryDTOList;
    }

    */
/* 해당 게시글의 프로젝트, 스터디 진행기간 조회 *//*

    public PeriodCategoryDTO findPeriodCategory(Long projectId) {
        ProjectPeriodCategoryLinkEntity projectPeriodCategoryLinkEntity
                = projectPeriodCategoryLinkRepository.findByProjectEntity_Id(projectId).get();
        Long periodId = projectPeriodCategoryLinkEntity.getPeriodCategoryEntity().getId();
        PeriodCategoryEntity periodCategoryEntity = periodCategoryRepository.findById(periodId).get();
        return PeriodCategoryDTO.toPeriodCategoryDTO(periodCategoryEntity);
    }

    */
/* 게시글 클릭 시 조회수 증가 *//*

    @Transactional
    public void updateReadCount(Long id) {
        projectRepository.updateReadCount(id);
    }

    */
/* 게시글 삭제 *//*

    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }


    */
/* 게시글 수정 *//*

    @Transactional
    public ProjectDTO update(ProjectDTO projectDTO) {
        projectRepository.updateProject(projectDTO.getTitle(),
                projectDTO.getContent(), projectDTO.getStartDate(),
                projectDTO.getHeadCount(), projectDTO.getId());
        ProjectEntity projectEntity = projectRepository.findById(projectDTO.getId()).get();
        return ProjectDTO.toProjectDTO(projectEntity);
    }

    */
/* 모집 포지션 수정 *//*

    @Transactional
    public List<PositionCategoryDTO> updatePosition(ProjectDTO projectDTO,
                                              List<Long> positionIdList) {
        ProjectEntity projectEntity = projectRepository.findById(projectDTO.getId()).get();
        */
/* 기존에 저장된 포지션 삭제 *//*

        projectPositionCategoryLinkRepository.deleteByProjectEntity(projectEntity);
        */
/* 새로 저장될 포지션 목록 저장 *//*

        for (Long positionId : positionIdList) {
            PositionCategoryEntity positionCategoryEntity = positionCategoryRepository.findById(positionId).get();
            projectPositionCategoryLinkRepository.save(ProjectPositionCategoryLinkEntity.toProjectPositionCategoryLinkEntity(
                    projectEntity, positionCategoryEntity));
        }
        */
/* 새로 저장된 포지션 목록 조회 *//*

        List<PositionCategoryDTO> positionCategoryDTOList = new ArrayList<>();
        List<ProjectPositionCategoryLinkEntity> entityList = projectPositionCategoryLinkRepository.findByProjectEntity_Id(projectDTO.getId());
        for (ProjectPositionCategoryLinkEntity entity : entityList) {
            Long positionId = entity.getPositionCategoryEntity().getId();
            PositionCategoryEntity positionCategoryEntity = positionCategoryRepository.findById(positionId).get();
            positionCategoryDTOList.add(PositionCategoryDTO.toPositionCategoryDTO(positionCategoryEntity));
        }
        return positionCategoryDTOList;
    }

    */
/* 진행기간 수정 *//*

    @Transactional
    public PeriodCategoryDTO updatePeriod(ProjectDTO projectDTO,
                                          ProjectPeriodCategoryLinkDTO projectPeriodCategoryLinkDTO) {
        projectPeriodCategoryLinkRepository.updatePeriod(projectPeriodCategoryLinkDTO.getPeriodId(), projectDTO.getId());
        PeriodCategoryEntity periodCategoryEntity = projectPeriodCategoryLinkRepository.findByProjectEntity_Id(projectDTO.getId()).get().getPeriodCategoryEntity();
        return PeriodCategoryDTO.toPeriodCategoryDTO(periodCategoryEntity);
    }

    */
/* 사용 언어 목록 수정 *//*

    @Transactional
    public List<TechCategoryDTO> updateTech(ProjectDTO projectDTO, List<Long> techIdList) {
        ProjectEntity projectEntity = projectRepository.findById(projectDTO.getId()).get();
        */
/* 기존에 저장된 사용 언어 데이터 삭제 *//*

        projectTechCategoryLinkRepository.deleteByProjectEntity(projectEntity);
        */
/* 새로 저장될 사용 언어 목록 저장 *//*

        for (Long techId : techIdList) {
            TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(techId).get();
            projectTechCategoryLinkRepository.save(ProjectTechCategoryLinkEntity.toProjectTechCategoryLinkEntity(
                    projectEntity, techCategoryEntity));
        }
        */
/* 새로 저장된 사용 언어 목록 조회 *//*

        List<TechCategoryDTO> techCategoryDTOList = new ArrayList<>();
        List<ProjectTechCategoryLinkEntity> entityList = projectTechCategoryLinkRepository.findByProjectEntity_IdOrderByIdAsc(projectDTO.getId());
        for (ProjectTechCategoryLinkEntity entity : entityList) {
            Long techId = entity.getTechCategoryEntity().getId();
            TechCategoryEntity techCategoryEntity = techCategoryRepository.findById(techId).get();
            techCategoryDTOList.add(TechCategoryDTO.toTechCategoryDTO(techCategoryEntity));
        }
        return techCategoryDTOList;
    }


    */
/* 프로젝트, 스터디 여부 수정 *//*

    @Transactional
    public ProjectStudyCategoryDTO updateProjectStudy(ProjectDTO projectDTO,
                                                      ProjectStudyCategoryLinkDTO projectStudyCategoryLinkDTO) {
        projectStudyCategoryLinkRepository.updateProjectStudy(projectStudyCategoryLinkDTO.getProjectStudyId(), projectDTO.getId());
        ProjectStudyCategoryEntity projectStudyCategoryEntity = projectStudyCategoryLinkRepository.findByProjectEntity_Id(projectDTO.getId()).get().getProjectStudyCategoryEntity();
        return ProjectStudyCategoryDTO.toProjectStudyCategoryDTO(projectStudyCategoryEntity);
    }


   */
/* 메인 페이지 (프로젝트 스터디 조회수 순으로 조회) - 나중에 수정할 것 !!!!*//*

    @Transactional
    public List<ProjectDTO> findAllInMainPage() {
        List<ProjectEntity> projectEntityList = projectRepository.findAllByOrderByReadCountDesc();
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntityList) {
            projectDTOList.add(ProjectDTO.toProjectDTO(projectEntity));
        }
        return projectDTOList;
    }

    */
/* 사용자의 프로젝트, 스터디 찾기 메뉴 활동 내역 조회 *//*

    @Transactional
    public List<ProjectArticleDTO> findArticleList(Long userId) {
        List<Object[]> projectArticles = projectRepository.getProjectArticles(userId);
        List<ProjectArticleDTO> projectArticleDTOList = new ArrayList<>();
        for (Object[] article : projectArticles) {
            ProjectArticleDTO projectArticleDTO = new ProjectArticleDTO();
            Long id = (Long) article[0];
            String title = (String) article[1];
            String content = (String) article[2];
            java.sql.Timestamp timestamp = (java.sql.Timestamp) article[3];
            LocalDateTime regDate = timestamp.toLocalDateTime();

            */
/* 게시물 내용일 경우 *//*

            if (title != null) {
                projectArticleDTO.setProjectId(id);
                projectArticleDTO.setTitle(title);
                projectArticleDTO.setProjectContent(content);
                projectArticleDTO.setRegDate(regDate);
                List<TechCategoryDTO> techCategoryDTOList = findTechCategory(id);
                ProjectStudyCategoryDTO projectStudyCategoryDTO = findProjectStudyCategory(id);
                projectArticleDTO.setProjectStudy(projectStudyCategoryDTO.getName());
                projectArticleDTO.setTechList(techCategoryDTOList);
            } else {
                ProjectCommentEntity projectCommentEntity = projectCommentRepository.findById(id).get();
                ProjectEntity projectEntity = projectCommentEntity.getProjectEntity();
                Long projectId = projectEntity.getId();

                projectArticleDTO.setProjectId(projectId);
                projectArticleDTO.setCommentId(id);
                projectArticleDTO.setTitle(projectEntity.getTitle());
                projectArticleDTO.setCommentContent(content);
                projectArticleDTO.setRegDate(regDate);

                ProjectStudyCategoryDTO projectStudyCategoryDTO = findProjectStudyCategory(projectId);
                projectArticleDTO.setProjectStudy(projectStudyCategoryDTO.getName());
                List<TechCategoryDTO> techCategoryDTOList = findTechCategory(projectId);
                projectArticleDTO.setTechList(techCategoryDTOList);
            }

            projectArticleDTOList.add(projectArticleDTO);

        }
        return projectArticleDTOList;
    }


    */
/* 게시물 스크랩 처리 *//*

    public void clip(ProjectClipDTO projectClipDTO) {
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(projectClipDTO.getProjectId());
        if (optionalProjectEntity.isPresent()) {
            ProjectEntity projectEntity = optionalProjectEntity.get();
            UserEntity userEntity = userRepository.findById(projectClipDTO.getUserId()).get();
            int clipCount = projectClipRepository.countByProjectEntityAndUserEntity(projectEntity, userEntity);
            if (clipCount > 0) {
                ProjectClipEntity projectClipEntity = projectClipRepository.findByProjectEntityAndUserEntity(projectEntity, userEntity).get();
                projectClipRepository.deleteById(projectClipEntity.getId());
            } else if (clipCount == 0) {
                ProjectClipEntity projectClipEntity = ProjectClipEntity.toProjectClipEntity(userEntity, projectEntity);
                projectClipRepository.save(projectClipEntity);
            }
        }
    }


    */
/* 게시물 스크랩 여부 확인 *//*

    public int checkClipYn(ProjectClipDTO projectClipDTO) {
        ProjectEntity projectEntity = projectRepository.findById(projectClipDTO.getProjectId()).get();
        UserEntity userEntity = userRepository.findById(projectClipDTO.getUserId()).get();
        return projectClipRepository.countByProjectEntityAndUserEntity(projectEntity, userEntity);
    }

    */
/* 게시물 스크랩 목록 *//*

    @Transactional
    public List<ProjectClipDTO> getClipList(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();

        List<ProjectClipDTO> projectClipList = new ArrayList<>();
        List<ProjectClipEntity> projectClipEntityList = projectClipRepository.findByUserEntityOrderByIdDesc(userEntity);
        for (ProjectClipEntity projectClipEntity : projectClipEntityList) {
            projectClipList.add(ProjectClipDTO.toProjectClipDTO(projectClipEntity));
        }
        return projectClipList;
    }

   */
/* 게시물 당 스크랩 수 조회 *//*

    public int clipCount(Long id) {
        ProjectEntity projectEntity = projectRepository.findById(id).get();
        return projectClipRepository.countByProjectEntity(projectEntity);
    }

    @Transactional
    */
/* Top Writers 조회 *//*

    public List<TopWritersDTO> getTopWriters() {
        List<Object[]> topWriters = projectRepository.getTopWriters();
        List<TopWritersDTO> topWritersDTOList = new ArrayList<>();
        for (Object[] writer : topWriters) {
            TopWritersDTO topWritersDTO = new TopWritersDTO();

            Long userId = (Long) writer[0];
            int totalCount = Integer.parseInt(String.valueOf(writer[1]));
            if (userId != null) {
                topWritersDTO.setUserId(userId);
                topWritersDTO.setTotalCount(totalCount);

                UserEntity userEntity = userRepository.findById(userId).get();
                topWritersDTO.setNickname(userEntity.getNickname());
                topWritersDTO.setFileAttached(userEntity.getFileAttached());
                if (topWritersDTO.getFileAttached() == 1) {
                    topWritersDTO.setStoredFileName(userEntity.getUserImageFileEntityList().get(0).getStoredFileName());
                }
                topWritersDTOList.add(topWritersDTO);
            }
        }
        return topWritersDTOList;
    }

    */
/* 프로젝트 , 스터디 카테고리 여부 조회 - 목록 페이지 *//*

    @Transactional(readOnly = true)
    public Page<ProjectDTO> findProjectStudyInListPage(Long id, String status, Pageable pageable) {
        Page<ProjectEntity> projectEntityList;

        if (status != null) {
            if (status.equals("unrecruited")) {
                projectEntityList = projectRepository.getProjectListByCategoryAndStatus(id, "모집중",
                        PageRequest.of(pageable.getPageNumber() - 1, PAGE_LIMIT));
            } else {
                projectEntityList = projectRepository.getProjectListByCategoryAndStatus(id, "모집완료",
                        PageRequest.of(pageable.getPageNumber() - 1, PAGE_LIMIT));
            }
        } else {
            projectEntityList = projectRepository.getProjectListByCategory(id, PageRequest.of(pageable.getPageNumber() - 1, PAGE_LIMIT));
        }



        Page<ProjectDTO> projectDTOS = projectEntityList.map(
                project -> new ProjectDTO(project));


        return projectDTOS;
    }
  */
/*  @Transactional(readOnly = true)
    public List<ProjectDTO> findProjectStudyInListPage(Long id, String status) {
        List<ProjectEntity> projectEntityList;
        if (status != null) {
            if ("unrecruited".equals(status)) {
                projectEntityList = projectRepository.getProjectListByCategoryAndStatus(id, "모집중");
            } else {
                projectEntityList = projectRepository.getProjectListByCategoryAndStatus(id, "모집완료");
            }
        } else {
            projectEntityList = projectRepository.getProjectListByCategory(id);
        }
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntityList) {
            projectDTOList.add(ProjectDTO.toProjectDTO(projectEntity));
        }
        return projectDTOList;
    }*//*


   */
/* 댓글 많은 순 조회 (전체보기) *//*

    @Transactional(readOnly = true)
    public Page<ProjectDTO> getProjectListOrderByComment(String status, Pageable pageable) {
         Page<ProjectEntity> projectEntityList;
         if (status != null) {
             if (status.equals("unrecruited")) {
                 projectEntityList = projectRepository.getProjectListOrderByCommentAndStatus("모집중", PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
             } else {
                 projectEntityList = projectRepository.getProjectListOrderByCommentAndStatus("모집완료", PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
             }
         } else {
             projectEntityList = projectRepository.getProjectListOrderByComment(PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
         }

        Page<ProjectDTO> projectDTOS = projectEntityList.map(projectEntity -> new ProjectDTO(projectEntity));

        return projectDTOS;
    }

    */
/* 댓글 많은 순 조회 (카테고리 포함) *//*

    @Transactional
    public Page<ProjectDTO> getProjectListOrderByCommentAndCategory(Long projectStudyId, String status, Pageable pageable) {
        Page<ProjectEntity> projectEntityList;
        if (status != null) {
            if (status.equals("unrecruited")) {
                projectEntityList = projectRepository.getProjectListOrderByCommentAndCategoryAndStatus(projectStudyId,"모집중", PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
            } else {
                projectEntityList = projectRepository.getProjectListOrderByCommentAndCategoryAndStatus(projectStudyId,"모집완료", PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
            }
        } else {
            projectEntityList = projectRepository.getProjectListOrderByCommentAndCategory(projectStudyId, PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
        }
        Page<ProjectDTO> projectDTOS = projectEntityList.map(projectEntity -> new ProjectDTO(projectEntity));
        return projectDTOS;
    }

   */
/* 스크랩 많은 순 조회 (전체 보기) *//*

    @Transactional
    public Page<ProjectDTO> getProjectListOrderByClip(String status, Pageable pageable) {
        Page<ProjectEntity> projectEntityList;
        if (status != null) {
            if (status.equals("unrecruited")) {
                projectEntityList = projectRepository.getProjectListOrderByClipAndStatus("모집중", PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
            } else {
                projectEntityList = projectRepository.getProjectListOrderByClipAndStatus("모집완료", PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
            }
        } else {
            projectEntityList = projectRepository.getProjectListOrderByClip(PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
        }

        Page<ProjectDTO> projectDTOS = projectEntityList.map(projectEntity -> new ProjectDTO(projectEntity));
        return projectDTOS;
    }

   */
/* 스크랩 많은 순 조회 (카테고리 포함) *//*

    @Transactional
   public Page<ProjectDTO> getProjectListOrderByClipAndCategory(Long projectStudyId, String status, Pageable pageable) {
        Page<ProjectEntity> projectEntityList;
        if (status != null) {
            if (status.equals("unrecruited")) {
                projectEntityList = projectRepository.getProjectListOrderByClipAndCategoryAndStatus(projectStudyId, "모집중",
                        PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
            } else {
                projectEntityList = projectRepository.getProjectListOrderByClipAndCategoryAndStatus(projectStudyId, "모집완료",
                        PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
            }
       } else {
            projectEntityList = projectRepository.getProjectListOrderByClipAndCategory(projectStudyId, PageRequest.of(pageable.getPageNumber()-1, PAGE_LIMIT));
        }

        Page<ProjectDTO> projectDTOS = projectEntityList.map(projectEntity -> new ProjectDTO(projectEntity));
       return projectDTOS;
   }


   */
/* 기술스택 선택하여 게시물 조회 *//*

    @Transactional
   public List<ProjectDTO> selectTechList(List<Long> techIdList) {
       List<ProjectEntity> projectEntityList = projectRepository.selectTechList(techIdList);
       List<ProjectDTO> projectDTOList = new ArrayList<>();
       for (ProjectEntity projectEntity : projectEntityList) {
          projectDTOList.add(ProjectDTO.toProjectDTO(projectEntity));
       }
       return projectDTOList;
   }


}
*/

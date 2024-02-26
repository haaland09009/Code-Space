package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.service.ProjectCommentService;
import com.yyi.projectStudy.service.ProjectService;
import com.yyi.projectStudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.unbescape.html.HtmlEscape;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectCommentService projectCommentService;
    private final UserService userService;


    // 글쓰기 폼
    @GetMapping("/write")
    public String writeForm(HttpSession session, Model model) {

        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            List<PeriodCategoryDTO> periodCategoryList = projectService.findAllPeriodCategoryDTOList();
            List<PositionCategoryDTO> positionCategoryList = projectService.findAllPositionCategoryDTOList();
            List<ProjectStudyCategoryDTO> projectStudyCategoryList = projectService.findAllProjectStudyCategoryDTOList();
            List<TechCategoryDTO> techCategoryList = projectService.findAllTechCategoryDTOList();

            model.addAttribute("periodCategoryList", periodCategoryList);
            model.addAttribute("positionCategoryList", positionCategoryList);
            model.addAttribute("projectStudyCategoryList", projectStudyCategoryList);
            model.addAttribute("techCategoryList", techCategoryList);

            return "project/write";
        }
    }

    // 게시글 작성
    @PostMapping("/write")
    public String write(@ModelAttribute ProjectDTO projectDTO,
                        @ModelAttribute ProjectPeriodCategoryLinkDTO
                                projectPeriodCategoryLinkDTO,
                        @ModelAttribute ProjectStudyCategoryLinkDTO
                                projectStudyCategoryLinkDTO,
                        @ModelAttribute ProjectPositionCategoryLinkDTO
                                projectPositionCategoryLinkDTO,
                        @ModelAttribute ProjectTechCategoryLinkDTO
                                projectTechCategoryLinkDTO) {
        // 내용 enter 처리
        String content = projectDTO.getContent().replaceAll("\\n", "<br>");
        projectDTO.setContent(content);
        //
        Long savedId = projectService.save(projectDTO);

        ProjectDTO dto = projectService.findById(savedId);

        // 프로젝트 - 포지션 T
        projectService.saveProjectPosition(dto, projectPositionCategoryLinkDTO);
        // 프로젝트 - 진행기간 T
        projectService.saveProjectPeriod(dto, projectPeriodCategoryLinkDTO);
        // 프로젝트 - 기술스택 T
        projectService.saveProjectTech(dto, projectTechCategoryLinkDTO);
        // 프로젝트 - 스터디 T
        projectService.saveProjectStudy(dto, projectStudyCategoryLinkDTO);


        return "redirect:/project/" + savedId;
    }

    // 게시글 리스트
    @GetMapping("")
    public String boardList(Model model) {
        List<ProjectDTO> projectDTOList = projectService.findAll();

        for (ProjectDTO projectDTO : projectDTOList) {
            String content = projectDTO.getContent().replace("<br>", "\\n");
            projectDTO.setContent(content);
            projectDTO.setCommentCount(projectCommentService.count(projectDTO.getId()));
            // 나중에 리스트로 변경
            projectDTO.setTechList(projectService.findTechCategory(projectDTO.getId()).getName());
        }
        model.addAttribute("projectList", projectDTOList);

        // 랜덤 추출 3개
        List<ProjectDTO> randomProjects = projectService.findRandomProjects();
        model.addAttribute("randomProjectList", randomProjects);

        return "project/list";
    }

    // 게시글 상세보기
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {

        // 작성자 정보
        Long userId = projectService.findById(id).getUserId();
        UserDTO userDTO = userService.findById(userId);
        model.addAttribute("writerInfo", userDTO);

        projectService.updateReadCount(id);
        ProjectDTO projectDTO = projectService.findById(id);
        // enter 처리
        String content = projectDTO.getContent().replace("<br>", "\r\n");
        projectDTO.setContent(content);
        //
        model.addAttribute("project", projectDTO);

        // 프로젝트 / 스터디 여부 조회
        ProjectStudyCategoryDTO projectStudyCategoryDTO = projectService.findProjectStudyCategory(id);
        model.addAttribute("projectStudyCategory", projectStudyCategoryDTO);

        // 모집 포지션 조회
        PositionCategoryDTO positionCategoryDTO = projectService.findPositionCategory(id);
        model.addAttribute("positionCategory", positionCategoryDTO);

        // 기술스택 조회
        TechCategoryDTO techCategoryDTO = projectService.findTechCategory(id);
        model.addAttribute("techCategory", techCategoryDTO);

        // 진행기간 조회
        PeriodCategoryDTO periodCategoryDTO = projectService.findPeriodCategory(id);
        model.addAttribute("periodCategory", periodCategoryDTO);


//        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        List<ProjectCommentDTO> projectCommentDTOList = projectCommentService.findAll(id);
        for (ProjectCommentDTO projectCommentDTO : projectCommentDTOList) {
            int commentLikeCount = projectCommentService.commentLikeCount(projectCommentDTO.getId());
            projectCommentDTO.setLikeCount(commentLikeCount);
        }
        Long commentCount = projectCommentService.count(id);


        model.addAttribute("commentList", projectCommentDTOList);
        model.addAttribute("commentCount", commentCount);
        // 나중에 삭제처리 -> 인터셉트 처리해야함 projectDTO가 NULL일 경우 (추후에 예정)


        // 랜덤 추출 3개
        List<ProjectDTO> randomProjects = projectService.findRandomProjects();
        model.addAttribute("randomProjectList", randomProjects);

        return "project/detail";
    }

    // 게시글 삭제
    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            projectService.deleteById(id);
            return "redirect:/project";
        }
    }

    // 게시글 수정 폼
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model) {

        List<PeriodCategoryDTO> periodCategoryList = projectService.findAllPeriodCategoryDTOList();
        List<PositionCategoryDTO> positionCategoryList = projectService.findAllPositionCategoryDTOList();
        List<ProjectStudyCategoryDTO> projectStudyCategoryList = projectService.findAllProjectStudyCategoryDTOList();
        List<TechCategoryDTO> techCategoryList = projectService.findAllTechCategoryDTOList();

        model.addAttribute("periodCategoryList", periodCategoryList);
        model.addAttribute("positionCategoryList", positionCategoryList);
        model.addAttribute("projectStudyCategoryList", projectStudyCategoryList);
        model.addAttribute("techCategoryList", techCategoryList);

        // 프로젝트 / 스터디 여부 조회
        ProjectStudyCategoryDTO projectStudyCategoryDTO = projectService.findProjectStudyCategory(id);
        model.addAttribute("selectedProjectStudyId", projectStudyCategoryDTO.getId());

        // 모집 포지션 조회
        PositionCategoryDTO positionCategoryDTO = projectService.findPositionCategory(id);
        model.addAttribute("selectedPositionId", positionCategoryDTO.getId());

        // 기술스택 조회
        TechCategoryDTO techCategoryDTO = projectService.findTechCategory(id);
        model.addAttribute("selectedTechId", techCategoryDTO.getId());

        // 진행기간 조회
        PeriodCategoryDTO periodCategoryDTO = projectService.findPeriodCategory(id);
        model.addAttribute("selectedPeriodId", periodCategoryDTO.getId());


        ProjectDTO projectDTO = projectService.findById(id);
        model.addAttribute("project", projectDTO);
        return "project/update";
    }


    // 게시글 수정
    @PostMapping("/update")
    public String update(@ModelAttribute ProjectDTO projectDTO,
                         @ModelAttribute ProjectPeriodCategoryLinkDTO
                                 projectPeriodCategoryLinkDTO,
                         @ModelAttribute ProjectStudyCategoryLinkDTO
                                     projectStudyCategoryLinkDTO,
                         @ModelAttribute ProjectPositionCategoryLinkDTO
                                     projectPositionCategoryLinkDTO,
                         @ModelAttribute ProjectTechCategoryLinkDTO
                                     projectTechCategoryLinkDTO,
                         Model model) {
        ProjectDTO project = projectService.update(projectDTO);
        model.addAttribute("project", project);

        // 포지션
        PositionCategoryDTO positionCategory = projectService.updatePosition(projectDTO, projectPositionCategoryLinkDTO);
        model.addAttribute("positionCategory", positionCategory);

        // 프로젝트 / 스터디
        ProjectStudyCategoryDTO projectStudyCategoryDTO = projectService.updateProjectStudy(projectDTO, projectStudyCategoryLinkDTO);
        model.addAttribute("projectStudyCategory", projectStudyCategoryDTO);

        // 기술스택
        TechCategoryDTO techCategoryDTO = projectService.updateTech(projectDTO, projectTechCategoryLinkDTO);
        model.addAttribute("techCategory", techCategoryDTO);

        // 진행기간 조회
        PeriodCategoryDTO periodCategoryDTO = projectService.updatePeriod(projectDTO, projectPeriodCategoryLinkDTO);
        model.addAttribute("periodCategory", periodCategoryDTO);

        List<ProjectCommentDTO> projectCommentDTOList = projectCommentService.findAll(projectDTO.getId());
        Long commentCount = projectCommentService.count(project.getId());


        model.addAttribute("commentList", projectCommentDTOList);
        model.addAttribute("commentCount", commentCount);

        return "project/detail";

    }

//    // 게시글 수정 후 조회
//    @GetMapping("/renew/{id}")
//    public String renew(@PathVariable Long id, Model model) {
//        ProjectDTO projectDTO = projectService.findById(id);
//        model.addAttribute("project", projectDTO);
//        return "project/detail";
//    }
}

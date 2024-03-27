package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.service.*;
import com.yyi.projectStudy.time.StringToDate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectCommentService projectCommentService;
    private final UserService userService;
    private final CookieService cookieService;


   /* 게시글 작성 페이지 이동 */
    @GetMapping("/write")
    public String writeForm(HttpSession session, Model model) {

        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            /*  진행기간 카테고리 조회 */
            List<PeriodCategoryDTO> periodCategoryList = projectService.findAllPeriodCategoryDTOList();
            model.addAttribute("periodCategoryList", periodCategoryList);

            /* 모집 포지션 카테고리 조회 */
            List<PositionCategoryDTO> positionCategoryList = projectService.findAllPositionCategoryDTOList();
            model.addAttribute("positionCategoryList", positionCategoryList);

            /* 프로젝트/스터디 카테고리 조회 */
            List<ProjectStudyCategoryDTO> projectStudyCategoryList = projectService.findAllProjectStudyCategoryDTOList();
            model.addAttribute("projectStudyCategoryList", projectStudyCategoryList);

            /* 기술스택 카테고리 조회 */
            List<TechCategoryDTO> techCategoryList = projectService.findAllTechCategoryDTOList();
            model.addAttribute("techCategoryList", techCategoryList);

            return "project/write";
        }
    }

    /*  게시글 작성  */
    @PostMapping("/write")
    public String write(@ModelAttribute ProjectDTO projectDTO,
                        @ModelAttribute ProjectPeriodCategoryLinkDTO
                                projectPeriodCategoryLinkDTO,
                        @ModelAttribute ProjectStudyCategoryLinkDTO
                                projectStudyCategoryLinkDTO,
                        @RequestParam(name = "positionId", required = false) List<Long> positionIdList,
                        @RequestParam(name = "techId", required = false) List<Long> techIdList) {

        /* 작성 내용 줄바꿈 후 dto에 저장 */
        String content = projectDTO.getContent().replaceAll("\n", "<br>");
        projectDTO.setContent(content);

        /* 게시글 작성 후 해당 게시글의 pk 반환 */
        Long savedId = projectService.save(projectDTO);
        /* 위에서 얻은 게시글 pk로 게시글 dto 조회 */
        ProjectDTO dto = projectService.findById(savedId);

        /*  모집 포지션 목록 저장 */
        projectService.saveProjectPosition(dto, positionIdList);
        /*  진행기간 저장 */
        projectService.saveProjectPeriod(dto, projectPeriodCategoryLinkDTO);
        /* 사용 언어 (기술 스택) 목록 저장 */
        projectService.saveProjectTech(dto, techIdList);
        /* 프로젝트, 스터디 여부 저장 */
        projectService.saveProjectStudy(dto, projectStudyCategoryLinkDTO);

        return "redirect:/project/" + savedId;
    }

    @GetMapping(value = {"", "/category/{category}"})
    public String boardList(Model model,
                            @PathVariable(name = "category", required = false) String category,
                            @RequestParam(name = "order", required = false) String order,
                            @RequestParam(name = "status", required = false) String status,
                            @PageableDefault(page = 1) Pageable pageable) {

        /* 게시글 목록 불러오기 */
        Page<ProjectDTO> projectDTOList;


        /* 프로젝트 메뉴를 클릭할 경우 */
        if (category != null && category.equals("pro")) {
            model.addAttribute("category", "pro");
            projectDTOList = projectService.findProjectStudyInListPage(1L, status, pageable);
            if ("clip".equals(order)) {
                projectDTOList = projectService.getProjectListOrderByClipAndCategory(1L, status, pageable);
            } else if ("comment".equals(order)) {
                projectDTOList = projectService.getProjectListOrderByCommentAndCategory(1L, status, pageable);
            }
        }
        /*  스터디 메뉴를 선택할 경우 */
        else if (category != null && category.equals("study")) {
            model.addAttribute("category", "study");
            projectDTOList = projectService.findProjectStudyInListPage(2L, status, pageable);
            if ("clip".equals(order)) {
                projectDTOList = projectService.getProjectListOrderByClipAndCategory(2L, status, pageable);
            } else if ("comment".equals(order)) {
                projectDTOList = projectService.getProjectListOrderByCommentAndCategory(2L, status, pageable);
            }
        } else {
            if ("clip".equals(order)) {
                projectDTOList = projectService.getProjectListOrderByClip(status, pageable);
            } else if ("comment".equals(order)) {
                projectDTOList = projectService.getProjectListOrderByComment(status, pageable);
            } else {
                projectDTOList = projectService.findAll(status, pageable);
            }
        }

        if (status != null) {
            model.addAttribute("status", status);
        }
        if (order != null && order.equals("clip")) {
            model.addAttribute("order", "clip");
        } else if (order != null && order.equals("comment")) {
            model.addAttribute("order", "comment");
        }

        /* 게시글 목록 반복문 */
        for (ProjectDTO projectDTO : projectDTOList) {

            /* 날짜 변환 */
            String formatDateTime = StringToDate.formatDateTime(String.valueOf(projectDTO.getRegDate()));
            projectDTO.setFormattedDate(formatDateTime);

            String content = projectDTO.getContent().replace("<br>", "\n");

            /* 목록 조회 화면에 html 태그 제거 */
            content = content.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
            content = content.replaceAll("<[^>]*>", " ");
            projectDTO.setContent(content);

            /* 게시글 당 댓글 수 조회 */
            projectDTO.setCommentCount(projectCommentService.count(projectDTO.getId()));

            /* 게시글 당 프로젝트, 스터디 여부 조회 */
            ProjectStudyCategoryDTO projectStudyCategoryDTO = projectService.findProjectStudyCategory(projectDTO.getId());
            projectDTO.setProjectStudy(projectStudyCategoryDTO.getName());

            /* 게시글 당 스크랩 수 조회 */
            int clipCount = projectService.clipCount(projectDTO.getId());
            projectDTO.setClipCount(clipCount);


            //////////////////////////////////////////
            /* !!! 추후에 진행상태 저장하는 코드 추가할 것 !!*/


            /* 게시글 당 사용 언어 목록 조회 */
            List<TechCategoryDTO> techCategoryDTOList = projectService.findTechCategory(projectDTO.getId());
            List<String> techList = new ArrayList<>();
            for (TechCategoryDTO techCategoryDTO : techCategoryDTOList) {
                techList.add(techCategoryDTO.getName());
            }
            projectDTO.setTechList(techList);
        }


        /* Top writer s*/
        List<TopWritersDTO> topWritersDTOList = projectService.getTopWriters();
        model.addAttribute("topWriters", topWritersDTOList);

        /* 인기글 */
        List<ProjectDTO> topProjectDTOList = projectService.findAllInMainPage();
        for (ProjectDTO projectDTO : topProjectDTOList) {
            UserDTO userDTO = userService.findById(projectDTO.getUserId());
            projectDTO.setFileAttached(userDTO.getFileAttached());
            if (userDTO.getFileAttached() == 1) {
                projectDTO.setStoredFileName(userDTO.getStoredFileName());
            }
        }
        model.addAttribute("topProjectList", topProjectDTOList);

        int blockLimit = 10; // (화면에 보여지는 페이지 갯수)
        int startPage = (((int)(Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < projectDTOList.getTotalPages()) ? startPage + blockLimit - 1 : projectDTOList.getTotalPages();


        model.addAttribute("projectList", projectDTOList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "project/list";
    }

    /* 게시글 상세보기 */
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model,
                           HttpSession session,
                           HttpServletRequest request, HttpServletResponse response) {

       /* 게시글 pk로 해당 게시글 작성자 정보 조회 */
        Long userId = projectService.findById(id).getUserId();
        UserDTO userDTO = userService.findById(userId);
        model.addAttribute("writerInfo", userDTO);

        /* 작성자의 직군 조회 */
        JobDTO job = userService.findJob(userId);
        model.addAttribute("job", job);

        /* 클릭 시 조회수 증가 */
        cookieService.checkCookieForReadCount(request, response, "project", id);

        /* enter 처리 */
        ProjectDTO projectDTO = projectService.findById(id);
        String content = projectDTO.getContent().replaceAll("<br>", "\n");
        projectDTO.setContent(content);
        model.addAttribute("project", projectDTO);

        /* 프로젝트, 스터디 여부 조회 */
        ProjectStudyCategoryDTO projectStudyCategoryDTO = projectService.findProjectStudyCategory(id);
        model.addAttribute("projectStudyCategory", projectStudyCategoryDTO);

        /* 모집 포지션 조회 */
        List<PositionCategoryDTO> positionCategoryDTOList = projectService.findPositionCategory(id);
        model.addAttribute("positionCategoryList", positionCategoryDTOList);

        /* 사용 언어 조회 */
        List<TechCategoryDTO> techCategoryDTOList = projectService.findTechCategory(id);
        model.addAttribute("techCategoryList", techCategoryDTOList);

        /* 진행기간 조회 */
        PeriodCategoryDTO periodCategoryDTO = projectService.findPeriodCategory(id);
        model.addAttribute("periodCategory", periodCategoryDTO);

        /* 게시글에 작성된 댓글 목록 조회 */
        List<ProjectCommentDTO> projectCommentDTOList = projectCommentService.findAll(id);
        for (ProjectCommentDTO projectCommentDTO : projectCommentDTOList) {

            /* 날짜 변환하기 */
            String formatDateTime = StringToDate.formatDateTime(String.valueOf(projectCommentDTO.getRegDate()));
            projectCommentDTO.setFormattedDate(formatDateTime);

            /* 댓글 당 좋아요 수 조회 */
            int commentLikeCount = projectCommentService.commentLikeCount(projectCommentDTO.getId());
            projectCommentDTO.setLikeCount(commentLikeCount);

            /* 댓글 당 싫어요 수 조회 */
            int commentDisLikeCount = projectCommentService.commentDisLikeCount(projectCommentDTO.getId());
            projectCommentDTO.setDisLikeCount(commentDisLikeCount);

            /* 댓글 작성자의 직군 조회 */
            JobDTO userJob = userService.findJob(projectCommentDTO.getUserId());
            projectCommentDTO.setJobName(userJob.getName());
        }
        /* 게시글에 작성된 총 댓글 수 조회 */
        Long commentCount = projectCommentService.count(id);

        model.addAttribute("commentList", projectCommentDTOList);
        model.addAttribute("commentCount", commentCount);


        /* 회원 당 게시글 스크랩 여부 확인 */
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser != null) {
            ProjectClipDTO projectClipDTO = new ProjectClipDTO();
            projectClipDTO.setProjectId(id);
            projectClipDTO.setUserId(sessionUser.getId());
            /* 스크랩 여부 조회 */
            int clipCount = projectService.checkClipYn(projectClipDTO);
            model.addAttribute("clipCount", clipCount);
        }
        return "project/detail";
    }

    /* 게시글 삭제 처리 */
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

    /* 게시글 수정 페이지 이동 */
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model, HttpSession session) {

        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            /* 선택할 카테고리 모두 조회 */
            List<PeriodCategoryDTO> periodCategoryList = projectService.findAllPeriodCategoryDTOList();
            model.addAttribute("periodCategoryList", periodCategoryList);

            List<PositionCategoryDTO> positionCategoryList = projectService.findAllPositionCategoryDTOList();
            model.addAttribute("positionCategoryList", positionCategoryList);

            List<ProjectStudyCategoryDTO> projectStudyCategoryList = projectService.findAllProjectStudyCategoryDTOList();
            model.addAttribute("projectStudyCategoryList", projectStudyCategoryList);

            List<TechCategoryDTO> techCategoryList = projectService.findAllTechCategoryDTOList();
            model.addAttribute("techCategoryList", techCategoryList);

            /* 게시글 작성 시 선택했던 프로젝트, 스터디 여부 조회 */
            ProjectStudyCategoryDTO projectStudyCategoryDTO = projectService.findProjectStudyCategory(id);
            model.addAttribute("selectedProjectStudyId", projectStudyCategoryDTO.getId());

            /* 게시글 작성 시 선택했던 모집 포지션 조회 */
            List<PositionCategoryDTO> positionCategoryDTOList = projectService.findPositionCategory(id);
            List<Long> selectedPositionIdList = new ArrayList<>();
            for (PositionCategoryDTO positionCategoryDTO : positionCategoryDTOList) {
                selectedPositionIdList.add(positionCategoryDTO.getId());
            }
            model.addAttribute("selectedPositionIdList", selectedPositionIdList);

            /* 게시글 작성 시 선택했던 사용 언어 조회 */
            List<TechCategoryDTO> techCategoryDTOList = projectService.findTechCategory(id);
            List<Long> selectedTechIdList = new ArrayList<>();
            for (TechCategoryDTO techCategoryDTO : techCategoryDTOList) {
                selectedTechIdList.add(techCategoryDTO.getId());
            }
            model.addAttribute("selectedTechIdList", selectedTechIdList);

            /* 게시글 작성 시 선택했던 진행 기간 조회 */
            PeriodCategoryDTO periodCategoryDTO = projectService.findPeriodCategory(id);
            model.addAttribute("selectedPeriodId", periodCategoryDTO.getId());

            /* 줄바꿈 처리 */
            ProjectDTO projectDTO = projectService.findById(id);
            String content = projectDTO.getContent();
            content = content.replaceAll("<br>", "\n");
            projectDTO.setContent(content);

            model.addAttribute("project", projectDTO);

            return "project/update";
        }
    }


    /* 게시글 수정 처리 → 수정된 데이터 조회 */
    @PostMapping("/update")
    public String update(@ModelAttribute ProjectDTO projectDTO,
                         @ModelAttribute ProjectPeriodCategoryLinkDTO
                                 projectPeriodCategoryLinkDTO,
                         @ModelAttribute ProjectStudyCategoryLinkDTO
                                     projectStudyCategoryLinkDTO,
                         @RequestParam(name = "positionId", required = false) List<Long> positionIdList,
                         @RequestParam(name = "techId", required = false) List<Long> techIdList,
                         Model model, HttpSession session) {

        /* 게시글 수정 처리 후 dto 반환 */
        ProjectDTO project = projectService.update(projectDTO);
        model.addAttribute("project", project);

        /* enter 처리 */
/*        String content = projectDTO.getContent().replaceAll("\r\n", "<br>");*/
        String content = projectDTO.getContent().replaceAll("\n", "<br>");
        projectDTO.setContent(content);


        /* 수정된 카테고리 코두 조회 */
        List<PositionCategoryDTO> positionCategoryDTOList = projectService.updatePosition(projectDTO, positionIdList);
        model.addAttribute("positionCategoryList", positionCategoryDTOList);

        ProjectStudyCategoryDTO projectStudyCategoryDTO = projectService.updateProjectStudy(projectDTO, projectStudyCategoryLinkDTO);
        model.addAttribute("projectStudyCategory", projectStudyCategoryDTO);

        List<TechCategoryDTO> techCategoryDTOList = projectService.updateTech(projectDTO, techIdList);
        model.addAttribute("techCategoryList", techCategoryDTOList);

        PeriodCategoryDTO periodCategoryDTO = projectService.updatePeriod(projectDTO, projectPeriodCategoryLinkDTO);
        model.addAttribute("periodCategory", periodCategoryDTO);

        /* 게시글에 작성된 모든 댓글 목록 조회 */
        List<ProjectCommentDTO> projectCommentDTOList = projectCommentService.findAll(projectDTO.getId());
        for (ProjectCommentDTO projectCommentDTO : projectCommentDTOList) {
            /* 댓글 당 좋아요 수 조회 */
            int commentLikeCount = projectCommentService.commentLikeCount(projectCommentDTO.getId());
            projectCommentDTO.setLikeCount(commentLikeCount);

            /* 댓글 당 싫어요 수 조회 */
            int commentDisLikeCount = projectCommentService.commentDisLikeCount(projectCommentDTO.getId());
            projectCommentDTO.setDisLikeCount(commentDisLikeCount);

            /* 댓글 작성자의 직군 조회  */
            JobDTO userJob = userService.findJob(projectCommentDTO.getUserId());
            projectCommentDTO.setJobName(userJob.getName());

            /* 날짜 변환하기 */
            String formatDateTime = StringToDate.formatDateTime(String.valueOf(projectCommentDTO.getRegDate()));
            projectCommentDTO.setFormattedDate(formatDateTime);
        }
        /* 게시글에 작성된 총 댓글 수 조회  */
        Long commentCount = projectCommentService.count(project.getId());

        model.addAttribute("commentList", projectCommentDTOList);
        model.addAttribute("commentCount", commentCount);

        /* 게시글 작성자 정보  */
        Long userId = projectService.findById(projectDTO.getId()).getUserId();
        UserDTO userDTO = userService.findById(userId);
        model.addAttribute("writerInfo", userDTO);

        /* 게시글 작성자의 직군 조회  */
        JobDTO job = userService.findJob(userId);
        model.addAttribute("job", job);

        return "project/detail";

    }


    /* 게시물 스크랩 클릭 */
    @PostMapping("/clip")
    public @ResponseBody void clip(@ModelAttribute ProjectClipDTO projectClipDTO) {
        projectService.clip(projectClipDTO);
    }

    /* 게시물 스크랩 여부 확인 */
    @GetMapping("/checkClipYn")
    public @ResponseBody boolean checkClipYn(@ModelAttribute ProjectClipDTO projectClipDTO) {
        int count = projectService.checkClipYn(projectClipDTO);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }







}

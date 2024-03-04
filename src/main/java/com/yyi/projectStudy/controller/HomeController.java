package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.service.ProjectCommentService;
import com.yyi.projectStudy.service.ProjectService;
import com.yyi.projectStudy.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProjectService projectService;
    private final ProjectCommentService projectCommentService;
    private final QnaService qnaService;
    @GetMapping("/")
    public String mainPage(Model model) {

        // 플젝 스터디
        List<ProjectDTO> projectDTOList = projectService.findAllInMainPage();
        for (ProjectDTO projectDTO : projectDTOList) {
            projectDTO.setCommentCount(projectCommentService.count(projectDTO.getId()));

            ProjectStudyCategoryDTO projectStudyCategoryDTO = projectService.findProjectStudyCategory(projectDTO.getId());
            projectDTO.setProjectStudy(projectStudyCategoryDTO.getName());

            List<TechCategoryDTO> techCategoryDTOList = projectService.findTechCategory(projectDTO.getId());
            List<String> techList = new ArrayList<>();
            for (TechCategoryDTO techCategoryDTO : techCategoryDTOList) {
                techList.add(techCategoryDTO.getName());
            }
            projectDTO.setTechList(techList);

            Date endDate = projectDTO.getEndDate();

            long currentTime = System.currentTimeMillis();
            long remainingTimeMillis = endDate.getTime() - currentTime;

            long remainingDays = remainingTimeMillis / (1000 * 60 * 60 * 24);

            projectDTO.setRemainingDays(remainingDays);

            model.addAttribute("projectList", projectDTOList);

            // qna
            List<QnaBestDTO> qnaDTOList = qnaService.findBestQnaList();
            model.addAttribute("qnaList", qnaDTOList);

        }
        return "main";
    }
}
package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.service.ChatService;
import com.yyi.projectStudy.service.ProjectService;
import com.yyi.projectStudy.service.QnaService;
import com.yyi.projectStudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ChatService chatService;
    private final ProjectService projectService;
    private final QnaService qnaService;
    // 로그인 페이지 이동
    @GetMapping("/loginPage")
    public String loginPage() {

        return "user/loginPage";
    }

    // 회원가입 페이지 이동
    @GetMapping("/joinPage")
    public String joinPage(Model model) {
        List<JobDTO> jobDTOList = userService.findAllJobs();
        model.addAttribute("jobList", jobDTOList);

        return "user/joinPage";
    }

    // 회원가입 프로세스
    @PostMapping("/joinProcess")
    public String joinProcess(@ModelAttribute UserDTO userDTO,
                              @ModelAttribute UserJobDTO userJobDTO) throws IOException {
        userService.save(userDTO, userJobDTO);
        return "user/loginPage";
    }

    // 로그인 프로세스
    @PostMapping("/loginProcess")
    public @ResponseBody String loginProcess(@ModelAttribute UserDTO userDTO, HttpSession session) {
        UserDTO sessionUser = userService.login(userDTO);
        if (sessionUser != null) {
            session.setAttribute("userDTO", sessionUser);
            return "ok";
        } else {

            return "no";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    // 마이페이지
    @GetMapping("/myPage")
    public String myPage(HttpSession session, Model model) {
//        return "user/myPage";
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            UserDTO userDTO = userService.findById(sessionUser.getId());
            model.addAttribute("userDTO", userDTO);

            JobDTO jobDTO = userService.findJob(sessionUser.getId());
            model.addAttribute("jobDTO", jobDTO);

            List<JobDTO> jobDTOList = userService.findAllJobs();
            model.addAttribute("jobList", jobDTOList);

            return "user/myPage";
        }
    }


    // 회원 정보 수정 (나중에 사진까지 수정해야함)
    @PostMapping("/updateProcess")
    public String updateProcess(@ModelAttribute UserDTO userDTO,
                                @ModelAttribute UserJobDTO userJobDTO) {
        userService.update(userDTO, userJobDTO);
        return "redirect:/user/myPage";
    }

    // 메시지함 조회
    @GetMapping("/message")
    public String message(HttpSession session, Model model) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            Long userId = sessionUser.getId();
            List<ChatDTO> chatDTOList = chatService.findRecentChats(userId);
            model.addAttribute("chatRoomList", chatDTOList);


            return "user/message";
        }
    }

    // 작성 게시물 조회
    @GetMapping("/articles")
    public String boardList(Model model, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            Long userId = sessionUser.getId();
            List<ProjectArticleDTO> projectArticleDTOList = projectService.findArticleList(userId);
            model.addAttribute("projectArticleList", projectArticleDTOList);

            List<QnaArticleDTO> qnaArticleDTOList = qnaService.findArticleList(userId);
            model.addAttribute("qnaArticleList", qnaArticleDTOList);

            return "user/articles";
        }
    }

    // 스크랩 목록 조회
    @GetMapping("/clip")
    public String clipList(Model model, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            Long userId = sessionUser.getId();

            List<ProjectClipDTO> projectClipDTOList = projectService.getClipList(userId);
            for (ProjectClipDTO projectClipDTO : projectClipDTOList) {
                ProjectDTO projectDTO = projectService.findById(projectClipDTO.getProjectId());
                projectClipDTO.setTitle(projectDTO.getTitle());
                projectClipDTO.setProjectStudy(projectService.findProjectStudyCategory(projectDTO.getId()).getName());
            }
            model.addAttribute("projectClipList", projectClipDTOList);


            List<QnaClipDTO> qnaClipDTOList = qnaService.getClipList(userId);
            for (QnaClipDTO qnaClipDTO : qnaClipDTOList) {
                QnaDTO qnaDTO = qnaService.findById(qnaClipDTO.getQnaId());
                qnaClipDTO.setTitle(qnaDTO.getTitle());
                qnaClipDTO.setCategoryName(qnaService.findTopic(qnaDTO.getId()).getName());
            }

            model.addAttribute("qnaClipList", qnaClipDTOList);

            return "user/clip";
        }

    }


}
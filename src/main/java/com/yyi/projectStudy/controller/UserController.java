package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.service.*;
import com.yyi.projectStudy.time.StringToDate;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final NotificationService notificationService;
    private final ProjectCommentService projectCommentService;
    private final QnaReplyService qnaReplyService;
    private final QnaReplyCommentService qnaReplyCommentService;

    /* 로그인 페이지 이동 */
    @GetMapping("/loginPage")
    public String loginPage() {

        return "user/loginPage";
    }

    /* 회원가입 페이지 이동 */
    @GetMapping("/joinPage")
    public String joinPage(Model model) {
        List<JobDTO> jobDTOList = userService.findAllJobs();
        model.addAttribute("jobList", jobDTOList);

        return "user/joinPage";
    }

    /* 이메일 중복 체크 */
    @GetMapping("/existsUserId")
    public @ResponseBody boolean existsUserId(String email) {
        return userService.existsUserId(email);
    }

    /* 닉네임 중복 체크 */
    @GetMapping("/existsNickname")
    public @ResponseBody boolean existsNickname(String nickname) {
        return userService.existsNickname(nickname);
    }


    /* 회원가입 프로세스 */
    @PostMapping("/joinProcess")
    public String joinProcess(@ModelAttribute UserDTO userDTO,
                              @ModelAttribute UserJobDTO userJobDTO) throws IOException {
        userService.save(userDTO, userJobDTO);
        return "user/loginPage";
    }

    /* 로그인 프로세스 */
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

    /* 로그아웃 */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    /* 마이페이지 이동 */
    @GetMapping("/myPage")
    public String myPage(HttpSession session, Model model) {
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

   /* !!! (나중에 사진까지 수정하는 기능 추가해야함) */
    /* 회원 정보 수정  */
    @PostMapping("/updateProcess")
    public String updateProcess(@ModelAttribute UserDTO userDTO,
                                @ModelAttribute UserJobDTO userJobDTO) {
        userService.update(userDTO, userJobDTO);
        return "redirect:/user/myPage";
    }

    /* 메시지함 조회 */
    @GetMapping("/message")
    public String message(HttpSession session, Model model) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            Long userId = sessionUser.getId();
            List<ChatDTO> chatDTOList = chatService.findRecentChats(userId, null);
            model.addAttribute("chatRoomList", chatDTOList);
            return "user/message";
        }
    }

    /* 작성 게시물 조회 */
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

    /* 스크랩 목록 조회 */
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

    /* 알림창 조회 */
    @GetMapping("/notification")
    public String notificationList(Model model, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            List<NotificationDTO> notificationDTOList
                    = notificationService.findAll(sessionUser.getId(), null);
            for (NotificationDTO notificationDTO : notificationDTOList) {
                String notUrl = notificationDTO.getNotUrl();
                String[] parts = notUrl.split("/");
                /* ex)  "/project/154#commentId_342"   */
                String contentPk;
                if (notUrl.contains("#")) {
                    // "#commentId_342" 부분을 제거하고 숫자만 추출한다.
                    contentPk = parts[2].split("#")[0];
                } else {
                    // #을 포함하고 있지 않으면 기존의 방식으로 숫자만 가져온다.
                    contentPk = parts[2];
                }
                Long id = Long.parseLong(contentPk); // 문자열을 Long 형으로 변환
                if (notificationDTO.getNotId() == 1) {
                    /* 해당 알림이 프로젝트, 스터디 게시물 댓글일 경우 */
                    ProjectDTO projectDTO = projectService.findById(id);

                    /* 게시글 제목 */
                    notificationDTO.setTitle(projectDTO.getTitle());

                    /* 게시글에 대한 댓글 */
                    ProjectCommentDTO projectCommentDTO
                            = projectCommentService.findById(notificationDTO.getEntityId());
                    notificationDTO.setNotContent(projectCommentDTO.getContent());

                } else if (notificationDTO.getNotId() == 2) {
                    /* 해당 알림이 qna 답변일 경우 */

                    /* 게시글 제목 */
                    QnaDTO qnaDTO = qnaService.findById(id);
                    notificationDTO.setTitle(qnaDTO.getTitle());

                    /* 게시글에 대한 답변 */
                    QnaReplyDTO qnaReplyDTO = qnaReplyService.findById(notificationDTO.getEntityId());
                    notificationDTO.setNotContent(qnaReplyDTO.getContent());

                } else if (notificationDTO.getNotId() == 3) {
                    /* 해당 알림이 qna 답변에 대한 댓글일 경우 */

                    /* 게시글 제목 */
                    QnaDTO qnaDTO = qnaService.findById(id);
                    notificationDTO.setTitle(qnaDTO.getTitle());

                    /* 답변에 대한 댓글 */
                    QnaReplyCommentDTO qnaReplyCommentDTO
                            = qnaReplyCommentService.findById(notificationDTO.getEntityId());
                    notificationDTO.setNotContent(qnaReplyCommentDTO.getContent());

                }
                /* 날짜 변환하기 */
                String formatDateTime = StringToDate.formatDateTime(String.valueOf(notificationDTO.getRegDate()));
                notificationDTO.setFormattedDate(formatDateTime);
            }
            model.addAttribute("notificationList", notificationDTOList);
            return "user/notification";
        }
    }

    /* 사용자 정보 조회 - 모달 클릭 목적 */
    @GetMapping("/getUserInfo/{id}")
    public ResponseEntity getUserInfo(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id);
         /* 회원의 직군 조회 */
        JobDTO jobDTO = userService.findJob(id);
        userDTO.setJobName(jobDTO.getName());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }





}
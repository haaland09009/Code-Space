package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.service.NotificationService;
import com.yyi.projectStudy.service.ProjectCommentService;
import com.yyi.projectStudy.service.ProjectService;
import com.yyi.projectStudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projectComment")
public class ProjectCommentController {
    private final ProjectService projectService;
    private final ProjectCommentService projectCommentService;
    private final NotificationService notificationService;
    private final UserService userService;
    // 댓글 작성
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute ProjectCommentDTO projectCommentDTO,
                               HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        projectCommentDTO.setUserId(sessionUser.getId());

        Long commentId = projectCommentService.save(projectCommentDTO);
        if (commentId != null) {
            List<ProjectCommentDTO> projectCommentDTOList =
                    projectCommentService.findAll(projectCommentDTO.getProjectId());

            // 댓글 알림 넣기
            ProjectDTO projectDTO = projectService.findById(projectCommentDTO.getProjectId());
            NotificationDTO notificationDTO = new NotificationDTO();
            if (!projectDTO.getUserId().equals(projectCommentDTO.getUserId())) {
                notificationDTO.setUserId(projectDTO.getUserId()); // 수신자 id
                notificationDTO.setSenderId(projectCommentDTO.getUserId()); // 발신자 id
                notificationDTO.setNotType("projectComment"); // 종류
                notificationDTO.setNotContentId(commentId); // 종류 pk
                notificationDTO.setNotUrl("/project/" + projectDTO.getId());
                notificationService.saveComment(notificationDTO, commentId);
            }
            return new ResponseEntity<>(projectCommentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // 댓글 수 조회
    @GetMapping("/count/{id}")
    public @ResponseBody Long commentCount(@PathVariable("id") Long id) {
        return projectCommentService.count(id);
    }


    // 댓글 조회
    @GetMapping("/getCommentList/{projectId}")
    public ResponseEntity getCommentList(@PathVariable("projectId") Long projectId) {
        if (projectId != null) {
            List<ProjectCommentDTO> projectCommentDTOList = projectCommentService.findAll(projectId);
            // 댓글마다 좋아요, 싫어요 수 등록
            for (ProjectCommentDTO projectCommentDTO : projectCommentDTOList) {
                int commentLikeCount = projectCommentService.commentLikeCount(projectCommentDTO.getId());
                projectCommentDTO.setLikeCount(commentLikeCount);

                int commentDisLikeCount = projectCommentService.commentDisLikeCount(projectCommentDTO.getId());
                projectCommentDTO.setDisLikeCount(commentDisLikeCount);

                JobDTO userJob = userService.findJob(projectCommentDTO.getUserId());
                projectCommentDTO.setJobName(userJob.getName());
            }
            return new ResponseEntity<>(projectCommentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // 댓글 삭제
    @PostMapping("/delete/{id}")
    public @ResponseBody void delete(@PathVariable("id") Long id) {
        projectCommentService.deleteById(id);
    }

    // 본인이 작성한 댓글인지 확인
    @GetMapping("/isYourComment")
    public @ResponseBody boolean isYourComment(@ModelAttribute ProjectCommentDTO projectCommentDTO) {

        Long userId = projectCommentDTO.getUserId(); // sessionId
        Long writerId = projectCommentService.isYourComment(projectCommentDTO.getId());
        if (userId.equals(writerId)) {
            return true;
        } else {
            return false;
        }
    }


    // 댓글 좋아요
    @PostMapping("/commentLike")
    public @ResponseBody void commentLike(@ModelAttribute ProCmtLikeDTO proCmtLikeDTO) {
        projectCommentService.commentLike(proCmtLikeDTO);
    }

    // 댓글 좋아요 수 확인
    @GetMapping("/likeCount/{id}")
    public @ResponseBody int commentLikeCount(@PathVariable("id") Long id) {
        return projectCommentService.commentLikeCount(id);
    }

    // 댓글 싫어요
    @PostMapping("/commentDisLike")
    public @ResponseBody void commentDisLike(@ModelAttribute ProCmtDisLikeDTO proCmtDisLikeDTO) {
        projectCommentService.commentDisLike(proCmtDisLikeDTO);
    }

    // 댓글 싫어요 수 확인
    @GetMapping("/disLikeCount/{id}")
    public @ResponseBody int commentDisLikeCount(@PathVariable("id") Long id) {
        return projectCommentService.commentDisLikeCount(id);
    }

    // 댓글 좋아요를 눌렀을 때 싫어요 여부 확인
    @GetMapping("/checkCommentDisLike")
    public @ResponseBody int checkCommentDisLike(@ModelAttribute ProjectCommentDTO projectCommentDTO) {
        return projectCommentService.checkCommentDisLike(projectCommentDTO);
    }

    // 댓글 싫어요를 눌렀을 때 좋어요 여부 확인
    @GetMapping("/checkCommentLike")
    public @ResponseBody int checkCommentLike(@ModelAttribute ProjectCommentDTO projectCommentDTO) {
        return projectCommentService.checkCommentLike(projectCommentDTO);
    }



}


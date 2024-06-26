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
    private final UserService userService;
    private final NotificationService notificationService;

    /* 댓글 작성하기 */
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute ProjectCommentDTO projectCommentDTO,
                               HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        projectCommentDTO.setUserId(sessionUser.getId());

        Long commentId = projectCommentService.save(projectCommentDTO);
        if (commentId != null) {
            List<ProjectCommentDTO> projectCommentDTOList =
                    projectCommentService.findAll(projectCommentDTO.getProjectId());

            /* 알림 넣기 */
            ProjectDTO projectDTO = projectService.findById(projectCommentDTO.getProjectId());
            /* 게시물 작성자와 댓글을 작성한 회원이 다른 경우에만 알림을 추가한다.*/
            if (!projectDTO.getUserId().equals(projectCommentDTO.getUserId())) {
                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setReceiver(projectDTO.getUserId()); // 수신자 pk
                notificationDTO.setSender(projectCommentDTO.getUserId()); // 발신자 pk
                notificationDTO.setEntityId(commentId); // 댓글 pk
                notificationDTO.setNotUrl("/project/" + projectDTO.getId() + "#comment_" + commentId);
                notificationService.saveProjectComment(notificationDTO, projectDTO);
            }
            return new ResponseEntity<>(projectCommentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    /* 게시글의 댓글 수 조회 */
    @GetMapping("/count/{id}")
    public @ResponseBody int commentCount(@PathVariable("id") Long id) {
        return projectCommentService.count(id);
    }


    /* 댓글 작성 후 목록 조회 */
    @GetMapping("/getCommentList/{projectId}")
    public ResponseEntity getCommentList(@PathVariable("projectId") Long projectId, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (projectId != null) {
            List<ProjectCommentDTO> projectCommentDTOList = projectCommentService.findAll(projectId);
            /* 댓글마다 좋아요, 싫어요 수 */
            for (ProjectCommentDTO projectCommentDTO : projectCommentDTOList) {
                int commentLikeCount = projectCommentService.commentLikeCount(projectCommentDTO.getId());
                projectCommentDTO.setLikeCount(commentLikeCount);

                int commentDisLikeCount = projectCommentService.commentDisLikeCount(projectCommentDTO.getId());
                projectCommentDTO.setDisLikeCount(commentDisLikeCount);

                if (sessionUser == null) {
                    projectCommentDTO.setLikeYn(0);
                    projectCommentDTO.setDisLikeYn(0);
                } else {
                    int likeYn = projectCommentService.checkLike(projectCommentDTO.getId(), sessionUser.getId());
                    int disLikeYn = projectCommentService.checkDisLike(projectCommentDTO.getId(), sessionUser.getId());
                    projectCommentDTO.setLikeYn(likeYn);
                    projectCommentDTO.setDisLikeYn(disLikeYn);
                }

                /* 댓글 작성자의 직군 */
                JobDTO userJob = userService.findJob(projectCommentDTO.getUserId());
                projectCommentDTO.setJobName(userJob.getName());
            }
            return new ResponseEntity<>(projectCommentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    /* 댓글 삭제 */
    @PostMapping("/delete/{id}")
    public @ResponseBody void delete(@PathVariable("id") Long id) {
        projectCommentService.deleteById(id);
    }

    /* 본인이 작성한 댓글인지 확인 */
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


    /* 댓글 좋아요 */
    @PostMapping("/commentLike")
    public @ResponseBody boolean commentLike(@ModelAttribute ProCmtLikeDTO proCmtLikeDTO) {
        return projectCommentService.commentLike(proCmtLikeDTO);
    }

    /* 댓글 좋아요 수 확인 */
    @GetMapping("/likeCount/{id}")
    public @ResponseBody int commentLikeCount(@PathVariable("id") Long id) {
        return projectCommentService.commentLikeCount(id);
    }

    /* 댓글 싫어요 */
    @PostMapping("/commentDisLike")
    public @ResponseBody boolean commentDisLike(@ModelAttribute ProCmtDisLikeDTO proCmtDisLikeDTO) {
        return projectCommentService.commentDisLike(proCmtDisLikeDTO);
    }

    /* 댓글 싫어요 수 확인 */
    @GetMapping("/disLikeCount/{id}")
    public @ResponseBody int commentDisLikeCount(@PathVariable("id") Long id) {
        return projectCommentService.commentDisLikeCount(id);
    }

    /* 댓글 좋아요를 눌렀을 때 싫어요 여부 확인 */
    @GetMapping("/checkCommentDisLike")
    public @ResponseBody int checkCommentDisLike(@ModelAttribute ProjectCommentDTO projectCommentDTO) {
        return projectCommentService.checkCommentDisLike(projectCommentDTO);
    }

    /* 댓글 싫어요를 눌렀을 때 좋어요 여부 확인 */
    @GetMapping("/checkCommentLike")
    public @ResponseBody int checkCommentLike(@ModelAttribute ProjectCommentDTO projectCommentDTO) {
        return projectCommentService.checkCommentLike(projectCommentDTO);
    }

    /* 댓글 수정하기 -- 추후에 수정해야함 !*/
    @PostMapping("update")
    public @ResponseBody void update(@ModelAttribute ProjectCommentDTO projectCommentDTO) {
        projectCommentService.update(projectCommentDTO);
    }


}


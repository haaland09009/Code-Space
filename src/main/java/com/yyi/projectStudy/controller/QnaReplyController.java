package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.QnaReplyCommentDTO;
import com.yyi.projectStudy.dto.QnaReplyDTO;
import com.yyi.projectStudy.dto.QnaReplyLikeDTO;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.service.QnaReplyCommentService;
import com.yyi.projectStudy.service.QnaReplyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qnaReply")
public class QnaReplyController {
    private final QnaReplyService qnaReplyService;
    private final QnaReplyCommentService qnaReplyCommentService;

    // 답변 작성
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute QnaReplyDTO qnaReplyDTO) {
        // enter 처리
        String content = qnaReplyDTO.getContent();
        content = content.replaceAll("\r\n", "<br>");
        qnaReplyDTO.setContent(content);
        //

        Long replyId = qnaReplyService.save(qnaReplyDTO);
        if (replyId != null) {
            List<QnaReplyDTO> qnaReplyDTOList = qnaReplyService.findAll(qnaReplyDTO.getQnaId());
            // enter 처리
            for (QnaReplyDTO dto : qnaReplyDTOList) {
                String replyContent = dto.getContent();
                replyContent = replyContent.replaceAll("<br>", "\n");
                dto.setContent(replyContent);
            }
            //
            return new ResponseEntity<>(qnaReplyDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }


    // 답글 조회
    @GetMapping("/getReplyList/{qnaId}")
    public ResponseEntity getReplyList(@PathVariable("qnaId") Long qnaId, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (qnaId != null) {
            List<QnaReplyDTO> qnaReplyDTOList = qnaReplyService.findAll(qnaId);
            for (QnaReplyDTO dto : qnaReplyDTOList) {
                String replyContent = dto.getContent();
                replyContent = replyContent.replaceAll("<br>", "\n");
                dto.setContent(replyContent);
                // 좋아요 수
                int likeCount = qnaReplyService.likeCount(dto.getId());
                dto.setLikeCount(likeCount);

                // 답변에 달린 댓글 가져오기
                List<QnaReplyCommentDTO> qnaReplyCommentDTOList = qnaReplyCommentService.findAll(dto.getId());
                List<QnaReplyCommentDTO> commentList = new ArrayList<>();
                for (QnaReplyCommentDTO qnaReplyCommentDTO : qnaReplyCommentDTOList) {
                    String commentContent = qnaReplyCommentDTO.getContent();
                    commentContent = commentContent.replaceAll("<br>", "\n");
                    qnaReplyCommentDTO.setContent(commentContent);
                    commentList.add(qnaReplyCommentDTO);
                }
                dto.setCommentList(commentList);

                // 댓글 수
                int commentCount = qnaReplyCommentService.commentCount(dto.getId());
                dto.setCommentCount(commentCount);

                if (sessionUser == null) {
                    dto.setIsLike(0);
                } else {
                    int isLike = qnaReplyService.checkReplyLikeForColor(dto.getId(), sessionUser.getId());
                    dto.setIsLike(isLike);
                }

            }
            return new ResponseEntity<>(qnaReplyDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // 답변 삭제
    @PostMapping("/delete/{id}")
    public @ResponseBody void delete(@PathVariable("id") Long id) {
        qnaReplyService.deleteById(id);
    }

    // 답글 수 조회
    @GetMapping("/count/{id}")
    public @ResponseBody int replyCount(@PathVariable("id") Long id) {
        return qnaReplyService.count(id);
    }

    // 본인이 작성한 답변인지의 여부 확인 (좋아요)
    @GetMapping("isYourReply")
    public @ResponseBody boolean isYourReply(@ModelAttribute QnaReplyDTO qnaReplyDTO) {
        Long userId = qnaReplyDTO.getUserId(); // sessionId
        Long writerId = qnaReplyService.isYourReply(qnaReplyDTO.getId());
        if (userId.equals(writerId)) {
            return true;
        } else {
            return false;
        }
    }

    // 답변 좋아요
    @PostMapping("like")
    public @ResponseBody void like(@ModelAttribute QnaReplyLikeDTO qnaReplyLikeDTO) {
        qnaReplyService.like(qnaReplyLikeDTO);
    }

    // 답변 좋아요 수 업데이트
    @GetMapping("likeCount/{id}")
    public @ResponseBody int likeCount(@PathVariable("id") Long id) {
        return qnaReplyService.likeCount(id);
    }

    // 사용자가 답변에 좋아요를 눌렀는지 확인 (색깔 변경 목적)
    @GetMapping("/checkReplyLikeForColor/{id}")
    public @ResponseBody boolean checkReplyLikeForColor(@PathVariable("id") Long id,
                                                      HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");

        int count = qnaReplyService.checkReplyLikeForColor(id, sessionUser.getId());
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }






}

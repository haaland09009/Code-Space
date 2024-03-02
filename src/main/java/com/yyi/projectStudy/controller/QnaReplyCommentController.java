package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.QnaReplyCommentDTO;
import com.yyi.projectStudy.dto.QnaReplyDTO;
import com.yyi.projectStudy.service.QnaReplyCommentService;
import com.yyi.projectStudy.service.QnaReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qnaReplyComment")
public class QnaReplyCommentController {
    private final QnaReplyCommentService qnaReplyCommentService;
    private final QnaReplyService qnaReplyService;

    // 댓글 작성
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute QnaReplyCommentDTO qnaReplyCommentDTO) {

        // enter 처리
        String content = qnaReplyCommentDTO.getContent();
        content = content.replaceAll("\r\n", "<br>");
        qnaReplyCommentDTO.setContent(content);

        Long commentId = qnaReplyCommentService.save(qnaReplyCommentDTO);
        if (commentId != null) {
            List<QnaReplyCommentDTO> qnaReplyCommentDTOList =
                    qnaReplyCommentService.findAll(qnaReplyCommentDTO.getReplyId());
            return new ResponseEntity<>(qnaReplyCommentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 답변이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // 답변 당 댓글 조회
    @GetMapping("/getCommentList/{id}")
    public ResponseEntity getCommentList(@PathVariable("id") Long id) {
        QnaReplyDTO qnaReplyDTO = qnaReplyService.findById(id);
        if (qnaReplyDTO != null) {
            List<QnaReplyCommentDTO> qnaReplyCommentDTOList = qnaReplyCommentService.findAll(id);
            for (QnaReplyCommentDTO qnaReplyCommentDTO : qnaReplyCommentDTOList) {
                // enter 처리
                String content = qnaReplyCommentDTO.getContent();
                content = content.replaceAll("<br>", "\n");
                qnaReplyCommentDTO.setContent(content);
            }
            return new ResponseEntity<>(qnaReplyCommentDTOList, HttpStatus.OK);
        } else {
            // 만약 답변이 삭제될 시 후처리 해야함.
            return new ResponseEntity<>("해당 답변이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // 어떤 답변에 댓글이 달린건지 확인 (답변 pk 추출)
    @GetMapping("/getReplyPk/{id}")
    public @ResponseBody Long getReplyPk(@PathVariable("id") Long id) {
        QnaReplyCommentDTO qnaReplyCommentDTO = qnaReplyCommentService.findById(id);
        QnaReplyDTO qnaReplyDTO = qnaReplyService.findById(qnaReplyCommentDTO.getReplyId());
        return qnaReplyDTO.getId();
    }

    // 댓글 삭제
    @PostMapping("/delete/{id}")
    public @ResponseBody void delete(@PathVariable("id") Long id) {
        qnaReplyCommentService.deleteById(id);
    }

    // 답변에 달린 댓글 수
    @GetMapping("/count/{id}")
    public @ResponseBody int commentCount(@PathVariable("id") Long id) {
        return qnaReplyCommentService.commentCount(id);
    }

}

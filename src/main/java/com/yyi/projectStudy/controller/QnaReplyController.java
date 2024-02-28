package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.QnaReplyDTO;
import com.yyi.projectStudy.service.QnaReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qnaReply")
public class QnaReplyController {
    private final QnaReplyService qnaReplyService;

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

}

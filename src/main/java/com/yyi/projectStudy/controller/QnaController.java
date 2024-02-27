package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.QnaDTO;
import com.yyi.projectStudy.dto.QnaTopicDTO;
import com.yyi.projectStudy.dto.TopicDTO;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.service.QnaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {
    private final QnaService qnaService;
    // 메인 페이지 - 기술, 커리어, 기타 모두 조회
    @GetMapping("")
    public String mainPage(Model model) {
        List<QnaDTO> qnaDTOList = qnaService.findAll();
        // 기술, 커리어, 기타 카테고리 여부 dto에 저장
        for (QnaDTO qnaDTO : qnaDTOList) {
            String content = qnaDTO.getContent();
//            content = content.replaceAll("<br>", " ");
            content = content.replaceAll("<br>", "\r\n");
            qnaDTO.setContent(content);
            TopicDTO topicDTO = qnaService.findTopic(qnaDTO.getId());
            qnaDTO.setCategoryName(topicDTO.getName());
        }
        model.addAttribute("qnaList", qnaDTOList);
        return "qna/list";
    }

    // 게시글 작성 폼
    @GetMapping("/write")
    public String writeForm(HttpSession session, Model model) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            List<TopicDTO> topicDTOList =  qnaService.findAllTopic();
            model.addAttribute("topicList", topicDTOList);
            return "qna/write";
        }
    }

    // 게시글 작성
    @PostMapping("/write")
    public String write(@ModelAttribute QnaTopicDTO qnaTopicDTO,
                        @ModelAttribute QnaDTO qnaDTO) {

        // enter 처리
        String content = qnaDTO.getContent();
        content = content.replaceAll("\r\n", "<br>");
        qnaDTO.setContent(content);
        // 게시글 저장 후 pk 가져오기
        Long savedId = qnaService.saveQna(qnaDTO);
        // qna - topic insert
        QnaDTO dto = qnaService.findById(savedId);
        TopicDTO topicDTO = qnaService.findByIdForTopic(qnaTopicDTO.getTopicId());
        qnaService.saveQnaTopic(dto, topicDTO);
        return "redirect:/qna";
    }


    // 게시글 상세보기
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        // 토픽 카테고리
        TopicDTO topicDTO = qnaService.findTopic(id);
        model.addAttribute("topic", topicDTO);

        // 게시글 정보
        QnaDTO qnaDTO = qnaService.findById(id);
        String content = qnaDTO.getContent();
//        content = content.replaceAll("<br>", "\r\n");
        content = content.replaceAll("<br>", "\n");
        qnaDTO.setContent(content);
        model.addAttribute("qna", qnaDTO);

        // 조회수 증가
        qnaService.updateReadCount(id);
        return "qna/detail";
    }


    // 게시글 삭제
    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        qnaService.deleteById(id);
        return "redirect:/qna";
    }

    // 게시글 수정 폼
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        if (sessionUser == null) {
            return "redirect:/user/loginPage";
        } else {
            // 모든 토픽 조회
            List<TopicDTO> topicDTOList =  qnaService.findAllTopic();
            model.addAttribute("topicList", topicDTOList);

            QnaDTO qnaDTO = qnaService.findById(id);
            String content = qnaDTO.getContent();
            content = content.replaceAll("<br>", "\n");
            qnaDTO.setContent(content);

            TopicDTO topicDTO = qnaService.findTopic(id);
            model.addAttribute("qna", qnaDTO);
            model.addAttribute("selectedTopic", topicDTO);
            return "qna/update";
        }
    }

    // 게시글 수정
    @PostMapping("/update")
    public String update(@ModelAttribute QnaDTO qnaDTO,
                         @ModelAttribute QnaTopicDTO qnaTopicDTO,
                         Model model) {
        QnaDTO updateQnaDTO = qnaService.updateQna(qnaDTO);

        String content = updateQnaDTO.getContent();
        content = content.replaceAll("<br>", "\r\n");
        updateQnaDTO.setContent(content);

        TopicDTO updateTopicDTO = qnaService.updateQnaTopic(qnaDTO.getId(), qnaTopicDTO);

        model.addAttribute("qna", updateQnaDTO);
        model.addAttribute("topic", updateTopicDTO);
        return "qna/detail";
    }


}

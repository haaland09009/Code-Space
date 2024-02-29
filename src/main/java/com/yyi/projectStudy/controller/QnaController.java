package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.*;
import com.yyi.projectStudy.service.QnaReplyService;
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
    private final QnaReplyService qnaReplyService;
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

            int replyCount = qnaReplyService.count(qnaDTO.getId());
            qnaDTO.setReplyCount(replyCount);
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
        return "redirect:/qna/" + savedId;
    }


    // 게시글 상세보기
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model, HttpSession session) {
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

        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");

        // 답변 불러오기
        List<QnaReplyDTO> qnaReplyDTOList = qnaReplyService.findAll(id);
        // enter 처리
        for (QnaReplyDTO dto : qnaReplyDTOList) {
            String replyContent = dto.getContent();
            replyContent = replyContent.replaceAll("<br>", "\n");
            dto.setContent(replyContent);
            int likeCount = qnaReplyService.likeCount(dto.getId());
            dto.setLikeCount(likeCount);
            if (sessionUser == null) {
                dto.setIsLike(0);
            } else {
                int isLike = qnaReplyService.checkReplyLikeForColor(dto.getId(), sessionUser.getId());
                dto.setIsLike(isLike);
            }
        }
        //
        model.addAttribute("replyList", qnaReplyDTOList);

        // 답변 수
        int replyCount = qnaReplyService.count(qnaDTO.getId());
        model.addAttribute("replyCount", replyCount);

        // 좋아요 수, 싫어요 수
        int likeCount = qnaService.likeCount(id);
        int dislikeCount = qnaService.disLikeCount(id);
        qnaDTO.setLikeCount(likeCount);
        qnaDTO.setDislikeCount(dislikeCount);

//        // 좋아요, 싫어요 여부

        if (sessionUser == null) {
            model.addAttribute("isLike", 0);
            model.addAttribute("isDisLike", 0);
        } else {
            int isLike = qnaService.checkQnaLikeForColor(id, sessionUser.getId());
            int isDisLike = qnaService.checkQnaDisLikeForColor(id, sessionUser.getId());
            model.addAttribute("isLike", isLike);
            model.addAttribute("isDisLike", isDisLike);
        }


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
                         Model model, HttpSession session) {
        QnaDTO updateQnaDTO = qnaService.updateQna(qnaDTO);

        String content = updateQnaDTO.getContent();
        content = content.replaceAll("<br>", "\r\n");
        updateQnaDTO.setContent(content);

        TopicDTO updateTopicDTO = qnaService.updateQnaTopic(qnaDTO.getId(), qnaTopicDTO);

        model.addAttribute("qna", updateQnaDTO);
        model.addAttribute("topic", updateTopicDTO);

        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        // 답변 불러오기
        List<QnaReplyDTO> qnaReplyDTOList = qnaReplyService.findAll(qnaDTO.getId());
        // enter 처리
        for (QnaReplyDTO dto : qnaReplyDTOList) {
            String replyContent = dto.getContent();
            replyContent = replyContent.replaceAll("<br>", "\n");
            dto.setContent(replyContent);
            int likeCount = qnaReplyService.likeCount(dto.getId());
            dto.setLikeCount(likeCount);
            if (sessionUser == null) {
                dto.setIsLike(0);
            } else {
                int isLike = qnaReplyService.checkReplyLikeForColor(dto.getId(), sessionUser.getId());
                dto.setIsLike(isLike);
            }
        }
        //
        model.addAttribute("replyList", qnaReplyDTOList);

        // 답변 수
        int replyCount = qnaReplyService.count(qnaDTO.getId());
        model.addAttribute("replyCount", replyCount);

        // 좋아요 수, 싫어요 수
        int likeCount = qnaService.likeCount(qnaDTO.getId());
        int dislikeCount = qnaService.disLikeCount(qnaDTO.getId());
        updateQnaDTO.setLikeCount(likeCount);
        updateQnaDTO.setDislikeCount(dislikeCount);

//        // 좋아요, 싫어요 여부

        if (sessionUser == null) {
            model.addAttribute("isLike", 0);
            model.addAttribute("isDisLike", 0);
        } else {
            int isLike = qnaService.checkQnaLikeForColor(qnaDTO.getId(), sessionUser.getId());
            int isDisLike = qnaService.checkQnaDisLikeForColor(qnaDTO.getId(), sessionUser.getId());
            model.addAttribute("isLike", isLike);
            model.addAttribute("isDisLike", isDisLike);
        }

        return "qna/detail";
    }

    // 본인이 작성한 게시글인지 확인 (만약 눌렀을 때 삭제된 게시물일 경우 후처리 해야함)
    @GetMapping("/isYourQna")
    public @ResponseBody boolean isYourQna(@ModelAttribute QnaDTO qnaDTO) {

        Long userId = qnaDTO.getUserId(); // sessionId
        Long writerId = qnaService.isYourQna(qnaDTO.getId());
        if (userId.equals(writerId)) {
            return true;
        } else {
            return false;
        }
    }

    // 게시글 좋아요를 눌렀을 때 싫어요 여부 확인
    @GetMapping("/checkQnaDisLike")
    public @ResponseBody int checkQnaDisLike(@ModelAttribute QnaDTO qnaDTO) {
        return qnaService.checkQnaDisLike(qnaDTO);
    }


    // 게시글 좋아요 클릭
    @PostMapping("/like")
    public @ResponseBody void like(@ModelAttribute QnaLikeDTO qnaLikeDTO) {
        qnaService.like(qnaLikeDTO);
    }

    // 댓글 좋아요 수 확인
    @GetMapping("/likeCount/{id}")
    public @ResponseBody int qnaLikeCount(@PathVariable("id") Long id) {
        return qnaService.likeCount(id);
    }

    // 게시글 싫어요를 눌렀을 때 좋어요 여부 확인
    @GetMapping("/checkQnaLike")
    public @ResponseBody int checkQnaLike(@ModelAttribute QnaDTO qnaDTO) {
        return qnaService.checkQnaLike(qnaDTO);
    }

    // 게시글 싫어요 클릭
    @PostMapping("/dislike")
    public @ResponseBody void disLike(@ModelAttribute QnaDisLikeDTO qnaDisLikeDTO) {
        qnaService.disLike(qnaDisLikeDTO);
    }

    // 게시글 싫어요 수 확인
    @GetMapping("/disLikeCount/{id}")
    public @ResponseBody int qnaDisLikeCount(@PathVariable("id") Long id) {
        return qnaService.disLikeCount(id);
    }

    // 사용자가 게시글에 좋아요를 눌렀는지 확인 (색깔 변경 목적)
    @GetMapping("/checkQnaLikeForColor/{id}")
    public @ResponseBody boolean checkQnaLikeForColor(@PathVariable("id") Long id,
            HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");

        int count = qnaService.checkQnaLikeForColor(id, sessionUser.getId());
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // 사용자가 게시글에 싫어요를 눌렀는지 확인 (색깔 변경 목적)
    @GetMapping("/checkQnaDisLikeForColor/{id}")
    public @ResponseBody boolean checkQnaDisLikeForColor(@PathVariable("id") Long id,
                                                         HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        int count = qnaService.checkQnaDisLikeForColor(id, sessionUser.getId());
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }



}

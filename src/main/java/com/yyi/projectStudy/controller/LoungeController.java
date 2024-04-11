package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.JobDTO;
import com.yyi.projectStudy.dto.LoungeDTO;
import com.yyi.projectStudy.dto.LoungeLikeDTO;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.service.LoungeService;
import com.yyi.projectStudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lounge")
@RequiredArgsConstructor
public class LoungeController {
    private final LoungeService loungeService;

    @GetMapping("")
    public String mainPage(Model model, HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        List<LoungeDTO> loungeDTOList = loungeService.findAll();
        for (LoungeDTO loungeDTO : loungeDTOList) {
            /* 게시글 좋아요 여부 (아이콘) */
            if (sessionUser == null) {
                loungeDTO.setLikeYn(0);
            } else {
                int likeYn = loungeService.checkLike(loungeDTO.getId(), sessionUser.getId());
                loungeDTO.setLikeYn(likeYn);
            }
        }
        model.addAttribute("loungeList", loungeDTOList);
        return "lounge/list";
    }

    /* 게시글 목록 조회 */
    @GetMapping("/list")
    public ResponseEntity list(HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        List<LoungeDTO> loungeDTOList = loungeService.findAll();
        for (LoungeDTO loungeDTO : loungeDTOList) {
            /* 게시글 좋아요 여부 (아이콘) */
            if (sessionUser == null) {
                loungeDTO.setLikeYn(0);
            } else {
                int likeYn = loungeService.checkLike(loungeDTO.getId(), sessionUser.getId());
                loungeDTO.setLikeYn(likeYn);
            }
        }
        return new ResponseEntity<>(loungeDTOList, HttpStatus.OK);
    }

    /* 게시글 작성 */
    @PostMapping("/save")
    public @ResponseBody void save(@ModelAttribute LoungeDTO loungeDTO) {
        String content = loungeDTO.getContent();
        content = content.replaceAll("\r\n", "<br>");
        loungeDTO.setContent(content);

        loungeService.save(loungeDTO);
    }

    /* 게시글 삭제 */
    @PostMapping("/delete/{id}")
    public @ResponseBody void deleteById(@PathVariable("id") Long id) {
        loungeService.deleteById(id);
    }

    /* 본인이 작성한 게시글인지 확인 */
    @GetMapping("/isYourBoard")
    public @ResponseBody boolean isYourBoard(@ModelAttribute LoungeDTO loungeDTO) {
        return loungeService.isYourBoard(loungeDTO);
    }

    /* 게시글 좋아요 */
    @PostMapping("/like")
    public @ResponseBody boolean like(@ModelAttribute LoungeLikeDTO loungeLikeDTO) {
        return loungeService.like(loungeLikeDTO);
    }

    /* 게시글 좋아요 개수 */
    @GetMapping("/likeCount/{id}")
    public @ResponseBody int likeCount(@PathVariable("id") Long id) {
        return loungeService.likeCount(id);
    }


}

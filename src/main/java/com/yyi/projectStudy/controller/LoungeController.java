package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.JobDTO;
import com.yyi.projectStudy.dto.LoungeDTO;
import com.yyi.projectStudy.service.LoungeService;
import com.yyi.projectStudy.service.UserService;
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
    public String mainPage(Model model) {
        List<LoungeDTO> loungeDTOList = loungeService.findAll();
        model.addAttribute("loungeList", loungeDTOList);
        return "lounge/list";
    }

    /* 게시글 목록 조회 */
    @GetMapping("/list")
    public ResponseEntity list() {
        List<LoungeDTO> loungeDTOList = loungeService.findAll();
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

}

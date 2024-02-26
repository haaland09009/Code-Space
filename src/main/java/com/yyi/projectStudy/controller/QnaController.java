package com.yyi.projectStudy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

    @GetMapping("")
    public String mainPage() {
        return "qna/list";
    }

    @GetMapping("/write")
    public String writeForm() {
        return "qna/write";
    }
}

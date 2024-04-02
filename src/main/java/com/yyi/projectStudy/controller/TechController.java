package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.TechCategoryDTO;
import com.yyi.projectStudy.service.TechService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tech")
public class TechController {
    private final TechService techService;
    /* 기술스택 선택 리스트 조회 */
    @PostMapping("/list")
    public ResponseEntity selectTechList(@RequestParam(value = "techIdList[]", required = false) List<Long> techIdList) {
        if (techIdList == null) {
            techIdList = new ArrayList<>();
        }
        List<TechCategoryDTO> techCategoryDTOList = techService.selectTechList(techIdList);
        return new ResponseEntity<>(techCategoryDTOList, HttpStatus.OK);
    }
}

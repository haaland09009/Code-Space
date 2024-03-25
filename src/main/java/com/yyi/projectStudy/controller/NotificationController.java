package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.NotificationDTO;
import com.yyi.projectStudy.service.NotificationService;
import com.yyi.projectStudy.time.StringToDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    /* 회원 당 알림 조회 */
    @GetMapping("/findAll/{id}")
    public ResponseEntity findAll(@PathVariable("id") Long id) {
        List<NotificationDTO> notificationDTOList = notificationService.findAll(id);
        if (notificationDTOList != null) {
            for (NotificationDTO notificationDTO : notificationDTOList) {
                /* 날짜 변환하기 */
                String formatDateTime = StringToDate.formatDateTime(String.valueOf(notificationDTO.getRegDate()));
                notificationDTO.setFormattedDate(formatDateTime);
            }
            return new ResponseEntity<>(notificationDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("알림이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    /* 회원 당 안 읽은 알림 수 조회 */
    @GetMapping("notReadCount/{id}")
    public @ResponseBody int notReadCount(@PathVariable("id") Long id) {
        return notificationService.notReadCount(id);
    }


}

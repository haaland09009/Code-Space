package com.yyi.projectStudy.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageDTO {
    private Long id;
    private Long receiverId;
    private Long senderId;
    private LocalDateTime sendTime;
    private LocalDateTime readTime;
    private String content;
}

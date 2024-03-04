package com.yyi.projectStudy.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QnaBestDTO {
    private Long id;
    private String title;
    private int replyCount;
    private int likeCount;
    private String topicName;

    private String writer;
    private LocalDateTime regDate;
    private int commentCount;
    private int fileAttached;
    private String storedFileName;

    private String categoryName;
}

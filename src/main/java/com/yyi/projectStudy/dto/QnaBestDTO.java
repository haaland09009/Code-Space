package com.yyi.projectStudy.dto;

import lombok.*;

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
}

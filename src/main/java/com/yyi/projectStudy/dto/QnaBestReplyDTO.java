package com.yyi.projectStudy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QnaBestReplyDTO {
    private Long id;
    private String title;

    private int replyCount; // 답변 개수
    private int likeCount; // 답변의 좋아요 개수
    private String topicName; // 토픽 종류
}

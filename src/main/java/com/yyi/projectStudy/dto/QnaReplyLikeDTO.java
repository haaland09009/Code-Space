package com.yyi.projectStudy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QnaReplyLikeDTO {
    private Long id;
    private Long replyId;
    private Long userId;
}

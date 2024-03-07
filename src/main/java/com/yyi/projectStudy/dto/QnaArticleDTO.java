package com.yyi.projectStudy.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QnaArticleDTO {
    private Long qnaId;
    private Long replyId;
    private Long commentId;
    private String title;
    private LocalDateTime regDate;
    private String replyContent;
    private String commentContent;

    private String categoryName;
    private Long boardId;
}

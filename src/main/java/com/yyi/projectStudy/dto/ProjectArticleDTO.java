package com.yyi.projectStudy.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectArticleDTO {
    private Long projectId; // 게시물 id
    private Long commentId; // 댓글 id
    private String title; // 게시물 제목 (댓글, 게시물 확인 여부)
    private String projectContent;
    private String commentContent;
    private LocalDateTime regDate;

    private String projectStudy;
    private List<TechCategoryDTO> techList = new ArrayList<>();

}

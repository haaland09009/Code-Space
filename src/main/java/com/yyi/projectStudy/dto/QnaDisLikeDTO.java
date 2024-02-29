package com.yyi.projectStudy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QnaDisLikeDTO {
    private Long id;
    private Long qnaId;
    private Long userId;
}

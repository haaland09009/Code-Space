package com.yyi.projectStudy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QnaLikeDTO {
    private Long id;
    private Long qnaId;
    private Long userId;
}

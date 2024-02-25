package com.yyi.projectStudy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProCmtDisLikeDTO {
    private Long id;
    private Long commentId;
    private Long userId;
}

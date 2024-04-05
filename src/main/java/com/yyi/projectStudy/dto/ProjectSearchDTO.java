package com.yyi.projectStudy.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectSearchDTO {
    private List<Long> techIdList;
    private Long positionId;
    private String status;
    private Long categoryId;
    private String clipYn;
    private Long userId;
}

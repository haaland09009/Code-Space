package com.yyi.projectStudy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopWritersDTO {
    private Long userId;
    private int totalCount;

    private String nickname;
    private int fileAttached;
    private String storedFileName;
}

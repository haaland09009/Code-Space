package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.LoungeEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoungeDTO {
    private Long id;
    private Long userId;
    private String content;
    private LocalDateTime regDate;

    private String writer;
    private String jobName;
    private int fileAttached;
    private String storedFileName;

    private String formattedDate;

    public static LoungeDTO toLoungeDTO(LoungeDTO loungeDTO, LoungeEntity loungeEntity) {
        loungeDTO.setId(loungeEntity.getId());
        loungeDTO.setUserId(loungeEntity.getUserEntity().getId());
        loungeDTO.setContent(loungeEntity.getContent());
        loungeDTO.setRegDate(loungeEntity.getRegDate());
        return loungeDTO;
    }
}

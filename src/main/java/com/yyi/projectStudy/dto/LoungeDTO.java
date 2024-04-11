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
    private int likeCount;

    /* 게시글 좋아요 여부 */
    private int likeYn;

    public static LoungeDTO toLoungeDTO(LoungeDTO loungeDTO, LoungeEntity loungeEntity) {
        loungeDTO.setId(loungeEntity.getId());
        loungeDTO.setUserId(loungeEntity.getUserEntity().getId());
        loungeDTO.setContent(loungeEntity.getContent());
        loungeDTO.setRegDate(loungeEntity.getRegDate());
        return loungeDTO;
    }
}

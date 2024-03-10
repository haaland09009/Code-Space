package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.QnaClipEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QnaClipDTO {
    private Long id;
    private Long userId;
    private Long qnaId;
    private LocalDateTime regDate;

    private String title;
    private String categoryName;

    public static QnaClipDTO toQnaClipDTO(QnaClipEntity qnaClipEntity) {
        QnaClipDTO qnaClipDTO = new QnaClipDTO();
        qnaClipDTO.setId(qnaClipEntity.getId());
        qnaClipDTO.setUserId(qnaClipEntity.getUserEntity().getId());
        qnaClipDTO.setQnaId(qnaClipEntity.getQnaEntity().getId());
        qnaClipDTO.setRegDate(qnaClipEntity.getRegDate());
        return qnaClipDTO;
    }
}

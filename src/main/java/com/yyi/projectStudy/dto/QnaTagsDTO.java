package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.QnaTagsEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QnaTagsDTO {
    private Long id;
    private Long qnaId;
    private String tag;

    public static QnaTagsDTO toQnaTagsDTO(QnaTagsEntity qnaTagsEntity) {
        QnaTagsDTO qnaTagsDTO = new QnaTagsDTO();
        qnaTagsDTO.setId(qnaTagsEntity.getId());
        qnaTagsDTO.setQnaId(qnaTagsEntity.getQnaEntity().getId());
        qnaTagsDTO.setTag(qnaTagsEntity.getTag());
        return qnaTagsDTO;
    }
}

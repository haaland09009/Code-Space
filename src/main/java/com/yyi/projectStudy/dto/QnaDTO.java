package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.QnaEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QnaDTO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private int readCount;
    private LocalDateTime regDate;
    private LocalDateTime updDate;

    private String writer;
    private String categoryName;
    private int fileAttached;
    private String storedFileName;
    private int replyCount;
    private String formattedDate;

    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private String hashTag;



    public static QnaDTO toQnaDTO(QnaEntity qnaEntity) {
        QnaDTO qnaDTO = new QnaDTO();
        qnaDTO.setId(qnaEntity.getId());
        qnaDTO.setUserId(qnaEntity.getUserEntity().getId());
        qnaDTO.setTitle(qnaEntity.getTitle());
        qnaDTO.setContent(qnaEntity.getContent());
        qnaDTO.setReadCount(qnaEntity.getReadCount());
        qnaDTO.setRegDate(qnaEntity.getRegDate());
        qnaDTO.setUpdDate(qnaEntity.getUpdDate());

        // 게시글 작성자 정보
        qnaDTO.setWriter(qnaEntity.getUserEntity().getNickname());
        qnaDTO.setFileAttached(qnaEntity.getUserEntity().getFileAttached());
        if (qnaEntity.getUserEntity().getFileAttached() == 1) {
            qnaDTO.setStoredFileName(qnaEntity.getUserEntity().getUserImageFileEntityList().get(0).getStoredFileName());
        }
        return qnaDTO;
    }
}

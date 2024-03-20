package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.QnaReplyCommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QnaReplyCommentDTO {
    private Long id;
    private Long replyId;
    private Long userId;
    private String content;
    private LocalDateTime regDate;
    /* 수정 여부 */
    private LocalDateTime updDate;

    private String writer;
    private int fileAttached;
    private String storedFileName;
    private String jobName;
    public static QnaReplyCommentDTO toQnaReplyCommentDTO(QnaReplyCommentEntity qnaReplyCommentEntity) {
        QnaReplyCommentDTO qnaReplyCommentDTO = new QnaReplyCommentDTO();
        qnaReplyCommentDTO.setId(qnaReplyCommentEntity.getId());
        qnaReplyCommentDTO.setReplyId(qnaReplyCommentEntity.getQnaReplyEntity().getId());
        qnaReplyCommentDTO.setUserId(qnaReplyCommentEntity.getUserEntity().getId());
        qnaReplyCommentDTO.setContent(qnaReplyCommentEntity.getContent());
        qnaReplyCommentDTO.setRegDate(qnaReplyCommentEntity.getRegDate());
        qnaReplyCommentDTO.setUpdDate(qnaReplyCommentEntity.getUpdDate());

        qnaReplyCommentDTO.setWriter(qnaReplyCommentEntity.getUserEntity().getNickname());
        qnaReplyCommentDTO.setFileAttached(qnaReplyCommentEntity.getUserEntity().getFileAttached());
        if (qnaReplyCommentEntity.getUserEntity().getFileAttached() == 1) {
            qnaReplyCommentDTO.setStoredFileName(qnaReplyCommentEntity.getUserEntity().getUserImageFileEntityList().get(0).getStoredFileName());
        }
        return qnaReplyCommentDTO;
    }
}

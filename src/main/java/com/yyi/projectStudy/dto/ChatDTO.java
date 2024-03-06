package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.ChatEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatDTO {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime readDate;

    private int fileAttached;
    private String storedFileName;
    private String nickname;
    private Long friendId; // 상대방
    private int isNotReadCount; // 최근 채팅방 목록에서 안 읽은 채팅 수

    public static ChatDTO toChatDTO(ChatEntity chatEntity) {
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setId(chatEntity.getId());
        chatDTO.setRoomId(chatEntity.getChatRoomEntity().getId());
        chatDTO.setSenderId(chatEntity.getSender().getId());
        chatDTO.setContent(chatEntity.getContent());
        chatDTO.setRegDate(chatEntity.getRegDate());
        chatDTO.setReadDate(chatEntity.getReadDate());
        return chatDTO;
    }
}

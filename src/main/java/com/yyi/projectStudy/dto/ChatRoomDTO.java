package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.ChatRoomEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRoomDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime regDate;

    private List<ChatDTO> chatList;

    // 상대 사용자 정보
    private int fileAttached;
    private String writer;
    private String storedFileName;

    public static ChatRoomDTO toChatRoomDTO(ChatRoomEntity chatRoomEntity) {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setId(chatRoomEntity.getId());
        chatRoomDTO.setSenderId(chatRoomEntity.getSender().getId());
        chatRoomDTO.setReceiverId(chatRoomEntity.getReceiver().getId());
        chatRoomDTO.setRegDate(chatRoomEntity.getRegDate());
        return chatRoomDTO;
    }
}

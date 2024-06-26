package com.yyi.projectStudy.entity;

import com.yyi.projectStudy.dto.ChatDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat_table")
public class ChatEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoomEntity chatRoomEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column
    private LocalDateTime readDate;

    public static ChatEntity toChatEntity(ChatDTO chatDTO, ChatRoomEntity chatRoomEntity,
                                          UserEntity sender) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setChatRoomEntity(chatRoomEntity);
        chatEntity.setSender(sender);
        chatEntity.setContent(chatDTO.getContent());
        return chatEntity;
    }
}

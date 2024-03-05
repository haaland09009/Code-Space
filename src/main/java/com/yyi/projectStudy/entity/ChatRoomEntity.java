package com.yyi.projectStudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(
        name = "chat_room_seq_generator"
        , sequenceName = "chat_room_seq"
        , initialValue = 1
        , allocationSize = 1
)
@Table(name = "chat_room_table")
public class ChatRoomEntity extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "chat_room_seq_generator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @Column
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "chatRoomEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChatEntity> chatEntityList
            = new ArrayList<>();

    public static ChatRoomEntity toChatRoomEntity(UserEntity sender, UserEntity receiver) {
        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
        chatRoomEntity.setSender(sender);
        chatRoomEntity.setReceiver(receiver);
        return chatRoomEntity;
    }
}

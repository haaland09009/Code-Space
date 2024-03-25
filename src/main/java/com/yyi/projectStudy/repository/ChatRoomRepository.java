package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ChatRoomEntity;
import com.yyi.projectStudy.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    /* 기존 채팅방이 존재하는지 여부 조회 */
    @Query(value = "select * from chat_room_table c " +
            "where (c.receiver_id = :receiver_id and c.sender_id = :sender_id)" +
            "or (c.receiver_id = :sender_id and c.sender_id = :receiver_id)", nativeQuery = true)
    Optional<ChatRoomEntity> existsChatRoom(@Param("receiver_id") Long receiverId,
                                            @Param("sender_id") Long senderId);

    /* 사용자가 보유한 모든 채팅방 조회 (나중에 마지막 채팅순으로 조회해야함) */
    List<ChatRoomEntity> findBySenderOrReceiver(UserEntity sender, UserEntity receiver);

}

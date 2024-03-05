package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ChatEntity;
import com.yyi.projectStudy.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    // 채팅방에 있는 채팅 기록 모두 조회
    // select * from chat_table where room_id = ? order by id asc;
    List<ChatEntity> findByChatRoomEntityOrderByIdAsc(ChatRoomEntity chatRoomEntity);

    // 채팅방 조회
    @Query(value =
    "SELECT room_id,\n" +
            "       chat_id as id,\n" +
            "       reg_date,\n" +
            "       content,\n" +
            "       read_date,\n" +
            "       sender_id\n" +
            "FROM (\n" +
            "    SELECT cr.id AS room_id,\n" +
            "           c.id AS chat_id,\n" +
            "           c.reg_date,\n" +
            "           c.content,\n" +
            "           c.read_date,\n" +
            "           c.sender_id,\n" +
            "           ROW_NUMBER() OVER (PARTITION BY cr.id ORDER BY c.reg_date DESC) AS row_num\n" +
            "    FROM chat_room_table cr\n" +
            "    JOIN chat_table c ON cr.id = c.room_id\n" +
            "    WHERE cr.receiver_id = :userId OR cr.sender_id = :userId\n" +
            ")\n" +
            "WHERE row_num = 1\n" +
            "ORDER BY reg_date DESC", nativeQuery = true)
    List<ChatEntity> findRecentChats(@Param("userId") Long userId);
}

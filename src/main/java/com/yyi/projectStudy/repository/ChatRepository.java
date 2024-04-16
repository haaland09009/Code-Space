package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.ChatEntity;
import com.yyi.projectStudy.entity.ChatRoomEntity;
import com.yyi.projectStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    /* 채팅방에 있는 채팅 기록 모두 조회 */
    List<ChatEntity> findByChatRoomEntityOrderByIdAsc(ChatRoomEntity chatRoomEntity);

    /* 채팅방 조회 */
    @Query(value = "SELECT room_id,\n" +
            "       chat_id AS id,\n" +
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
            "           (SELECT COUNT(*) FROM chat_table c2 WHERE c2.room_id = cr.id AND c2.reg_date >= c.reg_date) AS row_num\n" +
            "    FROM chat_room_table cr\n" +
            "    JOIN chat_table c ON cr.id = c.room_id\n" +
            "    WHERE cr.receiver_id = :userId OR cr.sender_id = :userId\n" +
            ") AS sub\n" +
            "WHERE row_num = 1\n" +
            "ORDER BY reg_date DESC", nativeQuery = true)
    List<ChatEntity> findRecentChats(@Param("userId") Long userId);

    /* 안 읽은 채팅이 있는 채팅방만 조회 */
        @Query(value = "SELECT \n" +
                "    cr.id AS room_id,\n" +
                "    c.id AS id,\n" +
                "    c.reg_date,\n" +
                "    c.content,\n" +
                "    c.read_date,\n" +
                "    c.sender_id\n" +
                "FROM \n" +
                "    chat_room_table cr\n" +
                "JOIN \n" +
                "    chat_table c ON cr.id = c.room_id\n" +
                "WHERE \n" +
                "    (cr.sender_id = :userId OR cr.receiver_id = :userId)\n" +
                "    AND c.sender_id != :userId\n" +
                "    AND c.read_date IS NULL\n" +
                "    AND NOT EXISTS (\n" +
                "        SELECT 1 \n" +
                "        FROM chat_table c2 \n" +
                "        WHERE c2.room_id = cr.id \n" +
                "        AND c2.reg_date > c.reg_date\n" +
                "    )\n" +
                "ORDER BY \n" +
                "    c.reg_date DESC", nativeQuery = true)
    List<ChatEntity> unreadChatList(@Param("userId") Long userId);


    /* 채팅방 중에서 안 읽은 채팅 개수 확인 */
    @Query(value = "select count(*) from ChatEntity c where c.chatRoomEntity = :chatRoomEntity " +
                    "and c.sender != :userEntity and c.readDate is null")
    int countChatIsNotRead(ChatRoomEntity chatRoomEntity, UserEntity userEntity);

    /* 채팅방 중에서 안 읽은 채팅 pk 확인*/
    @Query(value = "select c from ChatEntity c where c.chatRoomEntity = :chatRoomEntity " +
                    "and c.sender != :userEntity and c.readDate is null")
    List<ChatEntity> checkChatIsNotRead(ChatRoomEntity chatRoomEntity, UserEntity userEntity);


    /* 채팅방 접속 - 채팅 읽음 처리 */
    @Modifying
    @Query(value = "update ChatEntity c set c.readDate = sysdate where id = :id")
    void readChat(@Param("id") Long id);


    /* 안 읽은 채팅 총 개수 */
    @Query(value = "select count(*) from ChatEntity ct\n" +
                    "join ct.chatRoomEntity cr\n" +
                    "where ct.sender != :userEntity and\n" +
                    "(cr.sender = :userEntity or cr.receiver = :userEntity)\n" +
                    "and ct.readDate is null")
    int notReadCount(UserEntity userEntity);


}

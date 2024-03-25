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
    /* select * from chat_table where room_id = ? order by id asc;*/
    List<ChatEntity> findByChatRoomEntityOrderByIdAsc(ChatRoomEntity chatRoomEntity);

    /* 채팅방 조회 */
/*    SELECT room_id,
    chat_id as id,
    reg_date,
    content,
    read_date,
    sender_id
    FROM (
            SELECT cr.id AS room_id,
            c.id AS chat_id,
            c.reg_date,
            c.content,
            c.read_date,
            c.sender_id,
            ROW_NUMBER() OVER (PARTITION BY cr.id ORDER BY c.reg_date DESC) AS row_num
    FROM chat_room_table cr
    JOIN chat_table c ON cr.id = c.room_id
    WHERE cr.receiver_id = 43 OR cr.sender_id = 43
            )
    WHERE row_num = 1
    ORDER BY reg_date DESC;*/
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

    /* 안 읽은 채팅이 있는 채팅방만 조회 */
  /*   SELECT
    room_id,
    chat_id AS id,
    reg_date,
    content,
    read_date,
    sender_id
FROM (
    SELECT
        cr.id AS room_id,
        c.id AS chat_id,
        c.reg_date,
        c.content,
        c.read_date,
        c.sender_id,
        ROW_NUMBER() OVER (PARTITION BY cr.id ORDER BY c.reg_date DESC) AS row_num
    FROM
        chat_room_table cr
    JOIN
        chat_table c ON cr.id = c.room_id
    WHERE
        (cr.sender_id = 123 OR cr.receiver_id = 123)
        AND c.sender_id != 123
        AND c.read_date IS NULL
) subquery
WHERE
    row_num = 1
ORDER BY
    reg_date DESC */
    @Query(value = "SELECT\n" +
            "    room_id,\n" +
            "    chat_id AS id,\n" +
            "    reg_date,\n" +
            "    content,\n" +
            "    read_date,\n" +
            "    sender_id\n" +
            "FROM (\n" +
            "    SELECT\n" +
            "        cr.id AS room_id,\n" +
            "        c.id AS chat_id,\n" +
            "        c.reg_date,\n" +
            "        c.content,\n" +
            "        c.read_date,\n" +
            "        c.sender_id,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY cr.id ORDER BY c.reg_date DESC) AS row_num\n" +
            "    FROM\n" +
            "        chat_room_table cr\n" +
            "    JOIN\n" +
            "        chat_table c ON cr.id = c.room_id\n" +
            "    WHERE\n" +
            "        (cr.sender_id = :userId OR cr.receiver_id = :userId)\n" +
            "        AND c.sender_id != :userId\n" +
            "        AND c.read_date IS NULL\n" +
            ") subquery\n" +
            "WHERE\n" +
            "    row_num = 1\n" +
            "ORDER BY\n" +
            "    reg_date DESC", nativeQuery = true)
    List<ChatEntity> unreadChatList(@Param("userId") Long userId);


    /* 채팅방 중에서 안 읽은 채팅 개수 확인 */
    /* SELECT count(*) FROM chat_table where room_id = 2 and sender_id != 43 and read_date is null; */
    @Query(value = "select count(*) from chat_table where room_id = :roomId and sender_id != :userId and read_date is null", nativeQuery = true)
    int countChatIsNotRead(@Param("roomId") Long roomId,
                           @Param("userId") Long userId);

    /* 채팅방 중에서 안 읽은 채팅 pk 확인*/
    /* SELECT * FROM chat_table where room_id = 2 and sender_id != 43 and read_date is null; */
    @Query(value = "select * from chat_table where room_id = :roomId and sender_id != :userId and read_date is null", nativeQuery = true)
    List<ChatEntity> checkChatIsNotRead(@Param("roomId") Long roomId,
                                        @Param("userId") Long userId);

    /* 채팅방 접속 - 채팅 읽음 처리 */
    @Modifying
    @Query(value = "update ChatEntity c set c.readDate = sysdate where id = :id")
    void readChat(@Param("id") Long id);


    /* 안 읽은 채팅 총 개수 */
/*    SELECT COUNT(*) FROM chat_table ct
    JOIN chat_room_table cr ON ct.room_id = cr.id
    WHERE ct.sender_id != 43 AND
            (cr.sender_id = 43 OR cr.receiver_id = 43)
    AND ct.read_date IS NULL

    select count(*) from ChatEntity ct
    JOIN ChatRoomEntity cr
    WHERE ct.sender != userEntity AND
            (cr.sender = :userEntity OR cr.receiver = :userEntity)
    AND ct.readDate is null */
    @Query(value = "select count(*) from ChatEntity ct\n" +
            "join ct.chatRoomEntity cr\n" +
            "where ct.sender != :userEntity and\n" +
            "(cr.sender = :userEntity or cr.receiver = :userEntity)\n" +
            "and ct.readDate is null")
    int notReadCount(UserEntity userEntity);
}

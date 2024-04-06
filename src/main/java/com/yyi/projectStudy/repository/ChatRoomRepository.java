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
    @Query(value = "select c from ChatRoomEntity c " +
                    "where (c.receiver = :receiver and c.sender = :sender) " +
                    "or (c.receiver = :sender and c.sender = :receiver)")
    Optional<ChatRoomEntity> existsChatRoom(UserEntity receiver, UserEntity sender);



}

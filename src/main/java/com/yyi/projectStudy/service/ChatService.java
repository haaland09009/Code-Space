package com.yyi.projectStudy.service;


import com.yyi.projectStudy.dto.ChatDTO;
import com.yyi.projectStudy.dto.ChatRoomDTO;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.entity.*;
import com.yyi.projectStudy.repository.*;
import com.yyi.projectStudy.time.StringToDate;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final UserJobRepository userJobRepository;
    private final JobRepository jobRepository;


   /* 메시지 전송하기 */
    @Transactional
    public Long save(ChatDTO chatDTO, UserDTO senderDTO, UserDTO receiverDTO) {
        /* 발신자 정보 */
        UserEntity sender = UserEntity.toUserEntity(senderDTO);
        /* 수신자 정보 */
        UserEntity receiver = UserEntity.toUserEntity(receiverDTO);

        /* 해당 발신자와 수신자 정보로 등록된 채팅방이 존재하는지 조회 */
        Optional<ChatRoomEntity> optionalChatRoomEntity = chatRoomRepository.existsChatRoom(receiver.getId(), sender.getId());
        if (optionalChatRoomEntity.isPresent()) {
            /* 기존 채팅방이 이미 존재한다면 */
            ChatRoomEntity chatRoomEntity = optionalChatRoomEntity.get();
            /* 기존 채팅방에 새로운 메시지 저장 - 채팅 dto -> entity 변환 */
            ChatEntity chatEntity = ChatEntity.toChatEntity(chatDTO, chatRoomEntity, sender);
            chatRepository.save(chatEntity);
            /* 채팅방 pk 반환 */
            return chatRoomEntity.getId();
        } else {
            /* 기존 채팅방이 이미 존재하지 않다면 새로운 채팅방을 생성하고 채팅방 pk 반환 */
            ChatRoomEntity roomEntity = ChatRoomEntity.toChatRoomEntity(sender, receiver);
            Long roomId = chatRoomRepository.save(roomEntity).getId();

            /* 새로 생성된 채팅방 entity 조회 */
            ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId).get();
            /* 전송된 메시지 저장 */
            ChatEntity chatEntity = ChatEntity.toChatEntity(chatDTO, chatRoomEntity, sender);
            chatRepository.save(chatEntity);
            return chatRoomEntity.getId();
        }
    }


    /* 채팅방에 있는 메시지 기록 모두 조회 */
    @Transactional
    public List<ChatDTO> findAll(Long roomId, Long sessionId) {
        /* 해당 id로 채팅방번호 (pk) 조회 (나중에 예외처리) */
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId).get();

        /* 상대방 entity (채팅방에 보여질 상대방 정보) */
        UserEntity friend;

        /* 발신자 */
        UserEntity sender = chatRoomEntity.getSender();
        /* 수신자 */
        UserEntity receiver = chatRoomEntity.getReceiver();

        ChatDTO chatDTO = new ChatDTO();
        if (!sessionId.equals(sender.getId())) {
            friend = sender;
            chatDTO.setNickname(friend.getNickname());
            chatDTO.setFileAttached(friend.getFileAttached());
            chatDTO.setFriendId(friend.getId());
            if (friend.getFileAttached() == 1) {
                chatDTO.setStoredFileName(friend.getUserImageFileEntityList().get(0).getStoredFileName());
            }
        } else if (!sessionId.equals(receiver.getId())) {
            friend = receiver;
            chatDTO.setNickname(friend.getNickname());
            chatDTO.setFileAttached(friend.getFileAttached());
            chatDTO.setFriendId(friend.getId());
            if (friend.getFileAttached() == 1) {
                chatDTO.setStoredFileName(friend.getUserImageFileEntityList().get(0).getStoredFileName());
            }
        }

        List<ChatEntity> chatEntityList = chatRepository.findByChatRoomEntityOrderByIdAsc(chatRoomEntity);
        List<ChatDTO> chatDTOList = new ArrayList<>();
        for (ChatEntity chatEntity : chatEntityList) {
            chatDTOList.add(ChatDTO.toChatDTO(chatEntity));
        }
        return chatDTOList;
    }


    /* 사용자의 모든 채팅방 조회 (최근 채팅 목록) */
    @Transactional
    public List<ChatDTO> findRecentChats(Long id, String isUnread) {

        List<ChatDTO> chatDTOList = new ArrayList<>();
        List<ChatEntity> chatEntityList;
        if (isUnread == null) {
            chatEntityList = chatRepository.findRecentChats(id);
        } else {
            chatEntityList = chatRepository.unreadChatList(id);
        }

        for (ChatEntity chatEntity : chatEntityList) {
            ChatDTO chatDTO = ChatDTO.toChatDTO(chatEntity);
            /* 상대방 entity */
            UserEntity friend;

            /* 채팅방 조회 (회원 pk 조회 목적) */
            ChatRoomEntity chatRoomEntity =
                    chatRoomRepository.findById(chatEntity.getChatRoomEntity().getId()).get();
            /* 발신자 */
            UserEntity sender = chatRoomEntity.getSender();
            /* 수신자 */
            UserEntity receiver = chatRoomEntity.getReceiver();

            /* 날짜 변환하기 */
            String formatDateTime = StringToDate.formatDateTime(String.valueOf(chatDTO.getRegDate()));
            chatDTO.setFormattedDate(formatDateTime);

            /* 채팅 안 읽은 개수 */
            Long roomId = chatRoomEntity.getId();
            Long userId = userRepository.findById(id).get().getId();
            int isNotReadCount = chatRepository.countChatIsNotRead(roomId, userId);
            chatDTO.setIsNotReadCount(isNotReadCount);

            if (!id.equals(sender.getId())) {
                /* 발신자 pk와 내 pk가 일치하지 않는다면 */
                friend = sender;
                chatDTO.setNickname(friend.getNickname());
                chatDTO.setFileAttached(friend.getFileAttached());
                if (friend.getFileAttached() == 1) {
                    chatDTO.setStoredFileName(friend.getUserImageFileEntityList().get(0).getStoredFileName());
                }
            } else if (!id.equals(receiver.getId())) {
                 /* 수신자 pk가 내 pk가 일치하지 않는다면 */
                friend = receiver;
                chatDTO.setNickname(friend.getNickname());
                chatDTO.setFileAttached(friend.getFileAttached());
                if (friend.getFileAttached() == 1) {
                    chatDTO.setStoredFileName(friend.getUserImageFileEntityList().get(0).getStoredFileName());
                }
            }
            chatDTOList.add(chatDTO);
        }
        return chatDTOList;
    }


    /* 채팅방 pk로 회원정보 조회 */
    @Transactional
    public UserDTO getUserInfo(Long roomId, Long sessionId) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId).get();
        UserEntity sender = chatRoomEntity.getSender();
        UserEntity receiver = chatRoomEntity.getReceiver();
        if (!sessionId.equals(sender.getId())) {
            UserDTO userDTO = UserDTO.toUserDTO(sender);
            UserJobEntity userJobEntity = userJobRepository.findByUserEntity(sender).get();
            JobEntity jobEntity = jobRepository.findById(userJobEntity.getJobEntity().getId()).get();
            userDTO.setJobName(jobEntity.getName());
            return userDTO;
        } else if (!sessionId.equals(receiver.getId())) {
            UserDTO userDTO = UserDTO.toUserDTO(receiver);
            UserJobEntity userJobEntity = userJobRepository.findByUserEntity(receiver).get();
            JobEntity jobEntity = jobRepository.findById(userJobEntity.getJobEntity().getId()).get();
            userDTO.setJobName(jobEntity.getName());
            return userDTO;
        } else { // 나중에 처리
            return null;
        }
    }

    /* 채팅방이 이미 있는 상황에서 메시지 전송하기 */
    public Long sendMessage(ChatDTO chatDTO) {
        Optional<ChatRoomEntity> optionalChatRoomEntity = chatRoomRepository.findById(chatDTO.getRoomId());
        if (optionalChatRoomEntity.isPresent()) {
            ChatRoomEntity chatRoomEntity = optionalChatRoomEntity.get();
            UserEntity sender = userRepository.findById(chatDTO.getSenderId()).get();
            ChatEntity chatEntity = ChatEntity.toChatEntity(chatDTO, chatRoomEntity, sender);
            return chatRepository.save(chatEntity).getId();
        } else {
            return null;
        }
    }

    /* 채팅방 접속 - 채팅읽기 */
    @Transactional
    public void readChat(Long roomId, Long sessionId) {
        List<ChatEntity> chatEntityList = chatRepository.checkChatIsNotRead(roomId, sessionId);
        for (ChatEntity chatEntity : chatEntityList) {
            chatRepository.readChat(chatEntity.getId());
        }
    }

    /* 채팅 삭제 */
    public void deleteById(Long id) {
        chatRepository.deleteById(id);
    }

    /* 안 읽은 채팅의 총 개수 */
    public int notReadCount(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        return chatRepository.notReadCount(userEntity);
    }


}


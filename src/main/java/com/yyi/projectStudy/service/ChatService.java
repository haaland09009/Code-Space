package com.yyi.projectStudy.service;


import com.yyi.projectStudy.dto.ChatDTO;
import com.yyi.projectStudy.dto.ChatRoomDTO;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.entity.ChatEntity;
import com.yyi.projectStudy.entity.ChatRoomEntity;
import com.yyi.projectStudy.entity.UserEntity;
import com.yyi.projectStudy.repository.ChatRepository;
import com.yyi.projectStudy.repository.ChatRoomRepository;
import com.yyi.projectStudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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


   // 채팅 전송
    @Transactional
    public Long save(ChatDTO chatDTO, UserDTO senderDTO, UserDTO receiverDTO) {
        // 발신자
        UserEntity sender = UserEntity.toUserEntity(senderDTO);
        // 수신자
        UserEntity receiver = UserEntity.toUserEntity(receiverDTO);

        Optional<ChatRoomEntity> optionalChatRoomEntity = chatRoomRepository.existsChatRoom(receiver.getId(), sender.getId());
        if (optionalChatRoomEntity.isPresent()) {
            // 기존 채팅방이 이미 존재한다면
            ChatRoomEntity chatRoomEntity = optionalChatRoomEntity.get();
            // 채팅 dto -> entity 변환
            ChatEntity chatEntity = ChatEntity.toChatEntity(chatDTO, chatRoomEntity, sender);
            // 채팅 저장
            chatRepository.save(chatEntity);
            return chatRoomEntity.getId();
        } else {
            // 기존 채팅방이 없다면
            ChatRoomEntity roomEntity = ChatRoomEntity.toChatRoomEntity(sender, receiver);
            // 채팅방을 생성하고 채팅방 pk를 반환한다.
            Long roomId = chatRoomRepository.save(roomEntity).getId();

            // 만들어진 채팅방 entity 조회
            ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId).get();
            // 채팅 dto -> entity 변환
            ChatEntity chatEntity = ChatEntity.toChatEntity(chatDTO, chatRoomEntity, sender);
            // 채팅 저장
            chatRepository.save(chatEntity);
            return chatRoomEntity.getId();
        }
    }

    // 채팅방에 있는 채팅 기록 모두 조회
    @Transactional
    public List<ChatDTO> findAll(Long roomId, Long sessionId) {
        // 해당 id로 채팅방번호 (pk) 조회 (나중에 예외처리)
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId).get();

        // 상대방 entity (채팅방에 보여질 상대방 정보)
        UserEntity friend;

        // 발신자
        UserEntity sender = chatRoomEntity.getSender();
        // 수신자
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


    // 사용자의 모든 채팅방 조회 (최근 채팅 목록)
    @Transactional
    public List<ChatDTO> findRecentChats(Long id) {

        List<ChatDTO> chatDTOList = new ArrayList<>();
        List<ChatEntity> chatEntityList = chatRepository.findRecentChats(id);
        for (ChatEntity chatEntity : chatEntityList) {
            ChatDTO chatDTO = ChatDTO.toChatDTO(chatEntity);
            // 상대방 entity
            UserEntity friend;

            // 채팅방 조회 (회원 pk 조회 목적)
            ChatRoomEntity chatRoomEntity =
                    chatRoomRepository.findById(chatEntity.getChatRoomEntity().getId()).get();
            // 발신자
            UserEntity sender = chatRoomEntity.getSender();
            // 수신자
            UserEntity receiver = chatRoomEntity.getReceiver();

            if (!id.equals(sender.getId())) {
                // 발신자 pk와 내 pk가 일치하지 않는다면
                friend = sender;
                chatDTO.setNickname(friend.getNickname());
                chatDTO.setFileAttached(friend.getFileAttached());
                if (friend.getFileAttached() == 1) {
                    chatDTO.setStoredFileName(friend.getUserImageFileEntityList().get(0).getStoredFileName());
                }
            } else if (!id.equals(receiver.getId())) {
                // 수신자 pk가 내 pk가 일치하지 않는다면
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




//    @Transactional
//    public List<ChatRoomDTO> findAllChatRoom(Long id) {
//        UserEntity sender = userRepository.findById(id).get();
//        UserEntity receiver = userRepository.findById(id).get();
//
//        List<ChatRoomDTO> chatRoomDTOList = new ArrayList<>();
//
//        // 모든 채팅방 찾기
//        List<ChatRoomEntity> chatRoomEntityList = chatRoomRepository.findBySenderOrReceiver(sender, receiver);
//        for (ChatRoomEntity chatRoomEntity : chatRoomEntityList) {
//            ChatRoomDTO chatRoomDTO = ChatRoomDTO.toChatRoomDTO(chatRoomEntity);
//
//            // 채팅방에 표시할 상대방의 정보를 조회 (프로필 사진, 이름)
//            if (!id.equals(chatRoomDTO.getSenderId())) {
//                chatRoomDTO.setWriter(chatRoomEntity.getSender().getNickname());
//                chatRoomDTO.setFileAttached(chatRoomEntity.getSender().getFileAttached());
//                if (chatRoomEntity.getSender().getFileAttached() == 1) {
//                    chatRoomDTO.setStoredFileName(chatRoomEntity.getSender().getUserImageFileEntityList().get(0).getStoredFileName());
//                }
//            } else if (!id.equals(chatRoomDTO.getReceiverId())) {
//                chatRoomDTO.setWriter(chatRoomEntity.getReceiver().getNickname());
//                chatRoomDTO.setFileAttached(chatRoomEntity.getReceiver().getFileAttached());
//                if (chatRoomEntity.getReceiver().getFileAttached() == 1) {
//                    chatRoomDTO.setStoredFileName(chatRoomEntity.getReceiver().getUserImageFileEntityList().get(0).getStoredFileName());
//                }
//            }
//            ///////////////////////
//            chatRoomDTOList.add(chatRoomDTO);
//        }
//        return chatRoomDTOList;
//    }



    }


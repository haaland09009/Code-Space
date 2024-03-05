package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.ChatDTO;
import com.yyi.projectStudy.dto.ChatRoomDTO;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.service.ChatService;
import com.yyi.projectStudy.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    // 채팅 전송
    @PostMapping("/save")
    public @ResponseBody String save(@ModelAttribute ChatRoomDTO chatRoomDTO,
                               @ModelAttribute ChatDTO chatDTO,
                               HttpSession session)  {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        // 발신자
        Long userId = sessionUser.getId();
        UserDTO senderDTO = userService.findById(userId);

        // 수신자
        Long receiverId = chatRoomDTO.getReceiverId();
        UserDTO receiverDTO = userService.findById(receiverId);

        // 내용 enter 처리
        String content = chatDTO.getContent();
        content = content.replaceAll("\r\n", "<br>");
        chatDTO.setContent(content);

        // 채팅 전송 (반환 : 채팅방 pk)
        Long savedId = chatService.save(chatDTO, senderDTO, receiverDTO);
        if (savedId != null) {
            return "ok";
        } else {
            return "no";
        }
    }


    // 채팅방 모두 모두 조회
    @GetMapping("/recentChatList/{id}")
    public ResponseEntity recentChatList(@PathVariable("id") Long id) {
        UserDTO userDTO = userService.findById(id);
        // 사용자의 모든 채팅방 조회 (최근 채팅 목록 - 채팅방 room_id 얻을 수 있음)
        List<ChatDTO> chatDTOList = chatService.findRecentChats(id);
        return new ResponseEntity<>(chatDTOList, HttpStatus.OK);
    }

    // 채팅방에 있는 모든 채팅 기록 조회
    @GetMapping("/chatList/{roomId}")
    public ResponseEntity chatList(@PathVariable("roomId") Long roomId,
                                   HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        Long sessionId = sessionUser.getId();
        List<ChatDTO> chatDTOList = chatService.findAll(roomId, sessionId);
        return new ResponseEntity<>(chatDTOList, HttpStatus.OK);
    }


//    @GetMapping("/roomList/{id}")
//    public ResponseEntity chatList(@PathVariable("id") Long id) {
//        UserDTO userDTO = userService.findById(id);
//        // 사용자의 모든 채팅방 조회
//        List<ChatRoomDTO> chatRoomDTOList = chatService.findAllChatRoom(id);
//        for (ChatRoomDTO chatRoomDTO : chatRoomDTOList) {
//            Long roomId = chatRoomDTO.getId();
//
//            // 채팅방에 있는 채팅 리스트를 담을 dto 리스트 생성
//            chatRoomDTO.setChatList(chatService.findByRoomId(roomId));
//        }
//        return new ResponseEntity<>(chatRoomDTOList, HttpStatus.OK);
//    }



}

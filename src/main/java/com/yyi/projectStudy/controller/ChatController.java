package com.yyi.projectStudy.controller;

import com.yyi.projectStudy.dto.ChatDTO;
import com.yyi.projectStudy.dto.ChatRoomDTO;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.entity.ChatRoomEntity;
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

    /* 메시지 전송하기 */
    @PostMapping("/save")
    public @ResponseBody String save(@ModelAttribute ChatRoomDTO chatRoomDTO,
                                     @ModelAttribute ChatDTO chatDTO,
                                     HttpSession session)  {
        /* js에서 세션 존재 여부를 이미 작성했으므로 여기서 세션 존재 여부를 작성하지 않음. */
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");

        /* 발신자 정보 입력 */
        Long userId = sessionUser.getId();
        UserDTO senderDTO = userService.findById(userId);

        /* 수신자 정보 입력 */
        Long receiverId = chatRoomDTO.getReceiverId();
        UserDTO receiverDTO = userService.findById(receiverId);

        /* 내용 enter 처리 */
        String content = chatDTO.getContent();
        content = content.replaceAll("\r\n", "<br>");
        chatDTO.setContent(content);

        /* 메시지 전송 (저장 후 반환 : 채팅방 pk) */
        Long savedId = chatService.save(chatDTO, senderDTO, receiverDTO);
        if (savedId != null) {
            return "ok";
        } else {
            return "no";
        }
    }

    /* 채팅방이 이미 있는 상황에서 메시지 전송하기 */
    @PostMapping("/sendMessage")
    public @ResponseBody String sendMessage(@ModelAttribute ChatDTO chatDTO,
                                            HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        chatDTO.setSenderId(sessionUser.getId());


        String content = chatDTO.getContent();
        content = content.replaceAll("\r\n", "<br>");
        chatDTO.setContent(content);

        Long savedId = chatService.sendMessage(chatDTO);
        /* 나머지 채팅 읽음 처리 */
        chatService.readChat(chatDTO.getRoomId(), sessionUser.getId());
        if (savedId != null) {
            return "ok";
        } else {
            return "no";
        }
    }

    /* 회원의 채팅방 모두 조회 */
    @GetMapping("/recentChatList/{id}")
    public ResponseEntity recentChatList(@PathVariable("id") Long id) {
        UserDTO userDTO = userService.findById(id);
        if (userDTO != null) {
            /* 사용자의 모든 채팅방 조회 (최근 채팅 목록 - 채팅방 room_id 얻을 수 있음) */
            List<ChatDTO> chatDTOList = chatService.findRecentChats(id, null);
            return new ResponseEntity<>(chatDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("회원 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    /* 안 읽은 채팅 기록이 있는 채팅방만 모두 조회 */
    @GetMapping("/unreadChatList/{id}")
    public ResponseEntity unreadChatList(@PathVariable("id") Long id) {
        UserDTO userDTO = userService.findById(id);
        if (userDTO != null) {
            /* 사용자의 모든 채팅방 조회 (최근 채팅 목록 - 채팅방 room_id 얻을 수 있음) */
            List<ChatDTO> chatDTOList = chatService.findRecentChats(id, "unread");
            return new ResponseEntity<>(chatDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("회원 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }




    /* 채팅방에 있는 모든 채팅 기록 조회 */
    @GetMapping("/list/{roomId}")
    public ResponseEntity chatList(@PathVariable("roomId") Long roomId,
                                   HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        Long sessionId = sessionUser.getId();
        List<ChatDTO> chatDTOList = chatService.findAll(roomId, sessionId);
        return new ResponseEntity<>(chatDTOList, HttpStatus.OK);
    }



    /* 채팅방에 있는 상대방 정보 조회 */
    // ********* 나중에 에러처리 고쳐야함 null 대비
    @GetMapping("/getUserInfo/{roomId}")
    public ResponseEntity getUserInfo(@PathVariable("roomId") Long roomId,
                                             HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        Long sessionId = sessionUser.getId();
        UserDTO userInfo = chatService.getUserInfo(roomId, sessionId);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }


    /* 채팅방 접속 - 채팅 읽기 */
    @GetMapping("/readChat/{roomId}")
    public @ResponseBody void readChat(@PathVariable("roomId") Long roomId,
                                       HttpSession session) {
        UserDTO sessionUser = (UserDTO) session.getAttribute("userDTO");
        Long sessionId = sessionUser.getId();
        chatService.readChat(roomId, sessionId);
    }

    /* 메시지 삭제하기 */
    @GetMapping("/delete/{id}")
    public @ResponseBody void delete(@PathVariable("id") Long id) {
        chatService.deleteById(id);
    }


    /* 안 읽은 총 채팅 개수 */
    @GetMapping("/notReadCount/{id}")
    public @ResponseBody int notReadCount(@PathVariable("id") Long id) {
        return chatService.notReadCount(id);
    }


}

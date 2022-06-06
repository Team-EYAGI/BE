package com.example.eyagi.controller;

import com.example.eyagi.dto.request.ChatRoomCreateRequestDto;
import com.example.eyagi.dto.response.ChatMessageAllResponseDto;
import com.example.eyagi.dto.response.ChatRoomCreateResponseDto;
import com.example.eyagi.dto.response.ChatRoomListResponseDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.ChatMessageService;
import com.example.eyagi.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    // 채팅 방 입장 내맘대로 수정한부분 : 이유는 POST아이디를 쓰기때문에
    @PostMapping("/chat/rooms")
    public ChatRoomCreateResponseDto createChatRoom(@RequestBody ChatRoomCreateRequestDto requestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.createChatRoom(requestDto, userDetails.getUser());
    }

    // 해당 채팅방의 메세지 조회
    @GetMapping("/chat/{roomId}/messages")
    public List<ChatMessageAllResponseDto> getRoomMessage(@PathVariable String roomId){
        return chatMessageService.getChatMessageByRoomId(roomId);
    }

    // 전체 채팅방 목록 조회 (관리자용)
    @GetMapping("/rooms")
    public Map<String, Object> getAllChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(!userDetails.getUserRole().equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("이 계정은 관리자 권한이 아닙니다." + userDetails.getUsername());
        }
        return chatRoomService.getAllChatRooms(userDetails);
    }

    // 새로운 메세지 확인하기
    @PostMapping("/finds/newMessage")
    public List<ChatRoomListResponseDto> messageNew(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.findNewMessage(userDetails);
    }

    //해당 채팅방 폭파!
    @DeleteMapping("/chat/{roomId}")
    public void quitChat(@PathVariable Long roomId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(!userDetails.getUserRole().equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("이 계정은 관리자 권한이 아닙니다." + userDetails.getUsername());
        }
        chatRoomService.quitChat(roomId);
    }

}

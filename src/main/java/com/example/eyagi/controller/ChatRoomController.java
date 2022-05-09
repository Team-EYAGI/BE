package com.example.eyagi.controller;

import com.example.eyagi.dto.request.ChatRoomCreateRequestDto;
import com.example.eyagi.dto.response.ChatRoomCreateResponseDto;
import com.example.eyagi.dto.response.ChatRoomListResponseDto;
import com.example.eyagi.model.ChatMessage;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.ChatMessageService;
import com.example.eyagi.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
//    private final UserRoomService userRoomService;

    // 채팅 방 입장 내맘대로 수정한부분 : 이유는 POST아이디를 쓰기때문에
    @PostMapping("/chat/rooms")
    public ChatRoomCreateResponseDto createChatRoom(@RequestBody ChatRoomCreateRequestDto requestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.createChatRoom(requestDto, userDetails.getUser());
    }

    // 사용자별 채팅방 목록 조회
    @GetMapping("/chat/rooms/mine")
    public List<ChatRoomListResponseDto> getOnesChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getOnesChatRoom(userDetails.getUser());
    }

    // 해당 채팅방의 메세지 조회
    @GetMapping("/chat/{roomId}/messages")
    public Page<ChatMessage> getRoomMessage(@PathVariable String roomId, @PageableDefault Pageable pageable){
        return chatMessageService.getChatMessageByRoomId(roomId, pageable);
    }

}

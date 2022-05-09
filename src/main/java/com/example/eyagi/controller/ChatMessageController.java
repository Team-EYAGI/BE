package com.example.eyagi.controller;

import com.example.eyagi.dto.request.ChatMessageRequestDto;
import com.example.eyagi.model.ChatMessage;
import com.example.eyagi.service.ChatMessageService;
import com.example.eyagi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final UserService userService;


    @MessageMapping("/message")
    public void message(@Header("token") String token, @RequestBody ChatMessageRequestDto messageRequestDto) {
        System.out.println("token = " + token);
        ChatMessage chatMessage = new ChatMessage(messageRequestDto);
        chatMessageService.sendChatMessage(chatMessage);
    }
}

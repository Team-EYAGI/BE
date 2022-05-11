package com.example.eyagi.pubsub;

import com.example.eyagi.model.ChatMessage;
import com.example.eyagi.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 sendMessage가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            messagingTemplate.convertAndSend("/sub/api/chat/rooms/" + chatMessage.getRoomId(), chatMessage);
            ChatMessage message = new ChatMessage();
            message.setType(chatMessage.getType());
            message.setRoomId(chatMessage.getRoomId());
            message.setSender(chatMessage.getSender());
            message.setMessage(chatMessage.getMessage());
            chatMessageRepository.save(message);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}

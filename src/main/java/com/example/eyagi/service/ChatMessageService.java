package com.example.eyagi.service;

import com.example.eyagi.model.ChatMessage;
import com.example.eyagi.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    // 메세지의 헤더에서 추출한 정보로 roomId 를 확인하고 리턴함
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            return "";
        }
    }

    // 메세지의 type 을 확인하고 그에따라 작업을 분기시킴
    public void sendChatMessage(ChatMessage chatMessageRequestDto){
        if (ChatMessage.MessageType.ENTER.equals(chatMessageRequestDto.getType())){
            chatMessageRequestDto.setMessage(chatMessageRequestDto.getSenderId() + "가 입장했습니다.");
            chatMessageRequestDto.setSenderId(chatMessageRequestDto.getSenderId());
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessageRequestDto.getType())){
            chatMessageRequestDto.setMessage(chatMessageRequestDto.getSenderId() +"님이 퇴장했습니다.");
            chatMessageRequestDto.setSenderId(chatMessageRequestDto.getSenderId());
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(),chatMessageRequestDto);
    }

    public void save(ChatMessage chatMessage) {
        ChatMessage message = new ChatMessage();
        message.setType(chatMessage.getType());
        message.setRoomId(chatMessage.getRoomId());
        message.setSenderId(chatMessage.getSenderId());
        message.setMessage(chatMessage.getMessage());
        chatMessageRepository.save(message);
    }

    public Page<ChatMessage> getChatMessageByRoomId(String roomId, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
        pageable = PageRequest.of(page, 150);
        return chatMessageRepository.findByRoomId(roomId, pageable);
    }
}

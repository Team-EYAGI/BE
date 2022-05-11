package com.example.eyagi.service;

import com.example.eyagi.dto.response.ChatMessageAllResponseDto;
import com.example.eyagi.model.ChatMessage;
import com.example.eyagi.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


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
            throw new IllegalArgumentException("lastIndex 오류입니다.");
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

    // 채팅방의 마지막 100개 메세지를 페이징하여 리턴함
//    public Page<ChatMessage> getChatMessageByRoomId(String roomId, Pageable pageable) {
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt" );
//        pageable = PageRequest.of(page, 100, sort);
//        return chatMessageRepository.findByRoomId(roomId, pageable);
//    }
    public List<ChatMessageAllResponseDto> getChatMessageByRoomId(String roomId) {
        List<ChatMessage> chatMessageList = chatMessageRepository.findByRoomId(roomId);
        List<ChatMessageAllResponseDto> chatMessageAllResponseList
                = new ArrayList<>();
        for(ChatMessage cM : chatMessageList) {
            ChatMessageAllResponseDto chatMessageAllResponseDto = ChatMessageAllResponseDto.builder()
                    .createdAt(formmater(cM.getCreatedAt()))
                    .id(cM.getId())
                    .type(cM.getType())
//                    .senderNickname(cM.getSender().getUsername())
                    .message(cM.getMessage())
                    .build();
            chatMessageAllResponseList.add(chatMessageAllResponseDto);
        }
        return chatMessageAllResponseList;
    }

    public String formmater(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("MM/dd hh:mm").format(localDateTime);
    }
}

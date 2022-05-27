package com.example.eyagi.service;

import com.example.eyagi.model.AllChatInfo;
import com.example.eyagi.model.ChatMessage;
import com.example.eyagi.model.ChatRoom;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.AllChatInfoRepository;
import com.example.eyagi.repository.ChatMessageQRepository;
import com.example.eyagi.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AllChatInfoService {
    private final AllChatInfoRepository allChatInfoRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageQRepository chatMessageQRepository;

    // 채팅방 접속 종료시 해당 채팅방의 마지막 TALK 타입 메시지의 id를 저장함
    @Transactional
    public void updateReadMessage(User user, String roomId){
        // 마지막으로 읽은 메세지
        Optional<ChatMessage> chatMessage = chatMessageQRepository.findbyRoomIdAndTalk(roomId);
        Long lastMessageId = chatMessage.get().getId();
        AllChatInfo allChatInfo = allChatInfoRepository.findByChatRoom_RoomIdAndUserId(Long.parseLong(roomId),user.getId());
        allChatInfo.updateLastMessageId(lastMessageId);
    }

    @Transactional
    public void save(User user, ChatRoom chatRoom){
        AllChatInfo allChatInfo = new AllChatInfo(user, chatRoom);
        allChatInfoRepository.save(allChatInfo);
    }
}

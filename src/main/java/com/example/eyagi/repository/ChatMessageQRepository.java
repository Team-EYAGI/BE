package com.example.eyagi.repository;

import com.example.eyagi.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;


@Repository
@RequiredArgsConstructor
public class ChatMessageQRepository {
    private final EntityManager em;

//    public ChatMessage findbyRoomIdAndTalk(String RoomId, ChatMessage.MessageType TALK) {
//        return em.createQuery("select cm from ChatMessage as cm where cm.roomId =:RoomId")
//                .setParameter("RoomId", RoomId)
//                .setParameter('MessageType', ChatMessage.MessageType);
//    }
//    public ChatMessage findbyRoomIdAndTalk(String RoomId) {
//        return queryFactory.selectFrom(chatMessage)
//                .where(chatMessage.roomId.eq(RoomId))
//                .where(chatMessage.type.eq(ChatMessage.MessageType.TALK))
//                .orderBy(chatMessage.id.desc())
//                .fetchFirst();
//    }
}

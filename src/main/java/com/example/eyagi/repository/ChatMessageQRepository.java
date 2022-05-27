package com.example.eyagi.repository;

import com.example.eyagi.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class ChatMessageQRepository {
    private final EntityManager em;
//    옵셔널로 설정
    public Optional<ChatMessage> findbyRoomIdAndTalk(String RoomId) {
        Optional<ChatMessage> chatMessage = null;
        try{
            chatMessage = Optional.ofNullable(em.createQuery("select cm from ChatMessage as cm where cm.roomId =:RoomId and cm.type =:messageType order by cm.createdAt desc", ChatMessage.class)
                    .setParameter("RoomId", RoomId)
                    .setParameter("messageType", ChatMessage.MessageType.TALK)
                    .getResultList()
                    .get(0));
        } catch (NoResultException e) {
            System.out.println(e);
            chatMessage = Optional.ofNullable(em.createQuery("select cm from ChatMessage as cm where cm.roomId =:RoomId and cm.type =:messageType order by cm.createdAt desc", ChatMessage.class)
                    .setParameter("RoomId", RoomId)
                    .setParameter("messageType", ChatMessage.MessageType.ENTER)
                    .getResultList()
                    .get(0));
        } finally {
            return chatMessage;
        }
    }

}

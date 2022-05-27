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

    public ChatMessage findbyRoomIdAndTalk(String RoomId) {
        return em.createQuery("select cm from ChatMessage as cm where cm.roomId =:RoomId and cm.type =:messageType order by cm.createdAt desc", ChatMessage.class)
                .setParameter("RoomId", RoomId)
                .setParameter("messageType", ChatMessage.MessageType.TALK)
                .getResultList()
                .get(0);
    }

}

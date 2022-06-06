package com.example.eyagi.repository;

import com.example.eyagi.model.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByRoomId(Long roomId);
    void deleteByRoomId(Long roomId);

    @Query(value = "select cr.roomId as roomId, cr.uuid as uuid, cr.createdAt as createdAt, cr.chatRoomName as chatRoomName, cr.ownUser.id as ownUserId, cr.ownUser.email as ownUserEmail," +
            " cr.ownUser.role as userRole, cr.ownUser.username as ownUsername from ChatRoom cr")
    Page<ChatRoomCustomRepository> findAllByOrderByRoomId(Pageable pageable);

}

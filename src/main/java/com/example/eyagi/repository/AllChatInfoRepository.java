package com.example.eyagi.repository;

import com.example.eyagi.model.AllChatInfo;
import com.example.eyagi.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllChatInfoRepository extends JpaRepository<AllChatInfo,Long> {
    List<AllChatInfo> findAllByUserId(Long userId);
    AllChatInfo findByChatRoom_RoomIdAndUserId(Long chatRoomId, Long userId);

    AllChatInfo findByChatRoom(ChatRoom chatRoom);

    void deleteByChatRoom_RoomId(Long roomId);
}

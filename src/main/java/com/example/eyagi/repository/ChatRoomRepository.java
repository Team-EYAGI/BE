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

//    @Query("SELECT c.roomId AS room_id, c.uuid, c.createdAt, c.chatRoomName, c.ownUser.id FROM ChatRoom c")
    @Query(value = "select cr.roomId as roomId, cr.uuid as uuid, cr.createdAt as createdAt, cr.chatRoomName as chatRoomName, cr.ownUser.id as ownUserId, cr.ownUser.email as ownUserEmail," +
            " cr.ownUser.role as userRole, cr.ownUser.username as ownUsername from ChatRoom cr")
    Page<ChatRoomCustomRepository> findAllByOrderByRoomId(Pageable pageable);


    // 근데 Named쿼리는 애플리케이션 로딩 시점에 쿼리를 파싱한다. 그래서 배포하기 전에 문제를 잡을 수 있다. 아니, 개발하면서 띄워 보기만해도 잡을 수 있다.
    // Spring-Data-JPA를 사용할 때 @Query 어노테이션이 이 Named쿼리로 동작하게 된다. 그래서 스프링 애플리케이션 올라갈때 바로 에러를 잡을 수 있다.
}

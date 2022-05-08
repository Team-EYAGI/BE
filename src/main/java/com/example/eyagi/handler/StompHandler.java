package com.example.eyagi.handler;

import com.example.eyagi.model.ChatMessage;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.UserRepository;
import com.example.eyagi.security.jwt.JwtDecoder;
import com.example.eyagi.service.ChatMessageService;
import com.example.eyagi.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatService;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()){
            String jwtToken = accessor.getFirstNativeHeader("token");
            log.info("CONNECT {}", jwtToken);
            jwtDecoder.decodeUsername(jwtToken);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()){// 채팅룸 구독요청
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            String sessionId = (String) message.getHeaders().get("simpSessionId");

            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
            //토큰 가져옴
            String jwtToken = accessor.getFirstNativeHeader("token");
            User user;
            if (jwtToken != null) {
                //토큰으로 user 가져옴
                user = userRepository.findByEmail(jwtDecoder.decodeUsername(jwtToken))
                        .orElseThrow(()->new IllegalArgumentException("user 가 존재하지 않습니다."));
            }else {
                throw new IllegalArgumentException("유효하지 않은 token 입니다.");
            }
            Long userId = user.getId();
            chatRoomService.setUserEnterInfo(sessionId, roomId, userId);
            chatService.sendChatMessage(ChatMessage.builder()
                    .type(ChatMessage.MessageType.ENTER)
                    .roomId(roomId)
                    .sender(user)
                    .build());
            log.info("SUBSCRIBED {}, {}", user.getEmail(), roomId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomService.getUserEnterRoomId(sessionId);
            // 저장했던 sessionId 로 유저 객체를 받아옴
            User user = chatRoomService.chkSessionUser(sessionId);
            chatService.sendChatMessage(ChatMessage.builder()
                    .type(ChatMessage.MessageType.QUIT)
                    .roomId(roomId)
                    .sender(user)
                    .build());
            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
            chatRoomService.removeUserEnterInfo(sessionId);
            log.info("DISCONNECTED {}, {}", user.getEmail(), roomId);
        }
        return message;
    }
}


package com.example.eyagi.handler;

import com.example.eyagi.model.ChatMessage;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.UserRepository;
import com.example.eyagi.security.jwt.JwtDecoder;
import com.example.eyagi.service.AllChatInfoService;
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

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatService;
    private final UserRepository userRepository;
    private final AllChatInfoService allChatInfoService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()){
            String jwtToken = accessor.getFirstNativeHeader("token");
            log.info("CONNECT {}", jwtToken);
            String[] newJwtToken = jwtToken.split("BEARER ");
            jwtDecoder.decodeUsername(newJwtToken[1]);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()){// 채팅룸 구독요청
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            String sessionId = (String) message.getHeaders().get("simpSessionId");

            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
            //토큰 가져옴
            String jwtToken = accessor.getFirstNativeHeader("token");
            String[] newJwtToken = jwtToken.split("BEARER ");
            User user;
            if (newJwtToken[1] != null) {
                //토큰으로 user 가져옴
                user = userRepository.findByEmail(jwtDecoder.decodeUsername(newJwtToken[1]))
                        .orElseThrow(()->new IllegalArgumentException("user 가 존재하지 않습니다."));
            }else {
                throw new IllegalArgumentException("유효하지 않은 token 입니다.");
            }
            Long userId = user.getId();
            chatRoomService.setUserEnterInfo(sessionId, roomId, userId);
            chatService.sendChatMessage(ChatMessage.builder()
                    .type(ChatMessage.MessageType.ENTER)
                    .roomId(roomId)
                    .senderId(userId)
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
                    .senderId(user.getId())
                    .build());
            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
            chatRoomService.removeUserEnterInfo(sessionId);
            log.info("DISCONNECTED {}, {}", user.getEmail(), roomId);

            // 유저가 퇴장할 당시의 마지막 TALK 타입 메세지 id 를 저장함
            allChatInfoService.updateReadMessage(user,roomId);
        }
        return message;
    }
}


package com.example.eyagi.service;

import com.example.eyagi.dto.request.ChatRoomCreateRequestDto;
import com.example.eyagi.dto.response.ChatRoomCreateResponseDto;
import com.example.eyagi.dto.response.ChatRoomListResponseDto;
import com.example.eyagi.model.ChatRoom;
import com.example.eyagi.model.User;
import com.example.eyagi.model.AllChatInfo;
import com.example.eyagi.repository.ChatRoomRepository;
import com.example.eyagi.repository.UserRepository;
import com.example.eyagi.repository.AllChatInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    // HashPerations 레디스에서 쓰는 자료형
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsUserInfo;
    private final ChatRoomRepository chatRoomRepository;
    private final AllChatInfoRepository allChatInfoRepository;
    private final UserRepository userRepository;

    public static final String ENTER_INFO = "ENTER_INFO";
    public static final String USER_INFO = "USER_INFO";

    //채팅방생성
    public ChatRoomCreateResponseDto createChatRoom(ChatRoomCreateRequestDto requestDto, User user) {
        ChatRoom chatRoom = new ChatRoom(requestDto.getChatRoomName(), requestDto.getUuid(), user);
        chatRoomRepository.save(chatRoom);
        ChatRoomCreateResponseDto chatRoomCreateResponseDto = new ChatRoomCreateResponseDto(chatRoom);
        return chatRoomCreateResponseDto;
    }

    // 사용자별 채팅방 목록 조회
    public List<ChatRoomListResponseDto> getOnesChatRoom(User user) {
        // 미완. 후에 데이터 관계 맵핑 or 불러오는 기획 설정 후 변경
        List<AllChatInfo> allChatInfoList = allChatInfoRepository.findAllByUserId(user.getId());
        List<ChatRoomListResponseDto> responseDtoList = new ArrayList<>();
        for ( AllChatInfo allChatInfo : allChatInfoList) {
            ChatRoom chatRoom = allChatInfo.getChatRoom();
            // myLastMessageId 와 newLastMessageId 를 비교하여 현재 채팅방에 새 메세지가 있는지 여부를 함께 내려줌
            //일단 무조건 True
            ChatRoomListResponseDto responseDto = new ChatRoomListResponseDto(chatRoom, true);
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    // redis 에 입장정보로 sessionId 와 roomId를 저장하고 해단 sessionId 와 토큰에서 받아온 userId를 저장함
    public void setUserEnterInfo(String sessionId, String roomId, Long userId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
        hashOpsUserInfo.put(USER_INFO, sessionId, Long.toString(userId));// redistemplate에 (입장type, ,) 누가 어떤방에 들어갔는지 정보를 리턴
    }
    // redis 에 저장했던 sessionId 로 roomId를 리턴함
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저가 나갈때 redis 에 저장했던 해당 세션 / 유저의 정보를 삭제함
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
        hashOpsUserInfo.delete(USER_INFO, sessionId);
    }


    //채팅방전부찾기
    public List<ChatRoom> getAll() {
        return chatRoomRepository.findAll();
    }

    // redis 에 저장했던 sessionId 로 userId 를 얻어오고 해당 userId 로 User 객체를 찾아 리턴함
    public User chkSessionUser(String sessionId) {
        Long userId = Long.parseLong(Objects.requireNonNull(hashOpsUserInfo.get(USER_INFO, sessionId)));
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));
    }
}

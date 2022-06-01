package com.example.eyagi.service;

import com.example.eyagi.dto.request.ChatRoomCreateRequestDto;
import com.example.eyagi.dto.response.ChatRoomCreateResponseDto;
import com.example.eyagi.dto.response.ChatRoomListAdminResponseDto;
import com.example.eyagi.dto.response.ChatRoomListResponseDto;
import com.example.eyagi.model.ChatMessage;
import com.example.eyagi.model.ChatRoom;
import com.example.eyagi.model.User;
import com.example.eyagi.model.AllChatInfo;
import com.example.eyagi.repository.*;
import com.example.eyagi.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private final ChatMessageRepository chatMessageRepository;
    private final AllChatInfoService allChatInfoService;
    private final ChatMessageQRepository chatMessageQRepository;

    public static final String ENTER_INFO = "ENTER_INFO";
    public static final String USER_INFO = "USER_INFO";

    //채팅방생성
    public ChatRoomCreateResponseDto createChatRoom(ChatRoomCreateRequestDto requestDto, User user) {
        ChatRoom chatRoom = new ChatRoom(requestDto.getChatRoomName(), requestDto.getUuid(), user);
        chatRoomRepository.save(chatRoom);

        allChatInfoService.save(user, chatRoom);

        ChatRoomCreateResponseDto chatRoomCreateResponseDto = ChatRoomCreateResponseDto.builder()
                .chatRoomName(chatRoom.getChatRoomName())
                .roomId(chatRoom.getRoomId())
                .userId(chatRoom.getOwnUser().getId())
                .userName(chatRoom.getOwnUser().getUsername())
                .build();
        return chatRoomCreateResponseDto;
    }

//    // 사용자별 채팅방 목록 조회
//    public List<ChatRoomListResponseDto> getOnesChatRoom(User user) {
//        // 미완. 후에 데이터 관계 맵핑 or 불러오는 기획 설정 후 변경
//        List<AllChatInfo> allChatInfoList = allChatInfoRepository.findAllByUserId(user.getId());
//        List<ChatRoomListResponseDto> responseDtoList = new ArrayList<>();
//        for ( AllChatInfo allChatInfo : allChatInfoList) {
//            ChatRoom chatRoom = allChatInfo.getChatRoom();
//            //일단 무조건 True
//            ChatRoomListResponseDto responseDto = ChatRoomListResponseDto.builder()
//                    .roomId(chatRoom.getRoomId())
//                    .ownUserId(chatRoom.getOwnUser().getId())
//                    .newMessage(true)
//                    .build();
//            responseDtoList.add(responseDto);
//        }
//        return responseDtoList;
//    }

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

    // 사용자 새로운 메시지 확인
    public List<ChatRoomListResponseDto> findNewMessage(UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("유저 없음."));
        Boolean newMessage;
        List<AllChatInfo> allChatInfoList = allChatInfoRepository.findAllByUserId(user.getId());
        List<ChatRoomListResponseDto> responseDtoList = new ArrayList<>();
        for ( AllChatInfo allChatInfo : allChatInfoList) {
            newMessage = false; // 기본 없다고 설정
            ChatRoom chatRoom = allChatInfo.getChatRoom();
            Optional<ChatMessage> newLastMessage = chatMessageQRepository.findbyRoomIdAndTalk(chatRoom.getRoomId().toString());
            Long lastMessage = allChatInfoRepository.findByChatRoom(chatRoom).getLastMessageId();
            if(newLastMessage.get().getId() > lastMessage) {
                newMessage = true;
            }

            ChatRoomListResponseDto responseDto = ChatRoomListResponseDto.builder()
                    .roomId(chatRoom.getRoomId())
                    .ownUserId(chatRoom.getOwnUser().getId())
                    .newMessage(newMessage)
                    .build();
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }


    //채팅방전부찾기
    public Map<String, Object> getAllChatRooms(UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("유저 없음."));
        Boolean newMessage;
        List<ChatRoom> AllChatRoom = chatRoomRepository.findAll();
        List<ChatRoomListAdminResponseDto> chatRoomList = new ArrayList<>();
        for(ChatRoom cR : AllChatRoom) {
            newMessage = false; // 기본 없다고 설정
//            // null일 경우가 생김
//            Optional<ChatMessage> newLastMessage = chatMessageQRepository.findbyRoomIdAndTalk(cR.getRoomId().toString());
//            Long lastMessage = allChatInfoRepository.findByChatRoom(cR).getLastMessageId();
//            if(newLastMessage == null) {
//                System.out.println("널" + newLastMessage);
//                // 환영인사 자동.
//                ChatMessage chatMessage = new ChatMessage(ChatMessage.MessageType.TALK, cR.getRoomId().toString(), user.getId(), "무엇을 도와드릴까요?");
//                chatMessageRepository.save(chatMessage);
//
//                // All chat info 에 기록남기기
////                allChatInfoService.save(user, cR);
//            } else {
//                if(newLastMessage.get().getId() > lastMessage) {
//                    System.out.println(newLastMessage.get().getId() + "///" +lastMessage);
//                    newMessage = true;
//                }
//            }

            ChatRoomListAdminResponseDto chatRoomListAdminResponseDto = new ChatRoomListAdminResponseDto().builder()
                    .roomId(cR.getRoomId())
                    .createdAt(formmater(cR.getCreatedAt()))
                    .nickname(cR.getOwnUser().getUsername())
                    .userRole(cR.getOwnUser().getRole())
                    .romName(cR.getChatRoomName())
                    .newMessages(newMessage)
                    .build();
            chatRoomList.add(chatRoomListAdminResponseDto);
        }
        Long userFind = user.getId();
        Map<String, Object> getAdminChatRooms = new HashMap<>();
        getAdminChatRooms.put("data", chatRoomList);
        getAdminChatRooms.put("userId", userFind);
        //  날짜 이름 방번호
        return getAdminChatRooms;
    }

    // 채팅방 나가기
    @Transactional
    public void quitChat(Long roomId) {
        chatRoomRepository.deleteByRoomId(roomId);
        chatMessageRepository.deleteByRoomId(roomId.toString());
        allChatInfoRepository.deleteByChatRoom_RoomId(roomId);
    }

    // redis 에 저장했던 sessionId 로 userId 를 얻어오고 해당 userId 로 User 객체를 찾아 리턴함
    public User chkSessionUser(String sessionId) {
        Long userId = Long.parseLong(Objects.requireNonNull(hashOpsUserInfo.get(USER_INFO, sessionId)));
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));
    }

    public String formmater(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd").format(localDateTime);
    }

    public ResponseEntity<?> getAllChatRoomsTest(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt" );
        pageable = PageRequest.of(page, pageable.getPageSize(), sort );
        Page<ChatRoomCustomRepository> AllChatRoomTest = chatRoomRepository.findAllByOrderByRoomId(pageable);
        return ResponseEntity.ok().body(AllChatRoomTest);
    }
}

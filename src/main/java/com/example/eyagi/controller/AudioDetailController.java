package com.example.eyagi.controller;


import com.example.eyagi.dto.AudioDetailDto;
import com.example.eyagi.dto.CommentDto;
import com.example.eyagi.model.AudioBook;
import com.example.eyagi.repository.FollowRepository;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.AudioDetailService;
import com.example.eyagi.service.FollowService;
import com.example.eyagi.service.UserPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/audio/detail")
@RequiredArgsConstructor
@RestController
public class AudioDetailController {

    private final AudioDetailService audioDetailService;
    private final UserPageService userPageService;
    private final FollowRepository followRepository;
    private final FollowService followService;


    //오디오북 상세페이지 조회 & 조회한 오디오북을 마이페이지 > 내가 듣고 있는 오디오북에 추가 + 조회 하는 유저가 해당 셀러를 팔로우햇는지 여부 추가
    @GetMapping("/{audioBookId}")
    public ResponseEntity<Map<String, Object>> audioBookDetail(@PathVariable Long audioBookId,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        AudioBook audioBook = audioDetailService.findAudioBook(audioBookId);
        AudioDetailDto audioDetailDto = audioDetailService.getAudioDetail(audioBook, userDetails.getUser());
        boolean b = followService.followStatus(userDetails.getUser().getId(), audioBook.getSeller().getId());
        Map<String, Object> audioBookDetail = new HashMap<>();
        audioBookDetail.put("audioBookDetail", audioDetailDto);
        audioBookDetail.put("followStatus", b);
        return ResponseEntity.ok(audioBookDetail);
    }

    //오디오북 상세페이지 -> 후기 목록
//    @GetMapping("/{audioBookId}/comment")
//    public ResponseEntity getComment (@PathVariable Long audioBookId){
//
//        List<CommentDto> commentDtoList = audioDetailService.commentList(audioBookId);
//
//        return new ResponseEntity(commentDtoList, HttpStatus.OK);
//    }

    //후기 조회
    @GetMapping("/{audioBookId}/comment")
    public ResponseEntity<?> getComment(@PathVariable Long audioBookId, Pageable pageable) {
        return audioDetailService.commentList(audioBookId, pageable);
    }

    //후기 등록
    @PostMapping("/{audioBookId}/comment/new")
    public ResponseEntity newComment(@PathVariable Long audioBookId, @RequestBody CommentDto commentDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long commentId = audioDetailService.newComment(audioBookId, commentDto, userDetails.getUser());
        Map<String, Object> message = new HashMap<>();
        message.put("newCommentId", commentId);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    //후기 수정
    @PutMapping("/comment/edit/{commentId}")
    public ResponseEntity editComment(@PathVariable Long commentId, @RequestBody CommentDto commentDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long editCommentId = audioDetailService.editComment(commentId, userDetails.getUser(), commentDto);
        Map<String, Object> message = new HashMap<>();
        message.put("editCommentId", editCommentId);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    //후기 삭제
    @DeleteMapping("/comment/remove/{commentId}")
    public ResponseEntity removeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long removeCommentId = audioDetailService.removeComment(commentId, userDetails.getUser());
        Map<String, Object> message = new HashMap<>();
        message.put("removeCommentId", removeCommentId);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    //오디오파일 삭제
    @DeleteMapping("audiofile/remove/{audiofileId}")
    public Long removeAudioFile(@PathVariable Long audiofileId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        audioDetailService.removeAudiofile(audiofileId);
        return audiofileId;
    }
    //오디오북 상세페이지 삭제
    @DeleteMapping("/remove/{audioBookId}")
    public Long removeAuidoBook(@PathVariable Long audioBookId){
        audioDetailService.removeAudioBook(audioBookId);
        return audioBookId;
    }

}

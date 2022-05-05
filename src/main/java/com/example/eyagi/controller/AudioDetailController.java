package com.example.eyagi.controller;

import com.example.eyagi.dto.AudioDetailDto;
import com.example.eyagi.dto.CommentDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.AudioDetailService;
import com.example.eyagi.service.UserPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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



    //오디오북 상세페이지 조회 & 조회한 오디오북을 마이페이지 > 내가 듣고 있는 오디오북에 추가
    @GetMapping("/{audioBookId}")
    public ResponseEntity<AudioDetailDto> audioBookDetail (@PathVariable Long audioBookId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(audioDetailService.getAudioDetail(audioBookId, userDetails.getUser()));
    }

    //오디오북 상세페이지 -> 후기 목록
    @GetMapping("/{audioBookId}/comment")
    public ResponseEntity getComment (@PathVariable Long audioBookId){

        List<CommentDto> commentDtoList = audioDetailService.commentList(audioBookId);

        return new ResponseEntity(commentDtoList,HttpStatus.OK);
    }

    //후기 등록
    @PostMapping("/{audioBookId}/comment/new")
    public ResponseEntity newComment (@PathVariable Long audioBookId, @RequestBody CommentDto commentDto,
                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long commentId = audioDetailService.newComment(audioBookId,commentDto,userDetails.getUser());
        Map<String, Object> message = new HashMap<>();
        message.put("newCommentId", commentId);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    //후기 수정
    @PutMapping("/comment/edit/{commentId}")
    public ResponseEntity editComment (@PathVariable Long commentId, @RequestBody CommentDto commentDto,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long editCommentId = audioDetailService.editComment(commentId, userDetails.getUser() , commentDto);
        Map<String, Object> message = new HashMap<>();
        message.put("editCommentId", editCommentId);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    //후기 삭제
    @DeleteMapping("/comment/remove/{commentId}")
    public ResponseEntity removeComment(@PathVariable Long commentId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long removeCommentId = audioDetailService.removeComment(commentId, userDetails.getUser());
        Map<String, Object> message = new HashMap<>();
        message.put("removeCommentId", removeCommentId);
        return new ResponseEntity(message, HttpStatus.OK);
    }



}

package com.example.eyagi.controller;

import com.example.eyagi.dto.AudioDetailDto;
import com.example.eyagi.dto.CommentDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.AudioDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("audio/detail")
@RequiredArgsConstructor
@RestController
public class AudioDetailController {

    private final AudioDetailService audioDetailService;



    //오디오북 상세페이지 조회
    @GetMapping("/{audioBookId}")
    public ResponseEntity<AudioDetailDto> audioBookDetail (@PathVariable Long audioBookId){
        return ResponseEntity.ok(audioDetailService.getAudioDetail(audioBookId));
    }

    //오디오북 상세페이지 -> 후기 목록
    @GetMapping("/{audioBookId}/comment")
    public ResponseEntity<List<CommentDto>> getComment (@PathVariable Long audioBookId){
        return ResponseEntity.ok(audioDetailService.commentList(audioBookId));
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

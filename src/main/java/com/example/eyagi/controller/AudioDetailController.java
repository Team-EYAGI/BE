package com.example.eyagi.controller;

import com.example.eyagi.dto.AudioDetailDto;
import com.example.eyagi.service.AudioDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public void getComment (@PathVariable Long audioBookId){

    }


    //오디오북 상세페이지 , 후기 등록
    @PostMapping("{audioBookId}/comment/new")
    public void newComment (@PathVariable Long audioBookId){

    }



}

package com.example.eyagi.controller;


import com.example.eyagi.dto.BooksDto;
import com.example.eyagi.service.BookDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/book/detail")
@RequiredArgsConstructor
@RestController
public class BookDetailController {

    private final BookDetailService bookDetailService;

    //책 상세페이지 불러오기
    @GetMapping("/{bookId}")
        public BooksDto bookDetail (@PathVariable Long bookId){

            return bookDetailService.readBookDetail(bookId);
        //아직 판매자가 작성하는 오디오북 소개글 없음.
        //아직 오디오 북이 없기때문에 오디오 미리듣기 부분에 들어가는 정보 없음.
    }

    //오디오에 찜 누르기
    @PostMapping("/{bookId}/heart/{audioId}")
    public void audioLike(@PathVariable Long bookId , @PathVariable Long audioId){


    }

    //오디오북 등록하기.
    @PostMapping("/newAudio/{bookId}")
    public void newAudioBook (@PathVariable Long bookId, @RequestPart(name = "audio")MultipartFile file,
                              @RequestPart(name = "contents")String contents){
        //책 id 값과 오디오 파일 , 목차 들어옴.

    }
}

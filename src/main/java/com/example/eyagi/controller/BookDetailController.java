package com.example.eyagi.controller;


import com.example.eyagi.dto.BooksDto;
import com.example.eyagi.service.BookDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/book/detail")
@RequiredArgsConstructor
@RestController
public class BookDetailController {

    private final BookDetailService bookDetailService;

    //책 상세페이지 불러오기
    @GetMapping("/{bookId}")
        public BooksDto bookDetail (@PathVariable Long bookId){
            return bookDetailService.readBookDetail(bookId);
    }

//
//    //오디오에 찜 누르기
//    @PostMapping("/{bookId}/heart/{audioId}")
//    public void audioLike(@PathVariable Long bookId , @PathVariable Long audioId){
//
//
//    }

}

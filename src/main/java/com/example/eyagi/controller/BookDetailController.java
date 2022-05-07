package com.example.eyagi.controller;


import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.BookDetailService;
import com.example.eyagi.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/book/detail")
@RequiredArgsConstructor
@RestController
public class BookDetailController {

    private final BookDetailService bookDetailService;
    private final UserPageService userPageService;

    //책 상세페이지 불러오기
    @GetMapping("/{bookId}")
        public Map<String, Object> bookDetail (@PathVariable Long bookId){
            return bookDetailService.readBookDetail(bookId);
    }

    //내 서재에 책 담기
    @PostMapping("/{bookId}/heart")
    public ResponseEntity<String> heartBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userPageService.heartBook(userDetails.getUsername(), bookId);
        return ResponseEntity.ok("도서가 서재에 쏙 담겼습니다!");
    }

}

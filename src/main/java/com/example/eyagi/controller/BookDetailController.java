package com.example.eyagi.controller;

import com.example.eyagi.Interceptor.Auth;
import com.example.eyagi.model.Fund;
import com.example.eyagi.model.Library_Books;
import com.example.eyagi.repository.FundRepository;
import com.example.eyagi.repository.Library_BooksRepository;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.BookDetailService;
import com.example.eyagi.service.BooksService;
import com.example.eyagi.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequestMapping("/book/detail")
@RequiredArgsConstructor
@RestController
public class BookDetailController {

    private final BookDetailService bookDetailService;
    private final UserPageService userPageService;
    private final FundRepository fundRepository;
    private final BooksService booksService;



    //책 상세페이지 불러오기
    @GetMapping("/{bookId}")
        public Map<String, Object> bookDetail (@PathVariable Long bookId){
            return bookDetailService.readBookDetail(bookId);
    }

    //특정 책에 대해서 펀딩 성공했는지 여부 확인
    @Auth
    @PostMapping("/{bookId}/success")
    public boolean fundSuccessCheck (@PathVariable Long bookId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        Optional<Fund> fund = fundRepository.findByUserAndBooks(userDetails.getUser(), booksService.findBook(bookId));
        return fund.map(Fund::isSuccessGoals).orElse(false); //똑똑한 인텔리제이가 해줌.. 뭔지는 공부해봐야함.
    }


    //내 서재에 책 담기
    @PostMapping("/{bookId}/heart")
    public ResponseEntity<String> heartBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok( userPageService.heartBook(userDetails.getUsername(), bookId));
    }

}

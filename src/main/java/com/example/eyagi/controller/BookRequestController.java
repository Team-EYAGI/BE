package com.example.eyagi.controller;

import com.example.eyagi.dto.BookRequestDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.BookRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class BookRequestController {

    private final BookRequestService bookRequestService;

    //모든 책 불러오기
@GetMapping("/book/request")
    public ResponseEntity<List<BookRequestDto.ResponesDto>> findAllRequest(){
        return ResponseEntity.ok(bookRequestService.findAllRequest());
    }


    //책요청 저장하기
 @PostMapping("/book/request/new")
    public ResponseEntity saveRequest(@RequestBody BookRequestDto bookRequestDto,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        String userEmail = userDetails.getUsername();
        System.out.println(userDetails.getUsername());
        Long requestId = bookRequestService.save(bookRequestDto,userEmail);

        Map<String, Object> message = new HashMap<>();
        message.put("bookRequestId", requestId);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    //책요청 수정
@PutMapping("/book/request/edit/{bookRequestId}")
    public Long updateBookRequest(@PathVariable Long bookRequestId, @RequestBody BookRequestDto bookRequestDto){
    bookRequestService.update(bookRequestId,bookRequestDto);
    return bookRequestId;
}

//책요청게시물 삭제
//    @DeleteMapping ("/book/request/remove/{bookRequestId}")
//    public Long deleteBookRequest(@PathVariable Long bookRequestId){
//    bookRequestService.delete(bookRequestId);
//    return bookRequestId;
//    }
}


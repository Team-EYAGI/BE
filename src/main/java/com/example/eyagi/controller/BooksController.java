package com.example.eyagi.controller;

import com.example.eyagi.service.BooksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BooksController {


    private final BooksService booksService;

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getBookbyCategory(@PathVariable String category, Pageable pageable){
        return booksService.findBooksByCategory(category, pageable);
    }


}




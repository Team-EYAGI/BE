package com.example.eyagi.controller;

import com.example.eyagi.dto.BooksDto;
import com.example.eyagi.service.BooksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BooksController {


    private final BooksService booksService;

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getBookbyCategory(@PathVariable String category, Pageable pageable){
        return booksService.findBooksByCategory(category, pageable);
    }

    // 카테고리 별로 보여주기 (내용빼고)
//    @GetMapping("/category/{category}")
//    public List<BooksDto> getBookbyCategory(@PathVariable String category){
//        List<BooksDto>book = booksService.findBooksByCategory(category);
//        return book;
//    }

    // 메인화면에서 추천도서 보여주기
    @GetMapping("/")
    public List<BooksDto> getBookListToMain(HttpServletRequest request){

        return booksService.showMainBooks();
    }

    // 메인화면에서 자기계발 카테고리 보여주기
    @GetMapping("/category")
    public List<BooksDto> getCategoryListToMain(){
        return booksService.mainSeifList();
    }

  /*
   //모든 책 불러오기
   @GetMapping("/")
    public ResponseEntity<?> getAllBooks(){
        return new ResponseEntity<>(booksService.getAllBooks(), HttpStatus.OK);
    }*/


/*  // 크롤링 등록
@PostMapping("/category")
    public void getBookByCategory() throws IOException {
       booksCrawlingService.getCategoryBooks("01");
        booksCrawlingService.getCategoryBooks("03");
        booksCrawlingService.getCategoryBooks("13");
        booksCrawlingService.getCategoryBooks("41");
        booksCrawlingService.getCategoryBooks("15");

    }*/

}




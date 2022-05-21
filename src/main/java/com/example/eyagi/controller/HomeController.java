package com.example.eyagi.controller;

import com.example.eyagi.dto.BooksDto;
import com.example.eyagi.dto.TodayCreatorDto;
import com.example.eyagi.service.BooksService;
import com.example.eyagi.service.FundService;
import com.example.eyagi.service.HomeService;
import com.example.eyagi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HomeController {

    private final BooksService booksService;
    private final FundService fundService;
    private final UserService userService;
    private final HomeService homeService;

    // 메인화면에서 추천도서 보여주기
    @GetMapping("/")
    public List<BooksDto> getBookListToMain(@CookieValue(value = "oneTimeCookie", required = false) String oneTimeCookie,
                                            @CookieValue(value = "monthCookie" ,required = false) String monthCookie,
                                            HttpServletRequest request, HttpServletResponse response){
        if (oneTimeCookie == null){
            homeService.selectOneTimeCookie(response);
            //VisitCount에 ++ 해줘야함
        }
        if(monthCookie == null){
            homeService.selectMonthCookie(response);
            //VisitCount에 ++ 해줘야함
        }




        return booksService.showMainBooks();
    }

//    @GetMapping("/")
//    public List<BooksDto> getBookListToMain(HttpServletRequest request, HttpServletResponse response){
////        if (oneTimeCookie == null){
////            homeService.selectOneTimeCookie(response);
////            //VisitCount에 ++ 해줘야함
////        }
////        if(monthCookie == null){
////            homeService.selectMonthCookie(response);
////            //VisitCount에 ++ 해줘야함
////        }
//
//        return booksService.showMainBooks();
//    }

    // 메인화면에서 자기계발 카테고리 보여주기
    @GetMapping("/category")
    public List<BooksDto> getCategoryListToMain() {
        return booksService.mainSeifList();
    }


    // 메인화면에서 펀딩목록 보여주기
    @GetMapping("/main/fund")
    public ResponseEntity<?> getFundListToMain(){
        return fundService.mainFundList();
    }

    // 메인에 Best 목소리
    @GetMapping("/user/todayCreator")
    public List<TodayCreatorDto> todayCreator(){
        return userService.showMainCreator();
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
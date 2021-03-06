package com.example.eyagi.controller;

import com.example.eyagi.dto.BooksDto;
import com.example.eyagi.dto.TodayCreatorDto;
import com.example.eyagi.model.VisitCount;
import com.example.eyagi.repository.ClientAdressRepository;
import com.example.eyagi.repository.VisitCountRepository;
import com.example.eyagi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HomeController {

    private final BooksService booksService;
    private final FundService fundService;
    private final UserService userService;
    private final HomeService homeService;
    private final VisitCountRepository visitCountRepository;
    private final ClientAdressRepository clientAdressRepository;
    private final BooksCrawlingService booksCrawlingService;

    // 메인화면에서 추천도서 보여주기
    @GetMapping("/")
    public List<BooksDto> getBookListToMain() {

        return booksService.showMainBooks();
    }


    @GetMapping("/cookie")
    public void cookie(@CookieValue(value = "oneTimeCookie", required = false) Cookie oneTimeCookie1,
                       HttpServletResponse response,HttpServletRequest request) throws UnsupportedEncodingException {

        LocalDate nowDay = LocalDate.now();
        VisitCount toDayCount = homeService.newDayNewCount(nowDay);

        if (oneTimeCookie1 == null) {

            homeService.selectOneTimeCookie(response, toDayCount);
        }

        String ip = homeService.etRemoteAddr(request);
//        System.out.println(ip);
        homeService.addressCheck(ip, toDayCount);

        visitCountRepository.save(toDayCount);
    }

    // 메인화면에서 자기계발 카테고리 보여주기
    @GetMapping("/category")
    public List<BooksDto> getCategoryListToMain() {
        return booksService.mainSeifList();
    }


    // 메인화면에서 펀딩목록 보여주기펀딩
    @GetMapping("/main/fund")
    public ResponseEntity<?> getFundListToMain(){
        return fundService.mainFundList();
    }

    // 메인에 Best 목소리
    @GetMapping("/user/todayCreator")
    public List<TodayCreatorDto> todayCreator(){
        return userService.showMainCreator();
    }


    // 크롤링 등록
    @PostMapping("/crawling/category")
    public void getBookByCategory(@PathVariable String id) throws IOException {
       booksCrawlingService.getCategoryBooks(id);
    }

}

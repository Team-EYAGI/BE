package com.example.eyagi.controller;


import com.example.eyagi.dto.BookSearchDto;
import com.example.eyagi.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    @GetMapping("/search")
    public ResponseEntity<Map<String,Object>> SearchData(@RequestParam("search") String search, HttpServletRequest request){
        log.info("검색"+search);

        Map<String,Object> map = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try{
            map.put("book",searchService.findBooks(search));
            map.put("user",searchService.findSeller(search));
        }catch (Exception e){
            log.warn("전체 검색 에러 : {}", e.getMessage());
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(map,status) ;
    }
}

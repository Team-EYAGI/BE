package com.example.eyagi.service;

import com.example.eyagi.model.Books;
import com.example.eyagi.repository.BooksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@Component
public class BooksCrawlingService {

    private final BooksRepository booksRepository;

    //카테고리 별 크롤링 메서드
    public void getCategoryBooks(String linkClass)throws IOException{


        String categoryValue = "";

        if(linkClass.equals("01")){
            categoryValue= "novel";
        }else if(linkClass.equals("03")){
            categoryValue="poem";
        }else if(linkClass.equals("13")){
            categoryValue="economy";
        }else if(linkClass.equals("41")){
            categoryValue="kids";
        }else if(linkClass.equals("15")){
            categoryValue = "self";
        }
/*   //소설
        http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=01&menuCode=002
        //시
        http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=03&menuCode=002
        //경제
        http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=13&menuCode=002
        //아동
        http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=41&menuCode=002
        //자기계발
        http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=15&menuCode=002*//*
*/

        String url = "http://www.kyobobook.co.kr/categoryRenewal/categoryMain.laf?perPage=20&mallGb=KOR&linkClass=" + linkClass + "&menuCode=002";
        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.select("ul.prd_list_type1");
        // li 밑에 모든 값이 가져오기
        Iterator<Element> bookIterator = elements.select(".id_detailli").iterator();


        while(bookIterator.hasNext()){
            Element bookInfo = bookIterator.next();
            String bookImg = bookInfo.select("div.cover img").attr("src");
            String author = bookInfo.select("div.detail div.pub_info span.author").text();
            //publisher가 값이 두개라서 출판사부분만 꺼내오기 위해서 잘라줌
            String publishers = bookInfo.select("div.detail div.pub_info span.publication").text();
            String[] str = publishers.split(" ");
            String publisher = str[0];

            String title = bookInfo.select("div.detail div.title a > strong").text();


            Books books = Books.builder()
                    .bookImg(bookImg)
                    .author(author)
                    .publisher(publisher)
                    .title(title)
                    .summary(null)
                    .category(categoryValue)
                            .build();

            booksRepository.save(books);
        }
    }
}

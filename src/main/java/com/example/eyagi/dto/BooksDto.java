package com.example.eyagi.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class BooksDto {
//오디오 리스트 없애고 서비스에서 맵으로 묶어서 뷰로 내려주도록 수정!!!
    private Long bookId;
    private String title;
    private String publisher;
    private String author;
    private String category;
    private String bookImg;


    //책 소개 글
    private String summary;
    //뷰로 내보내 줄 오디오북 미리보기 정보. 미리듣기 파일, 셀러 정보, ...
    private List<AudioPreDto> audioPreDtoList;

//    public BooksDto(Books books){
//        this.bookId = books.getBookId();
//        this.bookImg = books.getBookImg();
//        this.title = books.getTitle();
//        this.publisher = books.getPublisher();
//        this.author = books.getAuthor();
//        this.category = books.getCategory();
//    }
//
//    public BooksDto(Books books, List<AudioPreDto> audioPreDtoList){
//        this.bookId = books.getBookId();
//        this.bookImg = books.getBookImg();
//        this.title = books.getTitle();
//        this.publisher = books.getPublisher();
//        this.author = books.getAuthor();
//        this.category = books.getCategory();
//        this.summary = books.getSummary();
//        this.audioPreDtoList = audioPreDtoList;
//    }

}

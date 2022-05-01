package com.example.eyagi.dto;


import com.example.eyagi.model.Books;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
//@AllArgsConstructor
@Getter
//@Setter
public class BooksDto {

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
//        this.setBookId(books.getBookId());
//        this.setBookImg(books.getBookImg());
//        this.setCategory(books.getCategory());
//        this.setPublisher(books.getPublisher());
//        this.setTitle(books.getTitle());
//        this.setAuthor(books.getAuthor());
//
//    }

    public BooksDto(Books books){
        this.bookId = books.getBookId();
        this.bookImg = books.getBookImg();
        this.title = books.getTitle();
        this.publisher = books.getPublisher();
        this.author = books.getAuthor();
        this.category = books.getCategory();
    }

    public BooksDto(Books books, List<AudioPreDto> audioPreDtoList){
        this.bookId = books.getBookId();
        this.bookImg = books.getBookImg();
        this.title = books.getTitle();
        this.publisher = books.getPublisher();
        this.author = books.getAuthor();
        this.category = books.getCategory();
        this.summary = books.getSummary();
        this.audioPreDtoList = audioPreDtoList;
    }

}

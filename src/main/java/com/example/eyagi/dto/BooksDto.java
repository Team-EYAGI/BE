package com.example.eyagi.dto;

import com.example.eyagi.model.Books;
import lombok.*;


@NoArgsConstructor
@Getter
public class BooksDto {
    private Long bookId;
    private String title;
    private String publisher;
    private String author;
    private String category;
    private String bookImg;


//    //책 소개 글
//    private String summary;
//    //뷰로 내보내 줄 오디오북 미리보기 정보. 미리듣기 파일, 셀러 정보, ...
//    private List<AudioPreDto> audioPreDtoList;

    public BooksDto(Books books){
        this.bookId = books.getBookId();
        this.bookImg = books.getBookImg();
        this.title = books.getTitle();
        this.publisher = books.getPublisher();
        this.author = books.getAuthor();
        this.category = books.getCategory();
    }

public BooksDto(Long bookId, String bookImg, String title, String publisher, String author, String category){
    this.bookId = bookId;
    this.bookImg = bookImg;
    this.title = title;
    this.publisher = publisher;
    this.author = author;
    this.category = category;
    }
}

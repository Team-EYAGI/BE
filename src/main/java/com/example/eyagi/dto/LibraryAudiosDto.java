package com.example.eyagi.dto;


import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.Books;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LibraryAudiosDto {
    /*
    책제목, 책이미지, 책 아이디, 카테고리, 저자이름, 크리에이터 이름, 오디오북 아이디
     */
    private Long bookId;  //책 아이디
    private String title;  //책제목
    private String author;
    private String category;   //카테고리
    private String bookImg;  //책이미지

    private Long audioBookId;

    private String sellerName; //셀러 닉네임

    public LibraryAudiosDto(Books books, AudioBook audioBook) {
        this.bookId = books.getBookId();
        this.title = books.getTitle();
        this.author = books.getAuthor();
        this.category = books.getCategory();
        this.bookImg = books.getBookImg();
        this.audioBookId = audioBook.getId();
        this.sellerName = audioBook.getSeller().getUsername();

    }

}

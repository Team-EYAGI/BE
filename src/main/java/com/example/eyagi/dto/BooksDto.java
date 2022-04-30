package com.example.eyagi.dto;


import com.example.eyagi.model.Books;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BooksDto {

    private Long bookId;
    private String title;
    private String publisher;
    private String author;
    private String category;
    private String bookImg;


    public BooksDto(Books books){
        this.setBookId(books.getBookId());
        this.setBookImg(books.getBookImg());
        this.setCategory(books.getCategory());
        this.setPublisher(books.getPublisher());
        this.setTitle(books.getTitle());
        this.setAuthor(books.getAuthor());

    }
}

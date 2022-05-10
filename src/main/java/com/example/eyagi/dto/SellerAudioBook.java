package com.example.eyagi.dto;

import com.example.eyagi.model.AudioBook;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SellerAudioBook {
    private String sellerName;
    private Long audioBookId;
    private String bookTitle;
    private String author;
    private String bookImage;

    public SellerAudioBook (AudioBook audioBook){
        this.sellerName =audioBook.getSeller().getUsername();
        this.audioBookId = audioBook.getId();
        this.bookTitle = audioBook.getBook().getTitle();
        this.author = audioBook.getBook().getAuthor();
        this.bookImage = audioBook.getBook().getBookImg();
    }
}

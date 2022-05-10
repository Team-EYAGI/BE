package com.example.eyagi.dto;


import com.example.eyagi.model.Fund;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SellerFundDto {

    private String bookTitle;
    private String author;
    private String bookImg;
    private String fundFile;

    public SellerFundDto (Fund fund){
        this.bookTitle = fund.getBooks().getTitle();
        this.author = fund.getBooks().getAuthor();
        this.bookImg = fund.getBooks().getBookImg();
        this.fundFile =fund.getAudioFund().getFundFile();
    }
}

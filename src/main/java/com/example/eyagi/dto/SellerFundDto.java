package com.example.eyagi.dto;


import com.example.eyagi.model.Fund;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class SellerFundDto {

    private String bookTitle;
    private String author;
    private String bookImg;
    private Long fundId;
    private String sellerName;
    //    private String fundFile;
    private int likeCnt;
    private boolean successFunding; //펀딩 성공 여부

    public SellerFundDto(Fund fund) {
        this.bookTitle = fund.getBooks().getTitle();
        this.author = fund.getBooks().getAuthor();
        this.bookImg = fund.getBooks().getBookImg();
//        this.fundFile =fund.getAudioFund().getFundFile();
        this.fundId = fund.getFundId();
        this.likeCnt = fund.getHeartCnt();
        this.successFunding = fund.isSuccessGoals();
    }
}

package com.example.eyagi.repository.QRepository;

public interface FundCustomRepository {
    Long getFundId();
    String getSellerName();
    String getContent();
    Integer getLikeCnt();
    String getFundFile();
    String getBookTitle();
    String getAuthor();
    String getBookImg();
    Integer getFundingGoals();
    Boolean getSuccessFunding();
}

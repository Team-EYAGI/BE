package com.example.eyagi.repository.QRepository;

public interface FundCustomRepository {
    Long getFundId();
    String getSellerName();
    Integer getLikeCnt();
    String getFundFile();
    String getBookTitle();
    String getBookImg();
    Integer getFundingGoals();
    Boolean getSuccessFunding();
}

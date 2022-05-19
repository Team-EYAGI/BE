package com.example.eyagi.repository.QRepository;

public interface BooksCustomRepository {
    Long getBookId();
    String getBookImg();
    String getTitle();
    String getPublisher();
    String getAuthor();
    String getCategory();
}

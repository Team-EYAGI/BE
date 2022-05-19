package com.example.eyagi.repository;

import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.Books;
import com.example.eyagi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AudioBookRepository extends JpaRepository<AudioBook, Long> {

    AudioBook findByBookAndSeller(Books book, User seller); // 특정 책의 특정 판매자의 오디오북 조회.

    @Query(value = "select distinct ab.book.BookId, ab.book.title, ab.book.author, ab.book.publisher, ab.book.category, ab.book.bookImg from AudioBook as ab")
    List<AudioBook> findAllOrderByBook_BookId();
}

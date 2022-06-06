package com.example.eyagi.repository;


import com.example.eyagi.model.Books;
import com.example.eyagi.repository.QRepository.BooksCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BooksRepository extends JpaRepository<Books,Long> {
    List<Books> findByCategory(String category);


    @Query(value = "select b.BookId as bookId, b.bookImg as bookImg, b.title as title, b.publisher as publisher," +
            "b.author as author, b.category as category from Books as b where b.category=:category order by b.BookId",
            countQuery = "select count(b.BookId) from Books b where b.category=:category")
    Page<BooksCustomRepository> findByCategory(String category, Pageable pageable);
}

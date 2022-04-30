package com.example.eyagi.repository;


import com.example.eyagi.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BooksRepository extends JpaRepository<Books,Long> {
    List<Books> findByCategory(String category);
    List<Books>findByCategoryContains(String category);

}

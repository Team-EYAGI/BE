package com.example.eyagi.repository;

import com.example.eyagi.model.Books;
import com.example.eyagi.model.Library_Books;
import com.example.eyagi.model.UserLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Library_BooksRepository extends JpaRepository<Library_Books, Long> {
    Library_Books findByBookAndUserLibrary(Books book, UserLibrary userLibrary);
    List<Library_Books> findAllByUserLibraryOrderByIdDesc(UserLibrary userLibrary);

}

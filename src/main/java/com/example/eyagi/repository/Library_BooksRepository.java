package com.example.eyagi.repository;

import com.example.eyagi.model.Library_Books;
import com.example.eyagi.model.UserLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Library_BooksRepository extends JpaRepository<Library_Books, Long> {


//    List<Library_Books> findAllByOrderByUserLibraryDesc(UserLibrary userLibrary);
}

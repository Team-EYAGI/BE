package com.example.eyagi.repository;

import com.example.eyagi.model.UserLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {


}

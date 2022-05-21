package com.example.eyagi.repository;

import com.example.eyagi.model.UserLibrary;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {


    UserLibrary findByUserId(Long id);


}

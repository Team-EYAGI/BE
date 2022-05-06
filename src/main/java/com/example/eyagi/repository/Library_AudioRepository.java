package com.example.eyagi.repository;

import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.Library_Audio;
import com.example.eyagi.model.UserLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Library_AudioRepository extends JpaRepository<Library_Audio,Long> {

    Library_Audio findByAudioBookAndUserLibrary(AudioBook audioBook, UserLibrary userLibrary);
//
//    List<Library_Audio> findAllByOrderByUserLibraryDesc(UserLibrary userLibrary);
}

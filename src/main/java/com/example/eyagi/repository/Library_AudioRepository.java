package com.example.eyagi.repository;

import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.Library_Audio;
import com.example.eyagi.model.UserLibrary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Library_AudioRepository extends JpaRepository<Library_Audio,Long> {

    Optional<Library_Audio> findByAudioBook_IdAndUserLibrary_Id(Long audioBook, Long userLibrary);

    List<Library_Audio> findAllByUserLibraryOrderByIdDesc(UserLibrary userLibrary);

    Library_Audio findByUserLibraryAndAudioBook(UserLibrary userLibrary, AudioBook audioBook);

}

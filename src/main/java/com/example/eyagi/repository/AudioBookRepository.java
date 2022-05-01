package com.example.eyagi.repository;

import com.example.eyagi.model.AudioBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AudioBookRepository extends JpaRepository<AudioBook, Long> {

    List<AudioBook> findByBook(Long id); //특정 책에 속하는 오디오북 모두 찾기.

}

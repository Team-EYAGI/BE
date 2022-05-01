package com.example.eyagi.repository;

import com.example.eyagi.model.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioFileRepository extends JpaRepository<AudioFile,Long> {
}

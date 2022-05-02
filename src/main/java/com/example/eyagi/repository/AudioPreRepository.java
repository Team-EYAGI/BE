package com.example.eyagi.repository;

import com.example.eyagi.model.AudioPreview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioPreRepository extends JpaRepository<AudioPreview,Long> {
}

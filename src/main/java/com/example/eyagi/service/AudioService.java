package com.example.eyagi.service;

import com.example.eyagi.model.AudioBook;
import com.example.eyagi.repository.AudioBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Service
public class AudioService{

    private final AudioBookRepository audioBookRepository;

    // 특정 오디오북 찾기
    public AudioBook findAudioBook (Long id) {
        return audioBookRepository.findById(id).orElseThrow(
                () -> new NullPointerException("요청하신 오디오북이 존재하지 않습니다.")
        );
    }



    @Transactional
    public void removeFile (String path, String originalFile) throws IOException, InterruptedException {
        Path filePath = Paths.get(path + originalFile); //로컬에 남은 오디오 삭제.
        Files.delete(filePath);
    }
}

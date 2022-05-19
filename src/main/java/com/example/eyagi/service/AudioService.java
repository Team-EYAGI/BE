package com.example.eyagi.service;

import com.example.eyagi.model.*;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.repository.AudioFileRepository;
import com.example.eyagi.repository.AudioPreRepository;
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
    private final AudioFileRepository audioFileRepository;
    private final AudioPreRepository audioPreRepository;

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

    public AudioPreview saveAudioPreview (String cutFileS3, String cutFileS3Url ){
        AudioPreview audioPreview = AudioPreview.builder()
                .originName(cutFileS3)
                .s3FileName(cutFileS3Url)
                .build();
        audioPreRepository.save(audioPreview);
        return audioPreview;
    }

    public AudioBook saveAudioBook (User seller, Books book, AudioPreview audioPreview, String contents){
        AudioBook audioBook1 = AudioBook.builder()
                .seller(seller)
                .book(book)
                .preview(audioPreview)
                .contents(contents) // 특정 도서에 오디오 개시때만 등록.
                .build();
        audioBookRepository.save(audioBook1);
        return audioBook1;
    }

    public void saveAudioFile (String originFileS3, String originFileS3Url, AudioBook audioBook){
        AudioFile audioFile = AudioFile.builder()
                .originName(originFileS3)
                .s3FileName(originFileS3Url)
                .audioBook(audioBook)
                .build();
        audioFileRepository.save(audioFile);

    }
}

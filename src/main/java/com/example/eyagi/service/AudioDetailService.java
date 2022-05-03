package com.example.eyagi.service;

import com.example.eyagi.dto.AudioDetailDto;
import com.example.eyagi.dto.AudioFileDto;
import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.AudioFile;
import com.example.eyagi.repository.AudioBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AudioDetailService {

    private final AudioBookRepository audioBookRepository;

    public AudioBook findAudioBook (Long id) {
        return audioBookRepository.findById(id).orElseThrow(
                ()-> new NullPointerException("요청한 오디오북이 존재하지 않습니다.")
        );
    }

    public AudioDetailDto getAudioDetail (Long id) {
        AudioBook audioBook = findAudioBook(id);
        List<AudioFile>audioFileList = audioBook.getAudioFile();
        List<AudioFileDto> audioFileDtoList = new ArrayList<>();
        for (AudioFile file : audioFileList){
            AudioFileDto audioFileDto = AudioFileDto.builder()
                    .id(file.getId())
                    .s3FileName(file.getS3FileName())
                    .build();
            audioFileDtoList.add(audioFileDto);
        }
        return AudioDetailDto.builder()
                 .title(audioBook.getBook().getTitle())
                 .author(audioBook.getBook().getAuthor())
                 .bookImg(audioBook.getBook().getBookImg())
                 .audioFileDtoList(audioFileDtoList)
                 .build();
    }



}

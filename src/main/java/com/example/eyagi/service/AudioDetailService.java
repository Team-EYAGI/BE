package com.example.eyagi.service;

import com.example.eyagi.dto.AudioDetailDto;
import com.example.eyagi.dto.AudioFileDto;
import com.example.eyagi.dto.CommentDto;
import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.AudioFile;
import com.example.eyagi.model.Comment;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AudioDetailService {

    private final AudioBookRepository audioBookRepository;
    private final CommentRepository commentRepository;
    private final AudioService audioService;

    public AudioBook findAudioBook (Long id) {
        return audioBookRepository.findById(id).orElseThrow(
                ()-> new NullPointerException("요청한 오디오북이 존재하지 않습니다.")
        );
    }

    //오디오북 상세 페이지 조회 1. 책 정보 + 오디오 목록
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


    //오디오북 상세 페이지 조회 2. 후기 목록
    public List<CommentDto> commentList(Long audioBookDetailId){

        List<Comment> commentList = commentRepository.findAllByAudioBook_Id(audioBookDetailId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment c : commentList){
            CommentDto commentDto = new CommentDto(c.getContent(), c.getUser().getUsername());
            commentDtoList.add(commentDto);
        }
        return commentDtoList;
    }


    //후기 등록
    public Long newComment(Long audioBookDetailId, CommentDto commentDto, User user) {

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(user)
                .audioBook(audioService.findAudioBook(audioBookDetailId))
                .build();
        commentRepository.save(comment);

        return comment.getId();
    }



}

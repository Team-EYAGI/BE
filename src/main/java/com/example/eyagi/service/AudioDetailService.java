package com.example.eyagi.service;

import com.example.eyagi.dto.AudioDetailDto;
import com.example.eyagi.dto.AudioFileDto;
import com.example.eyagi.dto.CommentDto;
import com.example.eyagi.model.*;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.repository.CommentRepository;
import com.example.eyagi.repository.Library_AudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AudioDetailService {

    private final UserPageService userPageService;
    private final AudioBookRepository audioBookRepository;
    private final CommentRepository commentRepository;
    private final AudioService audioService;
    private final Library_AudioRepository library_audioRepository;

    //특정 오디오북 찾기
    public AudioBook findAudioBook (Long id) {
        return audioBookRepository.findById(id).orElseThrow(
                ()-> new NullPointerException("요청한 오디오북이 존재하지 않습니다.")
        );
    }

    //오디오북 상세 페이지 조회 1. 책 정보 + 오디오 목록   ++ 셀러 닉네임, 오디오북 소개글 추가
    public AudioDetailDto getAudioDetail (Long id, User user) {
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
        Optional<Library_Audio> library_audio = library_audioRepository.findByAudioBook_IdAndUserLibrary_Id(audioBook.getId(),user.getUserLibrary().getId());
        if(!library_audio.isPresent()){ //해당 오디오북이 듣고 있는 오디오북 목록에 없다면, ->서버 테스트 완
            userPageService.listenBook(audioBook,user); // 마이페이지 > 내가 듣고 있는 오디오북에 추가!
            log.info(id + "번 오디오북을 내 서재에 추가!");
        }
        return AudioDetailDto.builder()
                .title(audioBook.getBook().getTitle())
                .author(audioBook.getBook().getAuthor())
                .bookImg(audioBook.getBook().getBookImg())
                .audioFileDtoList(audioFileDtoList)
                .sellerName(audioBook.getSeller().getUsername())  //셀러 닉네임 추가
                .audioInfo(audioBook.getContents())  // 오디오북 소개글 추가
                .sellerImage(audioBook.getSeller().getUserProfile().getUserImage()) //샐러 이미지 추가
                .build();
    }


    //오디오북 상세 페이지 조회 2. 후기 목록
    public List<CommentDto> commentList(Long audioBookDetailId){

        List<Comment> commentList = commentRepository.findAllByAudioBook_Id(audioBookDetailId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment c : commentList){
            CommentDto commentDto = new CommentDto(c,c.getUser().getUserProfile().getUserImage());
            commentDtoList.add(commentDto);
        }
        return commentDtoList;
    }


    //후기 등록
    public Long newComment(Long audioBookDetailId, CommentDto commentDto, User user) {

        Comment comment = new Comment(commentDto, user, audioService.findAudioBook(audioBookDetailId));
        commentRepository.save(comment);

        return comment.getId();
    }


    //후기 수정
    public Long editComment(Long commentId, User user, CommentDto commentDto){
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new NullPointerException("요청한 후기가 존재하지 않습니다.")
        );
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("요청자가 후기 작성자와 일치하지 않습니다.");
        }
        comment.update(commentDto);
        commentRepository.save(comment);
        return commentId;
    }

    //후기 삭제
    public Long removeComment(Long commentId, User user){
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new NullPointerException("요청한 후기가 존재하지 않습니다.")
        );
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("요청자가 후기 작성자와 일치하지 않습니다.");
        }
        commentRepository.delete(comment);
        return commentId;
    }

}

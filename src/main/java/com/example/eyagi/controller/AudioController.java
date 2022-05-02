package com.example.eyagi.controller;


import com.example.eyagi.model.*;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.repository.AudioFileRepository;
import com.example.eyagi.repository.AudioPreRepository;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AudioController {

    private final S3Uploader s3Uploader;
    private final AudioBookRepository audioBookRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioPreRepository audioPreRepository;
    private final BooksService booksService;
    private final AudioService audioService;



    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //자른 오디오 지정 경로
    static String path = "src/main/resources/static/";

    //성우가 해당 책에 오디오북을 처음 만드는 건지 확인해주는 부분.
    @GetMapping("book/detail/newAudio/check/{bookId}")
    public boolean newAudioCheck (@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Books book = booksService.findBook(bookId);
        User seller = userDetails.getUser();
        if(audioBookRepository.findByBookAndSeller(book, seller)== null){
            return true; //첫 등록이면 true
        } else {
            return false; //등록한 적이 있다면 false
        }
    }

    //오디오북 등록하기.
    @PostMapping("/book/detail/newAudio/{bookId}")
    public void newAudioBook (@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestPart(name = "audio") MultipartFile multipartFile,
                              @RequestPart(name = "contents" ,required = false )String contents) throws IOException {

        Books book = booksService.findBook(bookId);
        User seller = userDetails.getUser();

        AudioBook audioBook = audioBookRepository.findByBookAndSeller(book, seller);

        if (audioBook == null){

            AudioService2 audioService2 = new AudioService2(multipartFile, path, bucket);
            audioService2.start();

            try {
                audioService2.join();

                String originFileS3Url = s3Uploader.audioUpload(bucket, multipartFile, audioService2.getOriginFileS3());
                String cutFileS3Url = s3Uploader.copyAudioUpload(bucket,path,
                        audioService2.getCutFile(), audioService2.getCutFileS3());

                AudioPreview audioPreview = AudioPreview.builder()
                        .originName(audioService2.getCutFileS3())
                        .s3FileName(cutFileS3Url)
                        .build();
                audioPreRepository.save(audioPreview);

                AudioBook audioBook1 = AudioBook.builder()
                        .seller(seller)
                        .book(book)
                        .preview(audioPreview)
                        .contents(contents) // 특정 도서에 오디오 개시때만 등록.
                        .build();

                AudioFile audioFile = AudioFile.builder()
                        .originName(audioService2.getOriginFileS3())
                        .s3FileName(originFileS3Url)
                        .audioBook(audioBook1)
                        .build();
                audioFileRepository.save(audioFile);

                audioBook1.addAudio(audioFile);
                book.addAudioBook(audioBook1);

                audioBookRepository.save(audioBook1);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            audioService.removeFile(path, audioService2.getLocalFile());

        } else {

            String originFileS3 = "audio" +"/" + UUID.randomUUID() + "."
                    + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

            String originFileS3Url = s3Uploader.audioUpload(bucket, multipartFile, originFileS3);

            AudioFile audioFile = AudioFile.builder()
                    .originName(originFileS3)
                    .s3FileName(originFileS3Url)
                    .audioBook(audioBook)
                    .build();

            audioBook.addAudio(audioFile);
            audioFileRepository.save(audioFile);

        }
    }

    //오디오북 상세페이지 조회
    @GetMapping("audio/detail/{audioBookId}")
    public void audioBookDetail (@PathVariable Long audioBookId){

    }

}

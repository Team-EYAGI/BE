package com.example.eyagi.controller;

import com.example.eyagi.dto.AudioDetailDto;
import com.example.eyagi.model.*;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.repository.AudioFileRepository;
import com.example.eyagi.repository.AudioPreRepository;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.AudioService;
import com.example.eyagi.service.AudioService2;
import com.example.eyagi.service.AwsS3Service;
import com.example.eyagi.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AudioController {

    private final AwsS3Service awsS3Service;
    private final AudioBookRepository audioBookRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioPreRepository audioPreRepository;
    private final BooksService booksService;
    private final AudioService audioService;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //자른 오디오 지정 경로
//    static String path = "src/main/resources/static/"; //로컬테스트
//
    static String path = "/home/ubuntu/eyagi/audio/";  //배포시


        //성우가 해당 책에 오디오북을 처음 만드는 건지 확인해주는 부분.
    @GetMapping("/book/detail/newAudio/check/{bookId}")
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

    public ResponseEntity<String> newAudioBook(@PathVariable Long bookId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestPart(name = "audio") MultipartFile multipartFile,
                                               @RequestPart(name = "contents", required = false)AudioDetailDto.Request contents) throws IOException, InterruptedException {

        Books book = booksService.findBook(bookId);
        User seller = userDetails.getUser();

        AudioBook audioBook = audioBookRepository.findByBookAndSeller(book, seller);


        if (audioBook == null) {
            AudioService2 audioService2 = new AudioService2(multipartFile, path, bucket);
            audioService2.start();

            try {
                audioService2.join();

                String originFileS3Url = awsS3Service.fileUpload(bucket, multipartFile, audioService2.getOriginFileS3());
                String cutFileS3Url = awsS3Service.copyAudioUpload(bucket, path, audioService2.getCutFile(),
                                        audioService2.getCutFileS3());

                AudioPreview audioPreview = AudioPreview.builder()
                        .originName(audioService2.getCutFileS3())
                        .s3FileName(cutFileS3Url)
                        .build();
                audioPreRepository.save(audioPreview);

                AudioBook audioBook1 = AudioBook.builder()
                        .seller(seller)
                        .book(book)
                        .preview(audioPreview)
                        .contents(contents.getContents()) // 특정 도서에 오디오 개시때만 등록.
                        .build();
                audioBookRepository.save(audioBook1);

                AudioFile audioFile = AudioFile.builder()
                        .originName(audioService2.getOriginFileS3())
                        .s3FileName(originFileS3Url)
                        .audioBook(audioBook1)
                        .build();
                audioFileRepository.save(audioFile);


                book.addAudioBook(audioBook1);

                Thread.sleep(1000);
                audioService.removeFile(path, audioService2.getCutFile());
                audioService.removeFile(path, audioService2.getLocalFile());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return ResponseEntity.ok("오디오북 첫 개시에 성공하였습니다!");
        }
        else {

            String originFileS3 = "audio" +"/" + UUID.randomUUID() + "."
                    + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

            String originFileS3Url = awsS3Service.fileUpload(bucket, multipartFile, originFileS3);

            AudioFile audioFile = AudioFile.builder()
                    .originName(originFileS3)
                    .s3FileName(originFileS3Url)
                    .audioBook(audioBook)
                    .build();

            audioBook.addAudio(audioFile);
            audioFileRepository.save(audioFile);

            return ResponseEntity.ok("오디오 등록 성공!");

        }

    }

}
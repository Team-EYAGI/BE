package com.example.eyagi.controller;

import com.example.eyagi.dto.AudioDetailDto;
import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.AudioFile;
import com.example.eyagi.model.Books;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.repository.AudioFileRepository;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.AudioCutService;
import com.example.eyagi.service.AwsS3Service;
import com.example.eyagi.service.BooksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.example.eyagi.security.JwtProperties.filePath;
import static com.example.eyagi.service.AwsS3Path.pathAudio;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AudioCutController {

    private final AwsS3Service awsS3Service;
    private final AudioBookRepository audioBookRepository;
    private final AudioFileRepository audioFileRepository;
    private final BooksService booksService;
    private final AudioCutService exampleService;

//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;

    //자른 오디오 지정 경로
//    static String path = "src/main/resources/static/"; //로컬테스트
//
//    static String path = "/home/ubuntu/eyagi/audio/";  //배포시

    @Value("{$audio_path}")
    static String path= filePath;  //배포시

    @Async
    @PostMapping("/book/detail/newAudio/{bookId}")
    public CompletableFuture<ResponseEntity> test(@PathVariable Long bookId,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @RequestPart(name = "audio") MultipartFile multipartFile,
                                  @RequestPart(name = "contents", required = false) AudioDetailDto.Request contents) {

        return CompletableFuture.completedFuture(new ResponseEntity(exampleService.audioCutAsync(bookId,userDetails,multipartFile,contents.getContents()), HttpStatus.OK));

    }

    //첫등록 아닐경우
    @PostMapping("/book/detail/addAudio/{bookId}")
    public ResponseEntity<String> aa(@PathVariable Long bookId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails,
                                     @RequestPart(name = "audio") MultipartFile multipartFile) {
        Books book = booksService.findBook(bookId);
        User seller = userDetails.getUser();
        AudioBook audioBook = audioBookRepository.findByBookAndSeller(book, seller);

        Map<String, String> fileName = awsS3Service.uploadFile(multipartFile, pathAudio);

        AudioFile audioFile = AudioFile.builder()
                .originName(fileName.get("fileName"))
                .s3FileName(fileName.get("url"))
                .audioBook(audioBook)
                .build();

        audioBook.addAudio(audioFile);
        audioFileRepository.save(audioFile);

        log.info("오디오북 등록 성공! 등록한 셀러 : {}", seller.getId());
        return ResponseEntity.ok("오디오 등록 성공!");
    }


}

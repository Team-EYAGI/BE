package com.example.eyagi.controller;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.eyagi.dto.FilePath;
import com.example.eyagi.model.*;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.AudioService;
import com.example.eyagi.service.AwsS3Service;
import com.example.eyagi.service.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AudioController {

    private final AwsS3Service awsS3Service;
    private final AudioBookRepository audioBookRepository;
    private final BooksService booksService;
    private final AudioService audioService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //자른 오디오 지정 경로
    static String path = "src/main/resources/static/";


    //오디오북 등록하기.
    @PostMapping("/book/detail/newAudio/{bookId}")
    public void newAudioBook (@PathVariable Long bookId,
                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestPart(name = "audio") MultipartFile multipartFile,
                              @RequestPart(name = "contents" ,required = false )String contents){
        //책 id 값과 오디오 파일 , 목차 들어옴.
        Books book = booksService.findBook(bookId);
        User seller = userDetails.getUser();
        AudioBook audioBook = audioBookRepository.findByBookAndSeller(book, seller);
        if (audioBook == null){
            try {
//                AudioFile audioFile = new AudioFile();
//                AudioPreview audioPreview = new AudioPreview();

                FilePath filePath = audioService.fristAudioBook(multipartFile, path, bucket);

                audioService.save(filePath, seller, book, contents);

//                String multipartName = multipartFile.getOriginalFilename();
//                String localFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartName);
//                String originFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartName);
//                String originFileS3 = "audio" +"/" + localFile;
//                String cutFileS3 = "audioPreview" + "/" + originFile;
//
//                ObjectMetadata metadata = new ObjectMetadata();
//                metadata.setContentType(multipartFile.getContentType());
//                metadata.setContentLength(multipartFile.getSize());
//
//
//                    String originFileS3Url = awsS3Service.audioUpload(bucket, multipartFile, originFileS3, metadata);
//
//                    File file = audioService.fileConversion(multipartFile, path, localFile);
//                audioService.copyAudio(file, path + originFile, 1, 60);
//
//                    String cutFileS3Url = awsS3Service.copyAudioUpload(bucket,path, originFile, cutFileS3, metadata);


//                AudioPreview audioPreview = AudioPreview.builder()
//                        .originName(cutFileS3)
//                        .s3FileName(cutFileS3Url)
//                        .build();
//                AudioBook audioBook1 = AudioBook.builder()
//                        .seller(seller)
//                        .book(book)
//                        .preview(audioPreview)
//                        .build();
//                AudioFile audioFile = AudioFile.builder()
//                        .originName(originFileS3)
//                        .s3FileName(originFileS3Url)
//                        .audioBook(audioBook1)
//                        .num(contents)
//                        .build();
//
//                audioBook1.addAudio(audioFile);
//                book.addAudioBook(audioBook1);
//
//                audioService.removeFile(path, localFile);
//                audioService.removeFile(path, originFile);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
        } else {
            FilePath filePath = audioService.audioUpload(multipartFile, bucket);
            AudioFile audioFile = AudioFile.builder()
                    .originName(filePath.getOriginFileS3())
                    .s3FileName(filePath.getOriginFileS3Url())
                    .audioBook(audioBook)
                    .num(contents)
                    .build();

            audioBook.addAudio(audioFile);
        }


    }

}

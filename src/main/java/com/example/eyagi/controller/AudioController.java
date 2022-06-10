package com.example.eyagi.controller;

import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.Books;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.repository.AudioFileRepository;
import com.example.eyagi.repository.AudioPreRepository;
import com.example.eyagi.security.JwtProperties;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.AudioService;
import com.example.eyagi.service.AwsS3Service;
import com.example.eyagi.service.BooksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import javax.sound.sampled.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AudioController extends JwtProperties {

    private final AwsS3Service awsS3Service;
    private final AudioBookRepository audioBookRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioPreRepository audioPreRepository;
    private final BooksService booksService;
    private final AudioService audioService;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //자른 오디오 지정 경로
    static String path = "src/main/resources/static/"; //로컬테스트
//
//    static String path = "/home/ubuntu/eyagi/audio/";  //배포시

//    @Value("{$audio_path}")
//    static String path= filePath;  //배포시



    //성우가 해당 책에 오디오북을 처음 만드는 건지 확인해주는 부분.
    @PostMapping("/book/detail/newAudio/check/{bookId}")
    public boolean newAudioCheck (@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Books book = booksService.findBook(bookId);
        User seller = userDetails.getUser();
        AudioBook audioBook = audioBookRepository.findByBookAndSeller(book, seller);
       if(audioBook == null){
            return true; //첫 등록이면 true
        } else {
           return false; //등록한 적이 있다면 false
       }
    }

//    //오디오북 등록하기.
//    @PostMapping("/book/detail/newAudio/{bookId}")
//    public ResponseEntity<String> newAudioBook(@PathVariable Long bookId,
//                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
//                                               @RequestPart(name = "audio") MultipartFile multipartFile,
//                                               @RequestPart(name = "contents", required = false) AudioDetailDto.Request contents) throws IOException, InterruptedException {
//
//        Books book = booksService.findBook(bookId);
//        User seller = userDetails.getUser();
//        AudioBook audioBook = audioBookRepository.findByBookAndSeller(book, seller);
//
//        if (audioBook == null) {
//            AudioThread audioService2 = new AudioThread(multipartFile, path, bucket);
//            audioService2.start();
//
//            try {
//                audioService2.join();
//
////                Map<String, String> fileName = awsS3Service.uploadFile(multipartFile, pathAudio);
////                String cutFileS3Url = awsS3Service.copyAudioUpload(bucket, path, audioService2.getCutFile(),
////                        audioService2.getCutFileS3());
////
////
////                AudioPreview audioPreview = AudioPreview.builder()
////                        .originName(audioService2.getCutFileS3())
////                        .s3FileName(cutFileS3Url)
////                        .build();
////                audioPreRepository.save(audioPreview);
////
////                AudioBook audioBook1 = AudioBook.builder()
////                        .seller(seller)
////                        .book(book)
////                        .preview(audioPreview)
////                        .contents(contents.getContents()) // 특정 도서에 오디오 개시때만 등록.
////                        .build();
////                audioBookRepository.save(audioBook1);
////
////                AudioFile audioFile = AudioFile.builder()
////                        .originName(fileName.get("fileName"))
////                        .s3FileName(fileName.get("url"))
////                        .audioBook(audioBook1)
////                        .build();
////                audioFileRepository.save(audioFile);
////
//////                book.addAudioBook(audioBook1);
////
////
////                audioService.removeFile(path, audioService2.getCutFile());
////                audioService.removeFile(path, audioService2.getLocalFile());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            log.info("첫 오디오북 등록 성공! 등록한 셀러 : {}", seller.getId());
//            return ResponseEntity.ok("오디오북 첫 개시에 성공하였습니다!");
//        }
//        else {
//
//            Map<String, String> fileName = awsS3Service.uploadFile(multipartFile, pathAudio);
//
//            AudioFile audioFile = AudioFile.builder()
//                    .originName(fileName.get("fileName"))
//                    .s3FileName(fileName.get("url"))
//                    .audioBook(audioBook)
//                    .build();
//
//            audioBook.addAudio(audioFile);
//            audioFileRepository.save(audioFile);
//
//            log.info("오디오북 등록 성공! 등록한 셀러 : {}", seller.getId());
//            return ResponseEntity.ok("오디오 등록 성공!");
//
//        }
//
//    }


    //wav를 mp3로 변환해줌. -> wav가 용량이 크니, 저장할 때 사용해도 좋을 듯하다.
//
//    @PostMapping("/test")
//    public void converterTest (@RequestPart(name = "audio") MultipartFile multipartFile) throws IOException, UnsupportedAudioFileException {
//        String localFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
//        String [] a = localFile.split("\\.");
//        System.out.println(localFile);
//        System.out.println(a[0]);
//        System.out.println(a[1]);
//
//        File file = new File(path + localFile); // 저장하고 싶은 경로 + 지정하고픈 새 파일 이름을 파라미터로 담아서 생성
//        file.createNewFile();
//        FileOutputStream fos = new FileOutputStream(file);
//        BufferedOutputStream bos = new BufferedOutputStream(fos);  //조금 더 빠르게 파일을 읽기 위해 Buffer를 사용함.
//        bos.write(multipartFile.getBytes());  //바이트를 불러서 읽어준다.
//        bos.close();
//        boolean succeeded;
//        try {
//            File source = new File(path + localFile);
//            File target = new File(path + a[0] + "gg");
//
//            //Audio Attributes
//            AudioAttributes audio = new AudioAttributes();
//            audio.setCodec("libmp3lame");
//            audio.setBitRate(128000);
//            audio.setChannels(2);
//            audio.setSamplingRate(44100);
//
//            //Encoding attributes
//            EncodingAttributes attrs = new EncodingAttributes();
//            attrs.setInputFormat(".wav");
//            attrs.setAudioAttributes(audio);
//
//            //Encode
//            Encoder encoder = new Encoder();
//            encoder.encode(new MultimediaObject(source), target, attrs);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            succeeded = false;
//        }
////        cutAudio(file, "gggg", 1, 60);
//
//
//    }

}




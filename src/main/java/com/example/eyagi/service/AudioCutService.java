package com.example.eyagi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.eyagi.converter.Converter;
import com.example.eyagi.dto.AudioCutDto;
import com.example.eyagi.model.*;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.repository.AudioFileRepository;
import com.example.eyagi.repository.AudioPreRepository;
import com.example.eyagi.security.UserDetailsImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.eyagi.security.JwtProperties.filePath;
import static com.example.eyagi.service.AwsS3Path.pathAudio;

//쓰레드를 사용할때 정적 변수를 사용하는게 안좋다고 봣는데, 메서드가 직접적으로 쓰레드에 걸려있는게 아니여도 관계가 있...을..있겠죠? 안쓰는게 낫겠죠?
@Slf4j
@Getter
@RequiredArgsConstructor
@Service //service를 먹여서 빈에 등록되게 해주면 자가 호출이 가능해진다.
public class AudioCutService {


    private final AwsS3Service awsS3Service;
    private final AmazonS3 amazonS3;
    private final AudioBookRepository audioBookRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioPreRepository audioPreRepository;
    private final BooksService booksService;
    private final AudioService audioService;
    private final Converter converter;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //자른 오디오 지정 경로
//    static String path = "src/main/resources/static/"; //로컬테스트
//
//    static String path = "/home/ubuntu/eyagi/audio/";  //배포시

    @Value("{$audio_path}")
    static String path = filePath;  //배포시

    //어떤쓰레드를 사용하고 있는지 알려주는 로그 메서드.
    private static void log(String content) {
        log.info(Thread.currentThread().getName() + "> " + content);
    }

    //쓰레드 풀 생성.
//    ExecutorService executorService = Executors.newCachedThreadPool();

    // CompletionHandler를 구현한다.

    private final CompletionHandler<Map<String, String>, Void> completionHandler = new CompletionHandler<Map<String, String>, Void>() {
        // 작업이 성공적으로 종료된 경우 불리는 콜백 (작업 2)
        @Override
        public void completed(Map<String, String> result, Void attachment) {
            log("작업 2 시작 (작업 1의 결과: " + result + ")");
            try {
                removeFile(path, result.get("conversionFile")); //mp3미리듣기 삭제
                removeFile(path, result.get("originFile")); // 파일 삭제
            } catch (Exception e) {
                e.printStackTrace();
            }
            log("작업 2 종료");
        }

        // 작업이 실패했을 경우 불리는 콜백
        @Override
        public void failed(Throwable exc, Void attachment) {
            log("작업 1 실패: " + exc.toString());
        }
    };


//    @Async
    public String audioCutAsync(Long bookId, UserDetailsImpl userDetails, MultipartFile multipartFile, String contents) {
        String localFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String cutFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
//        String cutFileS3 = "audioPreview" + "/" + cutFile;

        Map<String, String> removeFileList = new HashMap<>();
        Books book = booksService.findBook(bookId);
        User seller = userDetails.getUser();

        long startTime = System.currentTimeMillis();

        log("작업 시작!");
        File file = converter.castConversion(multipartFile, path, localFile, ".mp3"); //요놈은 쓰레드 풀 밖에서 실행됨.
        try {
            Map<String, String> fileName = awsS3Service.uploadConversionFile(file, pathAudio);
//            executorService.submit(() -> { //위에서 생성한 쓰레드 풀 안에 넣어서 실행되도록 함.
            Map<String, String> converFile = run(multipartFile, path, localFile, cutFile); //형변환 1개 , mp3 파일 1개 생성.
            AudioCutDto dto = AudioCutDto.builder()
                    .originName(fileName.get("fileName"))
                    .s3FileName(fileName.get("url"))
                    .contents(contents)
                    .build();
            // S3업로드 전에, 컨버터를 거쳐서 파일을 mp3로 전환 후 S3에 업로드 한다.
            save(book, seller, dto, converFile.get("cutFileS3"), converFile.get("cutFileS3Url"));
            removeFileList.put("conversionFile", file.getName());
            removeFileList.put("originFile", localFile);
//            });
        } catch (Exception e) {
            completionHandler.failed(e, null);
        }
        completionHandler.completed(removeFileList, null);
//                completionHandler.completed(removeFileList, null);
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("걸린시간 : " + elapsedTime);
        log("작업 끝!");
        log.info("오디오북 첫 등록 성공! 등록한 셀러 : {}", seller.getId());
        return "첫 등록 완료";
    }


    @Transactional
    public Map<String, String> run(MultipartFile multipartFile, String path, String localFile, String cutFile) {

        log("오디오편집!");
//        ExampleDto dto = new ExampleDto();
        try {
            File file = new File(path + localFile); // 저장하고 싶은 경로 + 지정하고픈 새 파일 이름을 파라미터로 담아서 생성
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);  //조금 더 빠르게 파일을 읽기 위해 Buffer를 사용함.
            bos.write(multipartFile.getBytes());  //바이트를 불러서 읽어준다.
            bos.close();

            cutAudio(file, path + cutFile, 1, 60);

            log("오디오편집 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyAudioUpload(cutFile);
    }

    @Transactional
    public void cutAudio(File file, String destinationFileName, int startSecond, int secondsToCopy) {

        try {
            log("타임 확인" + file.getName());
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file); // File을 오디오형식으로 가져옴.
            AudioFormat format = fileFormat.getFormat();  //오디오를 데이터 형식으로 가져옴.
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file); // File을 읽어들임.
            //getFrameSize => 사운드의 각 프레임의 바이트 수. int형임. getFrameRate => 사운드의 1 초 쯤에 재생 또는 녹음된 프레임수. float 형임.
            int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate(); //1초에 실행되는 프레임의 바이트
            inputStream.skip(startSecond * bytesPerSecond); //지정한 바이트 수 만큼을 해당 오디오 입력 스트림에서 지움.
            long framesOfAudioToCopy = secondsToCopy * (int) format.getFrameRate(); //원하는 길이(초단위)에 해당하는 프레임
            AudioInputStream shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
            File destinationFile = new File(destinationFileName); //편집된 새로운 오디오를 담을 File 생성 (껍데기)
            AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile); //편집된 오디오를 읽어드려 File로 생성
            log("일분미리듣기는 언제?");
            inputStream.close(); //작업 끝났으니 닫아주기
            shortenedStream.close(); //작업 끝났으니 닫아주기
            System.out.println(file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }


    //    //로컬에 저장된 파일 s3로 업로드cutFile
    @Transactional
    public Map<String, String> copyAudioUpload(String cutFile) {
        File file = converter.converter(path, cutFile, ".mp3");
        String cutFileS3Url = "";
        String url = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            url = "audioPreview" + "/" + file.getName();
            amazonS3.putObject(new PutObjectRequest(bucket, url, bis, null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            removeFile(path, cutFile); //wav미리듣기 삭제
            removeFile(path, file.getName()); //wav미리듣기 삭제

            cutFileS3Url = amazonS3.getUrl(bucket, url).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> converFile = new HashMap<>();
        converFile.put("cutFileS3", url);
        converFile.put("cutFileS3Url", cutFileS3Url);
        return converFile;
    }

    //1분 미리듣기를 mp3로 바꿔준 뒤 S3로 업로드.
//    @Transactional
//    public Map<String, String> cutFileUpload(String cutFile, String fileCutNameS3) {
//        //wav미리듣기 삭제.
//
//            File file = converter.converter(path, cutFile, ".mp3");
//        String cutFileS3Url = copyAudioUpload(file, fileCutNameS3, cutFile);
//        Map<String, String> converFile = new HashMap<>();
//        converFile.put("cutFileS3", file.getName());
//        converFile.put("cutFileS3Url", cutFileS3Url);
//        return converFile;
//    }


    @Transactional
    public void removeFile(String path, String originalFile) throws IOException {
        Path filePath = Paths.get(path + originalFile); //로컬에 남은 오디오 삭제.
        Files.delete(filePath);
    }

    @Transactional
    public void save(Books book, User seller, AudioCutDto dto, String cutFileS3, String cutFileS3Url) {
        AudioPreview audioPreview = AudioPreview.builder()
                .originName(cutFileS3)
                .s3FileName(cutFileS3Url)
                .build();
        audioPreRepository.save(audioPreview);

        AudioBook audioBook1 = AudioBook.builder()
                .seller(seller)
                .book(book)
                .preview(audioPreview)
                .contents(dto.getContents()) // 특정 도서에 오디오 개시때만 등록.
                .build();
        audioBookRepository.save(audioBook1);

        AudioFile audioFile = AudioFile.builder()
                .originName(dto.getOriginName())
                .s3FileName(dto.getS3FileName())
                .audioBook(audioBook1)
                .build();
        audioFileRepository.save(audioFile);

        audioBook1.addAudio(audioFile);

    }

}

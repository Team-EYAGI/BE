package com.example.eyagi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.eyagi.dto.AudioCutDto;
import com.example.eyagi.model.*;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.repository.AudioFileRepository;
import com.example.eyagi.repository.AudioPreRepository;
import com.example.eyagi.security.UserDetailsImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

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

import static com.example.eyagi.service.AwsS3Path.pathAudio;

@Getter
@RequiredArgsConstructor
@Service //service로 빈에? 등록해주면 ... 뭐가 된다고 그랬는데...
public class AudioCutService {


    private final AwsS3Service awsS3Service;
    private final AmazonS3 amazonS3;
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

    private static void log(String content) {
        System.out.println(Thread.currentThread().getName() + "> " + content);
    }

    ExecutorService executorService = Executors.newCachedThreadPool();
    // CompletionHandler를 구현한다.

    private final CompletionHandler<String, Void> completionHandler = new CompletionHandler<String, Void>() {
        // 작업 1이 성공적으로 종료된 경우 불리는 콜백 (작업 2)
        @Override
        public void completed(String result, Void attachment) {
            log("작업 2 시작 (작업 1의 결과: " + result + ")");
            try {
//                String cutFileS3Url = copyAudioUpload(bucket, path, result.getCutFileS3(), result.getCutFileS3Url());
                removeFile(path, result);
//                save(result, cutFileS3Url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            log("작업 2 종료");
        }

        // 작업 1이 실패했을 경우 불리는 콜백
        @Override
        public void failed(Throwable exc, Void attachment) {
            log("작업 1 실패: " + exc.toString());
        }
    };

    public void finish() {
        executorService.shutdown();
    }

    public String audioCutAsync(Long bookId, UserDetailsImpl userDetails, MultipartFile multipartFile, String contents) {
        String localFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String cutFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String cutFileS3 = "audioPreview" + "/" + cutFile;

        Books book = booksService.findBook(bookId);
        User seller = userDetails.getUser();
        Map<String, String> fileName = awsS3Service.uploadFile(multipartFile, pathAudio);

        log("시작!");
        try {
            executorService.submit(() -> {
                run(multipartFile, path, localFile, cutFile);
                AudioCutDto dto = AudioCutDto.builder()
                        .originName(fileName.get("fileName"))
                        .s3FileName(fileName.get("url"))
                        .contents(contents)
                        .build();
                Map<String, String> filesName = mp3Converter(bucket, path, cutFile, cutFileS3);
//                String cutFileS3Url = copyAudioUpload(bucket, path, cutFile, cutFileS3);
                save(book, seller, dto ,filesName.get("cutFileS3"), filesName.get("cutFileS3Url"));
            });
//            completionHandler.completed(localFile, null);
        } catch (Exception e) {
            completionHandler.failed(e, null);
        }
        log("끝!");
        finish();
        return "첫 등록 완료";
    }

    //    @Async
    @Transactional
    public void run(MultipartFile multipartFile, String path, String localFile, String cutFile) {
//        String localFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
//        String cutFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
//        String cutFileS3 = "audioPreview" + "/" + cutFile;
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
//        dto.setLocalFile(localFile);
//        dto.setCutFileS3(cutFile);
//        dto.setCutFileS3Url(cutFileS3);
//        return dto;
    }


    //    public void run() {
//        String localFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
//        String cutFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
//        String cutFileS3 = "audioPreview" + "/" + cutFile;
//        log("오디오편집!");
//        try {
//            File file = new File(path + localFile); // 저장하고 싶은 경로 + 지정하고픈 새 파일 이름을 파라미터로 담아서 생성
//            file.createNewFile();
//            FileOutputStream fos = new FileOutputStream(file);
//            BufferedOutputStream bos = new BufferedOutputStream(fos);  //조금 더 빠르게 파일을 읽기 위해 Buffer를 사용함.
//            bos.write(multipartFile.getBytes());  //바이트를 불러서 읽어준다.
//            bos.close();
//
//            cutAudio(file, path + cutFile, 1, 60);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            copyAudioUpload(bucket, path, cutFile, cutFileS3);
////            save();
//            try {
//                removeFile(path, cutFile);
//                removeFile(path, localFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    @Transactional
    public void cutAudio(File file, String destinationFileName, int startSecond, int secondsToCopy) {

        try {
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }


    //    //로컬에 저장된 파일 s3로 업로드
    @Transactional
    public String copyAudioUpload(String bucket, String path, String originName, String fileCutNameS3) {
//        System.out.println(originName);
//        System.out.println(fileCutNameS3);

        try {
            FileInputStream fis = new FileInputStream(path + originName);
            BufferedInputStream bis = new BufferedInputStream(fis);

            amazonS3.putObject(new PutObjectRequest(bucket, fileCutNameS3, bis, null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            removeFile(path, originName);

            return amazonS3.getUrl(bucket, fileCutNameS3).toString();

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Transactional
    public void removeFile(String path, String originalFile) throws IOException, InterruptedException {
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

//        dto.getBook().addAudioBook(audioBook1);
    }

    public Map<String, String> mp3Converter(String bucket, String path, String originName, String fileCutNameS3) {
        boolean succeeded;
        try {
            File source = new File(path + "/" + originName + ".wav");
            File target = new File(path + "/" + originName + "mp" + ".mp3");

            //Audio Attributes
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setInputFormat("mp3");
            attrs.setAudioAttributes(audio);

            //Encode
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);

        } catch (Exception ex) {
            ex.printStackTrace();
            succeeded = false;
        }
        String cutFileS3Url = copyAudioUpload(bucket, path, originName + "mp", fileCutNameS3);
        Map<String, String> fileName = new HashMap<>();
        fileName.put("cutFileS3", originName + "mp");
        fileName.put("cutFileS3Url", cutFileS3Url);
        return fileName;
    }


}

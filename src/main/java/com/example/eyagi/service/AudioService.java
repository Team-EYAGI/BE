package com.example.eyagi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.eyagi.dto.FilePath;
import com.example.eyagi.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AudioService {

    private final AmazonS3 amazonS3;
    private final AwsS3Service awsS3Service;

    @Transactional
    public void save (FilePath filePath, User seller, Books book, String contents) {
        AudioPreview audioPreview = AudioPreview.builder()
                .originName(filePath.getCutFileS3())
                .s3FileName(filePath.getCutFileS3Url())
                .build();
        AudioBook audioBook1 = AudioBook.builder()
                .seller(seller)
                .book(book)
                .preview(audioPreview)
                .build();
        AudioFile audioFile = AudioFile.builder()
                .originName(filePath.getOriginFileS3())
                .s3FileName(filePath.getOriginFileS3Url())
                .audioBook(audioBook1)
                .num(contents)
                .build();

        audioBook1.addAudio(audioFile);
        book.addAudioBook(audioBook1);
    }

    public FilePath audioUpload (MultipartFile multipartFile, String bucket) {
        String originFileS3 = "audio" +"/" + UUID.randomUUID() + "." +
                StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());


        String originFileS3Url = awsS3Service.audioUpload(bucket, multipartFile, originFileS3, metadata);

        FilePath filePath = new FilePath();
        filePath.setOriginFileS3(originFileS3);
        filePath.setOriginFileS3Url(originFileS3Url);

        return filePath;
    }

    @Transactional
    public FilePath fristAudioBook (MultipartFile multipartFile, String path, String bucket) {
        String multipartName = multipartFile.getOriginalFilename();
        String localFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartName);
        String originFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartName);
        String originFileS3 = "audio" +"/" + localFile;
        String cutFileS3 = "audioPreview" + "/" + originFile;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        try {
            String originFileS3Url = awsS3Service.audioUpload(bucket, multipartFile, originFileS3, metadata);

            File file = fileConversion(multipartFile, path, localFile);
            copyAudio(file, path + originFile, 1, 60);

            String cutFileS3Url = awsS3Service.copyAudioUpload(bucket,path, originFile, cutFileS3, metadata);

            removeFile(path, localFile);
            removeFile(path, originFile);

            FilePath filePath = new FilePath();
            filePath.setOriginFileS3(originFileS3);
            filePath.setOriginFileS3Url(originFileS3Url);
            filePath.setCutFileS3(cutFileS3);
            filePath.setCutFileS3Url(cutFileS3Url);

            return filePath;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }



    //multipartFile -> file
    @Transactional
    public File fileConversion (MultipartFile multipartFile, String path, String localFile) throws IOException {

            File file = new File(path + localFile);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(multipartFile.getBytes());
            bos.close();
            return file;

    }

    //파일 자르고 로컬에 저장
    //destinationFileName => 편집후 저장할 파일명. 여기에 확장자를 지정해서 적으면 그 확장자로 저장이 되었음! .mp3이렇게.
    @Transactional
    public void copyAudio(File file, String destinationFileName, int startSecond, int secondsToCopy) {

        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            AudioFormat format = fileFormat.getFormat();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);

            int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate();
            inputStream.skip(startSecond * bytesPerSecond);
            long framesOfAudioToCopy = secondsToCopy * (int) format.getFrameRate();
            AudioInputStream shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
            File destinationFile = new File(destinationFileName);
            AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);

            inputStream.close();
            shortenedStream.close();

        } catch (IOException e){
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    public void removeFile (String path, String originalFile) throws IOException {
        Path filePath = Paths.get(path + originalFile); //로컬에 남은 오디오 삭제.
        Files.delete(filePath);
    }
}

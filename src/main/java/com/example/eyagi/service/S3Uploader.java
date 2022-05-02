package com.example.eyagi.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.eyagi.model.AudioPreview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3;


    //파일 바로 S3에 업로드
    @Transactional
    public String audioUpload(String bucket, MultipartFile multipartFile, String fileName ) {

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            metadata.setContentLength(multipartFile.getSize());

            amazonS3.putObject(new PutObjectRequest(bucket,fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return amazonS3.getUrl(bucket, fileName).toString();

        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //로컬에 저장된 파일 s3로 업로드
    @Transactional
    public String copyAudioUpload(String bucket, String path, String originName,String fileCutNameS3){

        try {
            FileInputStream fis = new FileInputStream(path + originName);
            BufferedInputStream bis = new BufferedInputStream(fis);

            amazonS3.putObject(new PutObjectRequest(bucket, fileCutNameS3, bis, null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            Path filePath = Paths.get(path + originName); //로컬에 남은 오디오 삭제.
            Files.delete(filePath);

            return amazonS3.getUrl(bucket, fileCutNameS3).toString();

        } catch (IOException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}

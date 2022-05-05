package com.example.eyagi.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.eyagi.model.AudioPreview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    //펀딩 업로드
    @Transactional
    public Map<String, String> uploadFile(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (multipartFile.getContentType().equals("multipart/form-data")
                && getFileExtension(multipartFile.getOriginalFilename()).equals(".mp3")) {
            objectMetadata.setContentType("audio/mp3");
        }
        if (multipartFile.getContentType().equals("multipart/form-data")
                && getFileExtension(multipartFile.getOriginalFilename()).equals(".wav")) {
            objectMetadata.setContentType("audio/basic");
        }

        //objectMetaData에 파라미터로 들어온 파일의 타입 , 크기를 할당.
        objectMetadata.setContentLength(multipartFile.getSize());

        //fileName에 파라미터로 들어온 파일의 이름을 할당.
        String fileName = multipartFile.getOriginalFilename();
        fileName = createFileName(fileName);
        try(InputStream inputStream = multipartFile.getInputStream()) {
            //amazonS3객체의 putObject 메서드로 db에 저장
            amazonS3.putObject(new PutObjectRequest(bucket, fileName , inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }

        Map<String , String> result = new HashMap<>();
        result.put("url" , String.valueOf(amazonS3.getUrl(bucket,fileName)));
        result.put("fileName" , fileName);
        return result;
    }

    private String createFileName(String fileName) { // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
        return "fund"+"/"+ UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".wav");
        fileValidate.add(".mp3");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)){
            System.out.println("idxFileName = " + idxFileName);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }


   //파일 바로 S3에 업로드
    @Transactional
    public String fileUpload(String bucket, MultipartFile multipartFile, String fileName ) {

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


//    //로컬에 저장된 파일 s3로 업로드
    @Transactional
    public String copyAudioUpload(String bucket, String path, String originName,String fileCutNameS3){

        try {
            FileInputStream fis = new FileInputStream(path + originName);
            BufferedInputStream bis = new BufferedInputStream(fis);

            amazonS3.putObject(new PutObjectRequest(bucket, fileCutNameS3, bis, null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return amazonS3.getUrl(bucket, fileCutNameS3).toString();

        } catch (IOException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }


    //S3 파일 삭제
    public void removeImage (String fileName){
        log.info("S3파일 삭제 시도 file name : "+ fileName);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
        log.info("S3파일 삭제 완료 file name : "+ fileName);
    }

}

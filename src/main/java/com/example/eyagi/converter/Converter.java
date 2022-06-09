package com.example.eyagi.converter;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Component
public class Converter {

    /*파일 이름 받고, 변환해주기 . 변환하려는 확장자도 입력으로 받을 수 있게 만들기.
    //2가지 경우에 쓰임. wav로 바꿀때 / mp3로 바꿀때 .
    //파라미터로 받아야하는 것들 : 1.만들려는 확장자 . wav인지 mp3인지 . 2. 들어온 확장자 wav인지 mp3인지 m4a인지.
    wav로 바꾼경우, mp3로 다시 변환할 필요없이 들어왔던 확장자를 올려주고 wav만 지우면 되겠다.!!!
    처음부터 wav로 들어왔을 경우만 mp3로 변환해서 저장해주면 되겠다 !
    */

//    private String a = StringUtils.getFilenameExtension(localFile);
private static void log(String content) {
    System.out.println(Thread.currentThread().getName() + "> " + content);
}


    public File converter(String path, String localFile, String extension) {
        log("컨버터 시작");
        String [] a = localFile.split("\\.");
        System.out.println(localFile);
        System.out.println("파일명 : " + a[0]);
        System.out.println("확장자명 : "+ a[1]);
        boolean succeeded;
        File source = new File(path  + localFile); //1 들어온 확장자
        File target = new File(path  + a[0] + extension); //2 만들려는 확장자
        try {

            //Audio Attributes
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame"); // 파일 확장자
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setInputFormat(a[1]); //1 들어온 확장자
            attrs.setAudioAttributes(audio);

            //Encode
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);


        } catch (Exception ex) {
            ex.printStackTrace();
            succeeded = false;
        }
        log("컨버터 종료! 변환된 확장자명 : " + extension);
        return target;
    }

    @Transactional
    public File castConversion(MultipartFile multipartFile,String path,String localFile, String extension) {
        cast(multipartFile, path, localFile);
        log("여깁니다 1");
        return converter(path, localFile, extension);
    }

    public File cast (MultipartFile multipartFile,String path,String localFile) {
        File file = new File(path + localFile); // 저장하고 싶은 경로 + 지정하고픈 새 파일 이름을 파라미터로 담아서 생성
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);  //조금 더 빠르게 파일을 읽기 위해 Buffer를 사용함.
            bos.write(multipartFile.getBytes());  //바이트를 불러서 읽어준다.
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}

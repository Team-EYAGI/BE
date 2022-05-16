package com.example.eyagi.service;

import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.*;
import java.util.UUID;

@Getter
public class AudioService2 extends Thread {

    private final String multipartName;
    private final String localFile;
    private final String cutFile;
    private final String originFileS3;
    private final String cutFileS3;
    private final String path;
    private final String bucket;
    private final MultipartFile multipartFile;

    public AudioService2(MultipartFile multipartFile, String path, String bucket){
        this.multipartFile = multipartFile;
        this.multipartName = multipartFile.getOriginalFilename();
        this.localFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartName);
        this.cutFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartName);
        this.originFileS3 = "audio" +"/" + localFile;
        this.cutFileS3 = "audioPreview" + "/" + cutFile;
        this.path = path;
        this.bucket = bucket;
    }

    @Override
    public void run() {

        try {
            File file = new File(path + localFile);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(multipartFile.getBytes());
            bos.close();

            copyAudio(file, path + cutFile, 1, 60);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    public void copyAudio(File file, String destinationFileName, int startSecond, int secondsToCopy) {

        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            AudioFormat format = fileFormat.getFormat();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            //getFrameSize => 사운드의 각 프레임의 바이트 수. int형임. getFrameRate => 사운드의 1 초 쯤에 재생 또는 녹음된 프레임수. float 형임.
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

}

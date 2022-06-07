//package com.example.eyagi.thread;
//
//import lombok.Getter;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.sound.sampled.*;
//import java.io.*;
//import java.util.UUID;
//
//@Getter
//public class AudioThread extends Thread {
//
//    private final String multipartName;
//    private final String localFile;
//    private final String cutFile;
//    private final String cutFileS3;
//    private final String path;
//    private final String bucket;
//    private final MultipartFile multipartFile;
//
//    public AudioThread(MultipartFile multipartFile, String path, String bucket){
//        this.multipartFile = multipartFile;
//        this.multipartName = multipartFile.getOriginalFilename();
//        this.localFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartName);
//        this.cutFile = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(multipartName);
//        this.cutFileS3 = "audioPreview" + "/" + cutFile;
//        this.path = path;
//        this.bucket = bucket;
//    }
//
//    @Override
//    public void run() {
//
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
//        }
//
//    }
//
//    @Transactional
//    public void cutAudio(File file, String destinationFileName, int startSecond, int secondsToCopy) {
//
//        try {
//            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file); // File을 오디오형식으로 가져옴.
//            AudioFormat format = fileFormat.getFormat();  //오디오를 데이터 형식으로 가져옴.
//            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file); // File을 읽어들임.
//            //getFrameSize => 사운드의 각 프레임의 바이트 수. int형임. getFrameRate => 사운드의 1 초 쯤에 재생 또는 녹음된 프레임수. float 형임.
//            int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate(); //1초에 실행되는 프레임의 바이트
//            inputStream.skip(startSecond * bytesPerSecond); //지정한 바이트 수 만큼을 해당 오디오 입력 스트림에서 지움.
//            long framesOfAudioToCopy = secondsToCopy * (int) format.getFrameRate(); //원하는 길이(초단위)에 해당하는 프레임
//            AudioInputStream shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
//            File destinationFile = new File(destinationFileName); //편집된 새로운 오디오를 담을 File 생성 (껍데기)
//            AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile); //편집된 오디오를 읽어드려 File로 생성
//
//            inputStream.close(); //작업 끝났으니 닫아주기
//            shortenedStream.close(); //작업 끝났으니 닫아주기
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (UnsupportedAudioFileException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}

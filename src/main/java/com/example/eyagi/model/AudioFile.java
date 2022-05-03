package com.example.eyagi.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Setter
@Getter
@Entity
public class AudioFile extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String s3FileName; //S3 경로

    @Column(nullable = false)
    private String originName; //원본 파일명

    //오디오북이랑 조인 manytoone 양방향
    @ManyToOne
    @JoinColumn(name = "AUDIOBOOK_ID")
    private AudioBook audioBook;


    public void addAudioBook (AudioBook audioBook1) {
        this.audioBook = audioBook1;
        audioBook1.addAudio(this);
    }
    //책이랑 조인 manytoone 양방향 => 오디오북에 책이랑 조인이 되어있어서, 오디오북을 타고 책 조회가능.

//    public AudioFile (String s3FileName, String originName, AudioBook audioBook) {
//        this.s3FileName = s3FileName;
//        this.originName = originName;
//        this.audioBook = audioBook;
//    }
}

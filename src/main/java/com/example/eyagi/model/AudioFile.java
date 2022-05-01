package com.example.eyagi.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class AudioFile extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    //S3 경로
    private String s3FileName;

    //원본 파일명
    private String originName;

    //오디오북이랑 조인 manytoone 양방향
    @ManyToOne
    @JoinColumn(name = "AUDIOBOOK_ID")
    private AudioBook audioBook;

    //책이랑 조인 manytoone 양방향 => 오디오북에 책이랑 조인이 되어있어서, 오디오북을 타고 책 조회가능.


}

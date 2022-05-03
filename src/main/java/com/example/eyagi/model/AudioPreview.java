package com.example.eyagi.model;


import lombok.*;


import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Setter
@Getter
@Entity
public class AudioPreview extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String s3FileName; //S3 경로

    @Column(nullable = false)
    private String originName; //원본 파일명


}

package com.example.eyagi.model;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Library_Audio { //다대다 관계를 위한 중간 테이블
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserLibrary_ID")
    private UserLibrary userLibrary;

    @ManyToOne
    @JoinColumn(name = "AudioBook_ID")
    private AudioBook audioBook;

}

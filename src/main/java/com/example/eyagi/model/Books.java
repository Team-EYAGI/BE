package com.example.eyagi.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity


public class Books {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long BookId;

    @Column(nullable = false)
    private String bookImg;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,length=2000)
    private String summary;

    @Column(nullable = false)
    private String category;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AudioBook> audioBookList;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Library_Books> userLibrary;


    public void addAudioBook (AudioBook audioBook){
        this.audioBookList.add(audioBook);
    }

}

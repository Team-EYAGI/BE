package com.example.eyagi.model;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
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

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<AudioBook> audioBookList;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Library_Books> userLibrary;


    public void addAudioBook (AudioBook audioBook){
        this.audioBookList.add(audioBook);
    }

}

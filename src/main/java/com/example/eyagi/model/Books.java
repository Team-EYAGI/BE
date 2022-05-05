package com.example.eyagi.model;

import lombok.*;

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

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<AudioBook> audioBookList;

    @OneToMany(mappedBy = "Book")
    private List<Library_Books> userLibrary; // 내 서재

    public void addAudioBook (AudioBook audioBook){
        this.audioBookList.add(audioBook);
    }

}

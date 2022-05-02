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

    private String bookImg;

    private String author;

    private String publisher;

    private String title;
    @Column(length=2000)
    private String summary;

    private String category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<AudioBook> audioBookList;

    public void addAudioBook (AudioBook audioBook){
        this.audioBookList.add(audioBook);
    }

}

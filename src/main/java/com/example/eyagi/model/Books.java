package com.example.eyagi.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Books {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long BookId;

    @Column
    private String bookImg;
    @Column
    private String author;
    @Column
    private String publisher;
    @Column
    private String title;
    @Column(length=2000)
    private String summary;
    @Column
    private String category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<AudioBook> audioBookList;



}

package com.example.eyagi.model;


import com.example.eyagi.dto.BooksDto;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
마이페이지 > 내 서재
1. 내 서재 담기를 한 책 리스트
2. 내가 듣고 있는 오디오북 리스트
 */
@Getter
@Entity
public class UserLibrary {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToMany
    private List<Library_Books> myBook = new ArrayList<>(); // 내 서재에 담은 책 리스트

    @OneToMany
    private List<Library_Audio> myAuidoBook = new ArrayList<>();  // 내가 듣고 있는 오디오북 리스트


    public void addBook (Library_Books book){ //내 서재에 담기
        this.myBook.add(book);
    }

    public void addAuidoBook(Library_Audio audioBook){  //내 오디오북에 담기
        this.myAuidoBook.add(audioBook);
    }
}

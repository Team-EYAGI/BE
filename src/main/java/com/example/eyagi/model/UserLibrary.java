package com.example.eyagi.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
마이페이지 > 내 서재
1. 내 서재 담기를 한 책 리스트
2. 내가 듣고 있는 오디오북 리스트
 */
@NoArgsConstructor
@Getter
@Entity
public class UserLibrary {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToMany(mappedBy = "userLibrary", fetch = FetchType.LAZY)
    private List<Library_Books> myBook = new ArrayList<>(); // 내 서재에 담은 책 리스트

    @OneToMany(mappedBy = "userLibrary")
    private List<Library_Audio> myAuidoBook = new ArrayList<>();  // 내가 듣고 있는 오디오북 리스트


    public void addBook (Library_Books book){ //내 서재에 담기
        this.myBook.add(book);
    }



}

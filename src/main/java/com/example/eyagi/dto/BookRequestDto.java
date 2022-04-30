package com.example.eyagi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookRequestDto {

    private String title;
    private String contents;

/*    public BookRequestDto(BookRequest bookRequest){
        this.setTitle(bookRequest.getTitle());
        this.setContents(bookRequest.getContents());
    }*/

}

package com.example.eyagi.dto;

import lombok.Getter;


@Getter
public class CommentDto {

   private String content;

   class CommentResponesDto {
      private String content;

//      private String 이메일로 할지 이름을 -> 닉네임 개념으로 바꿔서 닉네임으로 할지 ?
   }
}

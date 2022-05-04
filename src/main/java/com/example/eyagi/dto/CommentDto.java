package com.example.eyagi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentDto {

   private String content;
   private String username; //사용자 닉네임


   public CommentDto (String content, String username){
      this.content = content;
      this.username = username;
   }

}

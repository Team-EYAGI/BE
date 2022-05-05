package com.example.eyagi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentDto {

   private Long commentId;
   private String content;
   private String title;
   private String username; //사용자 닉네임

   public CommentDto (Long id, String title,String content, String username){
      this.commentId = id;
      this.title = title;
      this.content = content;
      this.username = username;
   }

}

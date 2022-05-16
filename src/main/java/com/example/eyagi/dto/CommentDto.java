package com.example.eyagi.dto;

import com.example.eyagi.model.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@Getter
public class CommentDto {

   private Long commentId;
   private String content;
   private String title;
   private String username; //사용자 닉네임
   private String createdAt;

   public CommentDto (Comment comment){
      this.commentId = comment.getId();
      this.title = comment.getTitle();
      this.content = comment.getContent();
      this.username = comment.getUser().getUsername();
      this.createdAt = comment.getCreatedAt().toString();
   }

}

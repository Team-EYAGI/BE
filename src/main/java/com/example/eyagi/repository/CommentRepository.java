package com.example.eyagi.repository;

import com.example.eyagi.model.Comment;
import com.example.eyagi.repository.QRepository.CommentCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAudioBook_Id(Long id);

//    @Query(value = "select distinct c.id as commentId, c.content as content, c.title as title, c.user.username as username, " +
//            "c.createdAt as createdAt from Comment c join c.audioBook a where a.id=:id")
//    Page<CommentCustomRepository> findAllByAudioBook_Id(Long id, Pageable pageable);
}

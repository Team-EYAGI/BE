
package com.example.eyagi.repository;


import com.example.eyagi.model.BookRequest;
import com.example.eyagi.repository.QRepository.BookRequestCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest,Long> {
   List<BookRequest> findAllByOrderByModifiedAtDesc();
   List<BookRequest> findAllByOrderByBookRequestIdDesc();
   Optional<BookRequest> findByUserEmail(String username);

   @Query(value = "select br.bookRequestId as bookRequestId, br.title as title, br.contents as contents, br.user.username as userName," +
           "br.bookId as bookId, br.createdAt as createdAt from BookRequest br")
   Page<BookRequestCustomRepository> findAllByOrderByBookRequestId(Pageable pageable);
}

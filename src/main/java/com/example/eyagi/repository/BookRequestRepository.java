
package com.example.eyagi.repository;


import com.example.eyagi.model.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest,Long> {
   List<BookRequest> findAllByOrderByModifiedAtDesc();
   List<BookRequest> findAllByOrderByBookRequestIdDesc();

   Optional<BookRequest> findByUserEmail(String username);
}

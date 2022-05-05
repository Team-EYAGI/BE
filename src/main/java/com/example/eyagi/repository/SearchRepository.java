package com.example.eyagi.repository;

import com.example.eyagi.dto.BookSearchDto;
import com.example.eyagi.model.Books;
import com.example.eyagi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRepository {
    private final EntityManager em;

    //책검색
     public List<Books>searchBooks(String search){
         return em.createQuery("select b from Books b where b.title like :title and b.author like :author",Books.class)
                 .setParameter("title", "%"+search+"%")
                 .setParameter("author", "%"+search+"%")
                 .getResultList();
     }
     //판매자 검색
     public List<User>searchSeller(String search){
         return em.createQuery("select u from User u where u.role = com.example.eyagi.model.UserRole.SELLER and u.username like:username ", User.class)
                 .setParameter("username", "%"+search+"%")
                 .getResultList();
     }

}

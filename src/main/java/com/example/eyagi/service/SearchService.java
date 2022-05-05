package com.example.eyagi.service;

import com.example.eyagi.dto.BookSearchDto;
import com.example.eyagi.dto.UserSearchDto;
import com.example.eyagi.model.Books;

import com.example.eyagi.model.User;
import com.example.eyagi.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;

    public List<BookSearchDto> findBooks(String search){
        List<Books> booksList = searchRepository.searchBooks(search);
        List<BookSearchDto> bookSearchDtoList = new ArrayList<>();
         for(Books b : booksList){
             bookSearchDtoList.add(new BookSearchDto(b));
         }
         return bookSearchDtoList;
    }

    public List<UserSearchDto> findSeller(String search) {
        List<User> userList = searchRepository.searchSeller(search);
        List<UserSearchDto> userSearchDtoList = new ArrayList<>();
        for(User u : userList){
            userSearchDtoList.add(new UserSearchDto(u));
        }
        return userSearchDtoList;
    }
}

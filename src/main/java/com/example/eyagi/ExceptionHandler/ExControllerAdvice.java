package com.example.eyagi.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalExHandle(IllegalArgumentException e){
        log.error(e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity illegalExHandle(NullPointerException e){
//        log.error(e.getMessage());
//        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
//    }

}

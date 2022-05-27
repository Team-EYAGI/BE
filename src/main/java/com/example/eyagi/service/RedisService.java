package com.example.eyagi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {


    private final StringRedisTemplate stringRedisTemplate;

    ValueOperations<String, String> values;

    // 키-벨류 설정
    public void setValues(String token, String email){
        values = stringRedisTemplate.opsForValue();

        values.set(token, email, Duration.ofMinutes(10));  // 3분 뒤 메모리에서 삭제된다.

        System.out.println("레디스에 저장! " + token + email);
    }

    // 키값으로 벨류 가져오기
    public String getValues(String token){
        values = stringRedisTemplate.opsForValue();
        return values.get(token);
    }

//     키-벨류 삭제
    public void delValues(String token) {
        stringRedisTemplate.delete(token.substring(7));
    }

}

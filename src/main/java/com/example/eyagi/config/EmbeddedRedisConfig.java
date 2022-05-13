package com.example.eyagi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Profile("54.180.115.121")
//@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

    private RedisServer redisServer;

    @PostConstruct
    public void start() {
        redisServer = new RedisServer(6379);
        System.out.println("레디스 시작");
        redisServer.start();
    }

    @PreDestroy
    public void stop() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

}

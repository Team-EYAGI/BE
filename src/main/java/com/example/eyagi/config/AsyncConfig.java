package com.example.eyagi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);  //스레드 풀에서 사용되는 스레드의 갯수
        executor.setMaxPoolSize(10);  //스레드 풀의 스레드 최대 허용치
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("audio-async-");
        executor.initialize();
        return executor;
    }
}

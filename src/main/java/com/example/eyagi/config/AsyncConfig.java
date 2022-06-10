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
        executor.setCorePoolSize(10);  //스레드 풀에서 동시에 사용되는 스레드의 갯수
        executor.setMaxPoolSize(20);  //스레드 풀의 스레드 최대 허용치 /
        executor.setQueueCapacity(100); //CorePoolSize가 모두 사용중일 경우 QueueCapacity에 작업을 적제.
        executor.setThreadNamePrefix("audio-async-");
//        executor.setKeepAliveSeconds(60);
//        executor.setWaitForTasksToCompleteOnShutdown(true); //프로세스를 종료할 경우, Queue에 대기중인 작업이 모두 완료될때까지 종료는 보류된다.
        executor.initialize();
        return executor;
    }
}

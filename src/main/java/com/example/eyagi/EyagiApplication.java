package com.example.eyagi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EyagiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EyagiApplication.class, args);

    }

}

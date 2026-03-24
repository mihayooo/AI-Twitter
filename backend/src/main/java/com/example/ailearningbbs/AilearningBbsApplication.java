package com.example.ailearningbbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.ailearningbbs.repository")
public class AilearningBbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AilearningBbsApplication.class, args);
    }
}
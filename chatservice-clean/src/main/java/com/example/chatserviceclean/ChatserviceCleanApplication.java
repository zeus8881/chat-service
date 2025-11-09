package com.example.chatserviceclean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class})
public class ChatserviceCleanApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatserviceCleanApplication.class, args);
    }

}

package com.example.userserviceclean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class})
public class UserserviceCleanApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserserviceCleanApplication.class, args);
    }

}

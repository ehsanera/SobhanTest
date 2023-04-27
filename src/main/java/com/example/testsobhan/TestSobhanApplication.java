package com.example.testsobhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableElasticsearchRepositories("com.example.testsobhan.repository.elastic")
@EnableScheduling
public class TestSobhanApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestSobhanApplication.class, args);
    }

}

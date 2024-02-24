package com.example;

import com.example.configs.AnalyzerKafkaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NewsAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsAnalyzerApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    CommandLineRunner run(AnalyzerKafkaService analyzerKafkaService) {
        return args -> {
            while (true) analyzerKafkaService.sendToMockNewsFeedTopic("This is kafka");
        };
    }
}

package com.example;

import com.example.model.FrequencyModel;
import com.example.service.NewsAnalyzeService;
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
    public FrequencyModel frq() {
        return FrequencyModel.builder()
                .enabled(true)
                .priorityTarget(8)
                .priorityDistance(1)
                .sendJustBadNews(true)
                .sendJustBadNews(null)
                .build();
    }

    @Bean
    CommandLineRunner run(NewsAnalyzeService newsAnalyzeService) {
        return args -> {
            newsAnalyzeService.GetAnalyzeResultLog();
        };
    }
}

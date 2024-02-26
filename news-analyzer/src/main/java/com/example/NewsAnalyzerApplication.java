package com.example;

import com.example.model.FrequencyModel;
import com.example.service.NewsAnalyzerService;
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

    //Sample Frequency as bean. you can change it.
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
    CommandLineRunner run(NewsAnalyzerService newsAnalyzerService) {
        return args -> {

            newsAnalyzerService.getAnalyzeResultLog();

        };
    }

}
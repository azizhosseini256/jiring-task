package com.example;

import com.example.configs.AnalyzerKafkaConfiguration;
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
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public FrequencyModel frq() {
        return FrequencyModel.builder()
                .Enable(true)
                .PriorityTarget(8)
                .PriorityDistance(1)
                .SendJustBadNews(true)
                .SendJustBadNews(null)
                .build();
    }

    @Bean
    CommandLineRunner run(AnalyzerKafkaConfiguration analyzerKafkaConfiguration, NewsAnalyzeService newsAnalyzeService) {
        return args -> {
            newsAnalyzeService.loggingWanted();
        };
    }
}

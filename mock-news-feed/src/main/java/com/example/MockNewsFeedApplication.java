package com.example;

import com.example.configs.NewsKafkaConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Random;

@SpringBootApplication
public class MockNewsFeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockNewsFeedApplication.class, args);
    }

    @Bean
    Random random() {
        return new Random();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    CommandLineRunner run(NewsKafkaConfiguration newsKafkaConfiguration) {
        return args -> {

            newsKafkaConfiguration.startBroadcastingMockNews();

        };
    }
}
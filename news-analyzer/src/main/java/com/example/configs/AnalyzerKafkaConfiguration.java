package com.example.configs;

import com.example.model.FrequencyModel;
import com.example.model.NewsModel;
import com.example.service.NewsAnalyzeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Service
public class AnalyzerKafkaConfiguration {

    @Value("${topic.mockNewsFeedTopic}")
    private String mockNewsFeedTopic;

//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsAnalyzeService newsAnalyzeService;

    @Autowired
    private FrequencyModel frq;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(mockNewsFeedTopic).build();
    }


    @KafkaListener(topics = "newsAnalyzerTopic", groupId = "groupId1")
    public void receive(String newsModelString) throws JsonProcessingException {

        NewsModel newsModel = newsModelFromString(newsModelString);
        newsAnalyzeService.filterAndManagementNewsByFrequency(newsModel,frq);



    }

    public NewsModel newsModelFromString(String newsModelString){
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(newsModelString, NewsModel.class);
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

//    @Value("${topic.newsAnalyzerTopic}")
//    private String newsAnalyzerTopic;

    public void sendDataToNewsAnalyzerService(Object object){
        kafkaTemplate().send("mockNewsFeedTopic",object);
    }




}
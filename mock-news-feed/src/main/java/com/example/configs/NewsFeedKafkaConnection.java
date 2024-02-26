package com.example.configs;

import com.example.model.NewsModel;
import com.example.service.MockNewsGeneratorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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
import java.util.HashMap;
import java.util.Map;

@Configuration
public class NewsFeedKafkaConnection {

    @Value("${topic.newsAnalyzerTopic}")
    private String newsAnalyzerTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private MockNewsGeneratorService mockNewsGeneratorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public NewTopic topic() {

        return TopicBuilder.name(newsAnalyzerTopic).build();

    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {

        return new KafkaTemplate<>(producerFactory());

    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaConsumerFactory<>(config);
    }

    public void sendData(Object eventObject) {

        kafkaTemplate().send(newsAnalyzerTopic, eventObject);

    }

    //Send mock news to News Analyzer service.
    public void startBroadcastingMockNews() {

        while (true) sendData(mockNewsGeneratorService.getMockNews());

    }

    //Receive and logging analyzed news from News Analyzer service.
    @SneakyThrows(value = {JsonProcessingException.class})
    @KafkaListener(topics = "mockNewsFeedTopic", groupId = "groupId1")
    public void AnalyzerConsumer(ConsumerRecord<String, String> newsEvent) {

        NewsModel analyzedNewsModel = objectMapper.readValue(newsEvent.value(), NewsModel.class);
        System.out.println(analyzedNewsModel.toString());

    }

}
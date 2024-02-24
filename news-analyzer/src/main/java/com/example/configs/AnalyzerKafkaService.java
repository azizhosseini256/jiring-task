package com.example.configs;

import com.example.model.FrequencyModel;
import com.example.model.NewsModel;
import com.example.service.NewsAnalyzer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;

@Configuration
@Service
public class AnalyzerKafkaService {

    @Value("${topic.mockNewsFeedTopic}")
    private String mockNewsFeedTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsAnalyzer newsAnalyzer;

    @Bean
    public NewTopic topic (){
        return TopicBuilder.name(mockNewsFeedTopic).build();
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public void sendToMockNewsFeedTopic(Object object){
        kafkaTemplate().send(mockNewsFeedTopic,object);
    }

    @KafkaListener(topics = "newsAnalyzerTopic", groupId = "groupId1")
    public void receive(ConsumerRecord<String, String> event) throws JsonProcessingException {

        NewsModel news = objectMapper.readValue(event.value(), NewsModel.class);
        sendToMockNewsFeedTopic(news.toString());

    }

    private FrequencyModel getFrequency() {
        return FrequencyModel.builder()
                .Enable(true)
                .PriorityTarget(4)
                .PriorityDistance(1)
                .SendJustGoodNews(true)
                .SendJustBadNews(null)
                .SendUniqueTitle(null)
                .build();
    }

}
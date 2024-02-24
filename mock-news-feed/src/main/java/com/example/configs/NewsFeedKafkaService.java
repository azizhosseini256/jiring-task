package com.example.configs;

import com.example.model.NewsModel;
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
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Service
public class NewsFeedKafkaService {

    @Value("${topic.newsAnalyzerTopic}")
    private String newsAnalyzerTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public NewTopic topic (){
        return TopicBuilder.name(newsAnalyzerTopic).build();
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

    public void sendDataToNewsAnalyzerTopic(Object object){
        kafkaTemplate().send(newsAnalyzerTopic,object);
    }

    public void sendDataToNewsAnalyzerTopic(Object[] objects){
        kafkaTemplate().send(newsAnalyzerTopic,objects);
    }

    @KafkaListener(topics = "mockNewsfeedTopic", groupId = "groupId1")
    public void recive(ConsumerRecord<String, String> event) throws JsonProcessingException {

        var eventModel = objectMapper.readValue(event.value(), NewsModel.class);
        System.out.println(eventModel.getText());
    }
}
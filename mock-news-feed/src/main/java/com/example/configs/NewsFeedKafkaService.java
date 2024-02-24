package com.example.configs;

import com.example.service.NewsMockGeneratorService;
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

    @Autowired
    private NewsMockGeneratorService newsMockGeneratorService;

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

    private void sendDataToNewsAnalyzer(Object object){
        if (object != null)
        kafkaTemplate().send(newsAnalyzerTopic,object);
    }

    @KafkaListener(topics = "mockNewsFeedTopic", groupId = "groupId1")
    public void receiveNewsFromAnalyzer(ConsumerRecord<String, String> news)  {
        System.out.println(news.value());
    }

    public void startBroadcastingMockNews(){
        while (true) sendDataToNewsAnalyzer(newsMockGeneratorService.randomNewsGenerator());
    }

}
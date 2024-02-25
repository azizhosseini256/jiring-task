package com.example.configs;

import com.example.model.FrequencyModel;
import com.example.model.LoggingModel;
import com.example.model.NewsModel;
import com.example.repository.NewsDao;
import com.example.service.NewsAnalyzeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Service
public class AnalyzerKafkaConfiguration {

    @Value("${topic.mockNewsFeedTopic}")
    private String otherTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(otherTopic).build();

    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    public void sendData(Object eventObject){
        kafkaTemplate().send(otherTopic, eventObject);
    }

    ///////////////////////////////////////////////////////////////////////////

    //KafkaListener Sahih Object
    @KafkaListener(topics = "newsAnalyzerTopic", groupId = "groupId1")
    public void AnalyzerConsumer(ConsumerRecord<String, String> news) throws JsonProcessingException {

        NewsModel newsModel = objectMapper.readValue(news.value(), NewsModel.class);

        NewsModel filteredNewsModel = newsAnalyzeService.filterAndManagementNewsByFrequency(newsModel,frq);
        if (filteredNewsModel !=null) sendData(filteredNewsModel);

    }

    ///////////////////////////////////////////////////////////////////////////

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private NewsAnalyzeService newsAnalyzeService;

    @Autowired
    private FrequencyModel frq;

    @Autowired
    @Scheduled(fixedRate = 10000)
    public String sendFilteringLogToConsole(){
        return logging();
    }

    public String logging(){

        LocalDateTime tenSecondsAgo = LocalDateTime.now().minusSeconds(10);

        int goodNewsInLast10Sec = newsDao.goodNewsInLast10Sec(tenSecondsAgo);
        List<String> unique3TitlesInLast10Sec = newsDao.unique3TitlesInLast10Sec(tenSecondsAgo);

        LoggingModel log = new LoggingModel();
        log.setGoodNewsInLast10Sec(goodNewsInLast10Sec);
        log.setUnique3TitlesInLast10Sec(unique3TitlesInLast10Sec);
        System.err.println(log.toString());
        return log.toString();
    }

}
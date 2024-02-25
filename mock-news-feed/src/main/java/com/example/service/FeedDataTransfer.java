package com.example.service;

import com.example.configs.NewsKafkaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedDataTransfer {

    @Autowired
    private NewsKafkaConfiguration newsKafka;

    @Autowired
    private MockNewsGeneratorService mockNewsGeneratorService;

//    public void send

}

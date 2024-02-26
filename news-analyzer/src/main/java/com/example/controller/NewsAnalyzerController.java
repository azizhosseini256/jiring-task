package com.example.controller;

import com.example.configs.AnalyzerKafkaConnection;
import com.example.model.FrequencyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analyzer")
public class NewsAnalyzerController {

    @Autowired
    private AnalyzerKafkaConnection analyzerKafkaConnection;

    @PutMapping("/get-frq")
    public ResponseEntity<Void> getFrq(@RequestBody FrequencyModel frequencyModel){

        return ResponseEntity.ok().build();

    }

}

package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LoggingModel {

//    private FrequencyModel frq;
    private int rangeFiltered;
    private int goodNewsFiltered;
    private int badNewsFiltered;
    private int goodNewsOn10Sec;
    private List<String> unique3TitlesOn10Sec;

}
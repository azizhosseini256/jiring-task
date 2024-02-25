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

    private int goodNewsInLast10Sec;
    private List<String> unique3TitlesInLast10Sec;

}
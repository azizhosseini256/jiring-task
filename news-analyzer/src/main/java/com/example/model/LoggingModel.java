package com.example.model;

import lombok.Data;
import java.util.List;

@Data
public class LoggingModel {

    private int countGoodNewsInLast10Sec;
    private List<String> titleOf3UniqueNewsInLast10Sec;

}
package com.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrequencyModel {

    private boolean enabled;
    private Integer priorityTarget;
    private Integer priorityDistance;
    private Boolean sendJustGoodNews;
    private Boolean sendJustBadNews;

}
package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FrequencyModel {

    private boolean enabled;
    private Integer priorityTarget;
    private Integer priorityDistance;
    private Boolean sendJustGoodNews;
    private Boolean sendJustBadNews;

}
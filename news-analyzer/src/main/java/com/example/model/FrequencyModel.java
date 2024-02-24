package com.example.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FrequencyModel {

    private boolean Enable;
    private Integer PriorityTarget;
    private Integer PriorityDistance;
    private Boolean SendJustGoodNews;
    private Boolean SendJustBadNews;

}
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

    private boolean Enable;
    private Integer PriorityTarget;
    private Integer PriorityDistance;
    private Boolean SendJustGoodNews;
    private Boolean SendJustBadNews;
    private Boolean SendUniqueTitle;

}
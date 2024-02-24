package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrequencyModel {

    private boolean Enable;
    private Integer PriorityTarget;
    private Integer PriorityDistance;
    private Boolean SendJustGoodNews;
    private Boolean SendJustBadNews;
    private Boolean SendUniqueTitle;

}
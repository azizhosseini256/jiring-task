package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsModel {

    private int priority;
    private String text;
    private Date createdDate;
    private String title;
    private boolean isGoodNews;
    private boolean isUniqueNews;

}
package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsModel {

    private int priority;
    private String text;
    private String createdDate;
    private String title;

}
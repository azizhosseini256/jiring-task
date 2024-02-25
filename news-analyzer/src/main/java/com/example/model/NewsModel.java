package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsModel {

    /*

    private int priority;
    private String text;
    private String title;
    private LocalDateTime createdDate;

     */

    private int priority;
    private String text;
    private String title;
//    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
//    private LocalDateTime createdDate;
    //
//    private boolean isGoodNews;
//    private boolean isUniqueNews;

}
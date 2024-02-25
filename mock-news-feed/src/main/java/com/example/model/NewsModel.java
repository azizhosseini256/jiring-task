package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsModel {

    private int priority;
    private String text;
    private String title;
//    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
//    private LocalDateTime createdDate;

}
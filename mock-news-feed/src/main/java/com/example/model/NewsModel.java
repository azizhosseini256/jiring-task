package com.example.model;

import lombok.*;

@Data
public class NewsModel {

    private int priority;
    private String createdDate;
    private String text;
    private String title;

}
package com.example.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "jiring_db",name = "news_tb")
public class NewsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -4289465051466486705L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    @Column (name = "title")
    private String title;

    @Column (name = "priority")
    private int priority;

    @Column (name = "text")
    private String text;

    @Column (name = "is_good_news")
    private boolean isGoodNews;

    @Column (name = "is_unique_news")
    private boolean isUniqueNews;

    @Column (name = "created_date")
    private Date createdDate;
}
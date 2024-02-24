package com.example.service;

import com.example.model.NewsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsMockGeneratorService {

    @Autowired
    private Random random;

    @Value("${words.allWords}")
    private List<String> allWords;

    private int randomTitleLenGenerator() {
        return random.nextInt(3) + 3;
    }

    private int randomPriorityGenerator() {
        int randomNumber = random.nextInt(10);
        return (randomNumber > 4 && random.nextBoolean()) ? randomNumber - 4 : randomNumber;
    }

    private String randomTitleGenerator() {

        return
                random
                        .ints(randomTitleLenGenerator(), 0, allWords.size())
                        .mapToObj(allWords::get)
                        .collect(Collectors.joining(" "));

    }

    public NewsModel randomNewsGenerator() {
        NewsModel news = new NewsModel();
        news.setPriority(randomPriorityGenerator());
        news.setTitle(randomTitleGenerator());
        news.setText(UUID.randomUUID().toString());
        news.setCreatedDate(new Date());
        return news;
    }

}
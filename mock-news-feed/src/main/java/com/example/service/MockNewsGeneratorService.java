package com.example.service;

import com.example.model.NewsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MockNewsGeneratorService {

    @Autowired
    private Random random;

    @Value("${words.allWords}")
    private List<String> allWords;

    //Random int int range 3~5
    private int randomTitleLengthGenerator() {

        return random.nextInt(3) + 3;

    }

    //Random int in range 0~9 with more chance for upper numbers.
    //If number is in second half of the range, shade it sometimes!
    private int randomPriorityGenerator() {
        int randomNumber = random.nextInt(10);
        return (randomNumber > 4 && random.nextBoolean()) ? randomNumber - 4 : randomNumber;
    }

    //IntStream ints(long streamSize, int randomNumberOrigin, int randomNumberBound)
    private String randomTitleGenerator() {

        return
                random
                        .ints(randomTitleLengthGenerator(), 0, allWords.size())
                        .mapToObj(allWords::get)
                        .collect(Collectors.joining(" "));

    }

    //Create mock news.
    public NewsModel getMockNews() {
        NewsModel news = new NewsModel();
        news.setPriority(randomPriorityGenerator());
        news.setTitle(randomTitleGenerator());
        news.setCreatedDate(LocalDateTime.now().toString());
        news.setText("UUID: ".concat(UUID.randomUUID().toString()));
        return news;
    }

}
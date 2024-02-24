package com.example.service;

import com.example.model.FrequencyModel;
import com.example.model.LoggingModel;
import com.example.model.NewsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@EnableScheduling
public class FilterService {

    @Value("${words.goodWords}")
    private List<String> goodWords;

    @Value("${words.badWords}")
    private List<String> badWords;

    private boolean isGoodNews(String title) {

        String[] sentenceWords = title.split(" ");

        List<String> sentenceGoodWords = Arrays

                .stream(sentenceWords)
                .filter(goodWords::contains)

                .toList();

        return ((double) sentenceGoodWords.size() / sentenceWords.length) > 0.5;
    }

    private boolean isBadNews(String title) {

        String[] sentenceWords = title.split(" ");

        List<String> sentenceBadWords = Arrays

                .stream(sentenceWords)
                .filter(badWords::contains)

                .toList();

        return ((double) sentenceBadWords.size() / sentenceWords.length) > 0.5;
    }

    private boolean isUniqueNews(NewsModel news){
        return isGoodNews(news.getTitle()) && news.getPriority() > 6;
    }

    //todo to bean validation
    private void validationFrequency(FrequencyModel frq){

        if ((frq.getPriorityTarget() <0 && frq.getPriorityTarget() >9)
        || (frq.getPriorityDistance()<0 && frq.getPriorityDistance() >9 )){
            throw new RuntimeException("EXCEPTION: OUT OF RANGE NUMBER!");
        }

        if (
                frq.getSendJustGoodNews() != null
                        && frq.getSendJustBadNews() != null
                        && frq.getSendJustGoodNews().equals(frq.getSendJustBadNews())) {
            throw new RuntimeException("Exception: LOGICAL CONFLICT IN GOO OR BAD NEWS!");
        }
        if ((
                frq.getPriorityTarget() != null && frq.getPriorityDistance() ==null)
                ||
                frq.getPriorityTarget() == null && frq.getPriorityDistance() !=null)
            throw new RuntimeException("Exception: EITHER SET BOTH OR BOTH ARE NULL");
    }

























    /*

//    private FrequencyModel frq;
//    private int countFilteredInRange;
//    private int countFilteredGoodNews;
//    private int countFilteredBadNews;
//    private int countGoodNewsOn10Sec;
    private List<String> titleOf3UniqueTitlesOn10Sec;

     */



    @Autowired
    private LoggingModel log;

    public NewsModel filterNewsByFrequency(NewsModel news, FrequencyModel frq) {

//        LoggingModel log = new LoggingModel();
//        log.setFrq(frq);


        if (frq == null || !frq.isEnable()) return news;
        validationFrequency(frq);


        if ((frq.getPriorityTarget()!=null)
                && Math.abs(news.getPriority() - frq.getPriorityTarget()) > frq.getPriorityDistance()) {
//            System.err.println("Filtered: Range");
            log.setRangeFiltered(log.getRangeFiltered()+1);
            return null;
        }

        if (frq.getSendJustGoodNews() != null
                && isGoodNews(news.getTitle()) != frq.getSendJustGoodNews()) {
//            System.err.println("Filtered: Good");
            log.setGoodNewsFiltered(log.getGoodNewsFiltered()+1);
            return null;
        }

        if (frq.getSendJustBadNews() != null
                && isBadNews(news.getTitle()) != frq.getSendJustBadNews()) {
//            System.err.println("Filtered: Bad");
            log.setBadNewsFiltered(log.getBadNewsFiltered()+1);
            return null;
        }

        news.setGoodNews(isGoodNews(news.getTitle()));

        if (isGoodNews(news.getTitle())){
            log.setGoodNewsOn10Sec(log.getGoodNewsOn10Sec()+1);
        }

        news.setUniqueNews(isUniqueNews(news));

        List<String> in3title = new ArrayList<>(3);
        if (isUniqueNews(news)){
            in3title.add(news.getTitle());
            log.setUnique3TitlesOn10Sec(in3title);
        }

        return news;
    }



    @Scheduled(fixedRate = 10000)
    public void testScheduling(){
        System.err.println(log);
        log = new LoggingModel();
    }





}
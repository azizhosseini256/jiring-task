package com.example.service;

import com.example.Domain.NewsEntity;
import com.example.model.FrequencyModel;
import com.example.model.LoggingModel;
import com.example.model.NewsModel;
import com.example.repository.NewsDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@EnableScheduling
public class NewsAnalyzeService {

    //TODO celestial!
    @Value("${words.goodWords}")
    private List<String> goodWords;

    @Value("${words.badWords}")
    private List<String> badWords;

    @Autowired
    private NewsDao newsDao;

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

    //todo fix dont return null
    public NewsModel filterMockNewsByFrequencyOrSave(NewsModel news, FrequencyModel frq) {

        if (frq == null || !frq.isEnabled()) return news;
        validationFrequency(frq);


        if ((frq.getPriorityTarget()!=null)
                && Math.abs(news.getPriority() - frq.getPriorityTarget()) > frq.getPriorityDistance()) {
            return null;
        }

        if (frq.getSendJustGoodNews() != null
                && isGoodNews(news.getTitle()) != frq.getSendJustGoodNews()) {
            return null;
        }

        if (frq.getSendJustBadNews() != null
                && isBadNews(news.getTitle()) != frq.getSendJustBadNews()) {
            return null;
        }

        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setTitle(news.getTitle());
        newsEntity.setPriority(news.getPriority());
        newsEntity.setText(news.getText());
        newsEntity.setGoodNews(isGoodNews(news.getTitle()));
        newsEntity.setUniqueNews(isUniqueNews(news));
        newsEntity.setCreatedDate(LocalDateTime.now());

        newsDao.save(newsEntity);
        return news;
    }

    @Autowired
    @Scheduled(fixedRate = 10000)
    public LoggingModel LoggingToConsole(){
        return GetAnalyzeResultLog();
    }

    public LoggingModel GetAnalyzeResultLog(){

        LocalDateTime tenSecondsAgo = LocalDateTime.now().minusSeconds(10);

        int goodNewsInLast10Sec = newsDao.goodNewsInLast10Sec(tenSecondsAgo);
        List<String> unique3TitlesInLast10Sec = newsDao.unique3TitlesInLast10Sec(tenSecondsAgo);

        LoggingModel log = new LoggingModel();
        log.setGoodNewsInLast10Sec(goodNewsInLast10Sec);
        log.setUnique3TitlesInLast10Sec(unique3TitlesInLast10Sec);

        System.err.println(log.toString());
        return log;
    }

}
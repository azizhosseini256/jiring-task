package com.example.repository;

import com.example.Domain.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsDao extends JpaRepository<NewsEntity,Long> {

    @Query(value = "SELECT COUNT(*) AS record_count FROM news_tb WHERE created_date >= :tenSecondsAgo", nativeQuery = true)
    int goodNewsInLast10Sec(@Param("tenSecondsAgo") LocalDateTime tenSecondsAgo);

    @Query(value = "SELECT n.title FROM news_tb n WHERE n.created_date >= :tenSecondsAgo order by created_date limit 3" , nativeQuery = true)
    List<String> unique3TitlesInLast10Sec (@Param("tenSecondsAgo") LocalDateTime tenSecondsAgo);

}

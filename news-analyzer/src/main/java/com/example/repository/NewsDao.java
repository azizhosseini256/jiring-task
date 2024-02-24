package com.example.repository;


import com.example.Domain.NewsEntity;
import com.example.model.NewsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsDao extends JpaRepository<NewsEntity,Long> {

    @Modifying
    @Query(value = """
        UPDATE user_tb u SET
        u.username = :#{#news.username},
        u.password = :#{#news.password},
        u.fk_prs   = :#{#news.personId}
        where u.username = :username
        """, nativeQuery = true)
    void updateUserDomainByUsername(NewsModel news);

}

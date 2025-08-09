package com.example.pethealthcare.repository;

import com.example.pethealthcare.model.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
    
    List<QuestionAnswer> findBySessionIdOrderByCreatedAtAsc(String sessionId);
    
    @Query("SELECT qa FROM QuestionAnswer qa WHERE qa.sessionId = :sessionId ORDER BY qa.createdAt DESC")
    List<QuestionAnswer> findRecentBySessionId(@Param("sessionId") String sessionId);
    
    @Query("SELECT qa FROM QuestionAnswer qa ORDER BY qa.createdAt DESC")
    List<QuestionAnswer> findAllOrderByCreatedAtDesc();
}
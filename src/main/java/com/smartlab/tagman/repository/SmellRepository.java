package com.smartlab.tagman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.smartlab.tagman.model.Sample;
import com.smartlab.tagman.model.Smell;

@Component
public interface SmellRepository extends JpaRepository<Smell, Long> {
    
}

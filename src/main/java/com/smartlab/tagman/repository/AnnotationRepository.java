package com.smartlab.tagman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.smartlab.tagman.model.Annotation;

@Component
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {
    
	public List<Annotation> findByUserId(Long userId);
	
}

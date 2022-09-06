package com.smartlab.tagman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.smartlab.tagman.model.Annotation;
import com.smartlab.tagman.model.InstructorMarking;
import com.smartlab.tagman.model.Sample;

@Component
public interface InstructorRepository extends JpaRepository<InstructorMarking, Long> {
    
}
